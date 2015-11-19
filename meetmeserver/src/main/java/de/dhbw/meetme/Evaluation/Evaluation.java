package de.dhbw.meetme.Evaluation;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.logic.GPSLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by vrettos on 18.11.2015.
 */
public class Evaluation {
    private static final Logger log = LoggerFactory.getLogger(Evaluation.class);

    @Inject
    GPSLogic geoLogic;

    int index = 0;


    public String evaluate(String username){
        List<GPSData> list =  geoLogic.listGeoandTimeStamp(username);
        List <List<GPSData>> all = new ArrayList<>();

        /*hier wird die liste all zusammengebastelt
        while(index < list.size()){
            List<GPSData> row = findRow(list);
            all.add(row);
            log.debug("der neue index in evaluate ist: " + index);
            log.debug("und die neue all ist " + all.toString());
        }*/
        int counter = 0;
        boolean first = true;
        double oldLat = 0;
        double oldLong = 0;
        double middleLat = 0;
        double middleLong = 0;
        double tempLat = 0;
        double tempLong = 0;
        double newLat;
        double newLong;
        List<GPSData> row = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            GPSData data = list.get(i);
            //erste Frage: ist das erste Element schon in der Reihe?
            if (first) {
                row.clear();
                tempLat = Double.parseDouble(data.getLatitude());
                tempLong = Double.parseDouble(data.getLongitude());
                oldLat = tempLat;
                oldLong = tempLong;
                row.add(data);
                counter++;
                first = false;
            } else {
                //das erste Element ist also schon vorhanden - jetzt prüfen wir die Nähe
                newLat = Double.parseDouble(data.getLatitude());
                newLong = Double.parseDouble(data.getLongitude());

                if (isClose(tempLat, tempLong, newLat, newLong)) {
                    //jetzt sind sie beieinander, die nächsten GeoDaten werden in die Reihe eingefügt
                    row.add(data);
                    counter++;

                    if (counter != 0 && (counter+1) % 10 == 0) {
                        //schauen wir nun, ob das Element schon ein vielfaches von 10 in der Liste ist
                        // passt die Entfernung noch oder läuft da jemand nur wie eine Schildkröte?
                        if (!isClose(oldLat, oldLong, newLat, newLong)) {
                            //ok er scheint hier wie eine Schildkröte zu laufen.. also hören wir jetzt auf mit der Reihe!
                            log.debug("beim ersten isClose raus" + i + " " +row.toString());
                            log.debug("neuer index: " + i);
                            all.add(row);
                            first = true;
                        } else {
                            //keine Schildkröte - der lebt hier vielleicht. Lass uns noch einen zusätzlichen Wert abprüfen und zuweisen, sicher ist sicher
                            if (middleLat != 0 && !isClose(middleLat, middleLong, newLat, newLong)) {
                                //es existiert ein mittlerer Wert, der aber zu weit weg ist vom aktuellen Standpunkt!
                                log.debug(i + " " + row.toString());
                                all.add(row);
                                first=true;
                            } else{
                                middleLat = Double.parseDouble(row.get(counter/2).getLatitude());
                                middleLong = Double.parseDouble(row.get(counter/2).getLongitude());
                            }
                        }
                    } else {
                        log.debug(i + " " +row.toString());
                        all.add(row);
                        first = true;
                    }
                }
            }
        }
        all.add(row);
        log.debug(all.toString());

        //hier wird die liste all ausgewertet - die Row mit den meisten Einträgen war der längste Aufenthaltsort
        int longestsize = 0;
        List<GPSData> longestList = null;
        for(List<GPSData> part: all){
            if(row.size()>longestsize){
                longestList = part;
            }
        }
        GPSData longest = longestList.get(0);
        GPSData last = longestList.get(longestList.size()-1);

        String ausgabe  =  ("Der user " + username+ " hat sich von " + longest.getTimestamp() +
                " bis " + last.getTimestamp() +
                " am Standort mit den Koordinaten " + longest.getLatitude() +
                "/" + longest.getLongitude() + " aufgehalten.");
        return ausgabe;
    }

    public List<GPSData> findRow(List<GPSData> list){
        log.debug("findrow wird aufgerufen");
        List<GPSData> row = new ArrayList<>();


        int counter = 0;
        GPSData temp = null;
        double oldLat = 0;
        double oldLong = 0;
        double middleLat = 0;
        double middleLong = 0;
        double tempLat = 0;
        double tempLong = 0;
        double newLat;
        double newLong;

        for (int i = index; i < list.size(); i++) {
            GPSData data = list.get(i);
            //erste Frage: ist das erste Element schon in der Reihe?
            if (temp == null) {
                temp = data;
                tempLat = Double.parseDouble(data.getLatitude());
                tempLong = Double.parseDouble(data.getLongitude());
                oldLat = tempLat;
                oldLong = tempLong;
                row.add(data);
            } else {
                //das erste Element ist also schon vorhanden - jetzt prüfen wir die Nähe
                newLat = Double.parseDouble(data.getLatitude());
                newLong = Double.parseDouble(data.getLongitude());

                if (isClose(tempLat, tempLong, newLat, newLong)) {
                    //jetzt sind sie beieinander, die nächsten GeoDaten werden in die Reihe eingefügt
                    row.add(data);
                    counter++;
                    temp = data;

                    if (counter != 0 && counter % 10 == 0) {
                        //schauen wir nun, ob das Element schon ein vielfaches von 10 in der Liste ist
                        // passt die Entfernung noch oder läuft da jemand nur wie eine Schildkröte?
                        if (!isClose(oldLat, oldLong, newLat, newLong)) {
                            //ok er scheint hier wie eine Schildkröte zu laufen.. also hören wir jetzt auf mit der Reihe!
                            log.debug("beim ersten isClose raus" + index + " " +row.toString());
                            index++;
                            log.debug("neuer index: " + index);
                            return row;
                        } else {
                            //keine Schildkröte - der lebt hier vielleicht. Lass uns noch einen zusätzlichen Wert abprüfen und zuweisen, sicher ist sicher
                            if (middleLat != 0 && !isClose(middleLat, middleLong, newLat, newLong)) {
                                //es existiert ein mittlerer Wert, der aber zu weit weg ist vom aktuellen Standpunkt!
                                log.debug(index + " " + row.toString());
                                index++;
                                return row;
                            } else {
                                middleLat = newLat;
                                middleLong = newLong;
                            }
                        }
                    } else {
                        log.debug(index + " " +row.toString());
                        index++;
                        return row;
                    }
                }
            }
        }
        log.debug(index + " " + row.toString());
        return row;
    }

    public boolean isVeryClose(double lat1, double long1, double lat2, double long2){
        double dx = 71.5 * (long1 - long2);
        double dy = 111.3 * (lat1 - lat2);
        double distance = Math.sqrt((dx * dx) + (dy * dy));

        return distance<=0.030;
    }

    public boolean isClose(double lat1, double long1, double lat2, double long2){
        double dx = 71.5 * (long1 - long2);
        double dy = 111.3 * (lat1 - lat2);
        double distance = Math.sqrt((dx * dx) + (dy * dy));

        return distance<=0.030;
    }

}
