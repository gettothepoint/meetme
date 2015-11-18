package de.dhbw.meetme.Evaluation;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.logic.GPSLogic;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by vrettos on 18.11.2015.
 */
public class Evaluation {
    @Inject
    GPSLogic geoLogic;
    @Inject
    Transaction transaction;

    public void evaluate(String username){
        List<GPSData> list =  geoLogic.listGeoandTimeStamp(username);
        Collection <Collection<GPSData>> all = new ArrayList<>();

    }

    public List<GPSData> findRow(int number, List<GPSData> list){
        List<GPSData> row = new ArrayList<>();

        int counter = 0;
        GPSData temp = null;
        double oldLat = 0;
        double oldLong = 0;
        double middleLat = 0;
        double middleLong = 0;
        double tempLat = 0;
        double tempLong = 0;
        double newLat = 0;
        double newLong = 0;
        for (int i = number; i < list.size(); i++) {
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
                    row.add(temp);
                    counter++;
                    temp = data;

                    if (counter != 0 && counter % 10 == 0) {
                        //schauen wir nun, ob das Element schon ein vielfaches von 10 in der Liste ist
                        // passt die Entfernung noch oder läuft da jemand nur wie eine Schildkröte?
                        if (!isClose(oldLat, oldLong, newLat, newLong)) {
                            //ok er scheint hier wie eine Schildkröte zu laufen.. also hören wir jetzt auf mit der Reihe!
                            return row;
                        } else {
                            //keine Schildkröte - der lebt hier vielleicht. Lass uns noch einen zusätzlichen Wert abprüfen und zuweisen, sicher ist sicher
                            if (middleLat != 0 && !isClose(middleLat, middleLong, newLat, newLong)) {
                                //es existiert ein mittlerer Wert, der aber zu weit weg ist vom aktuellen Standpunkt!
                                return row;
                            } else {
                                middleLat = newLat;
                                middleLong = newLong;
                            }
                        }
                    } else {
                        return row;
                    }
                }
            }
        }
        return row;
    }

    public boolean isClose(double lat1, double long1, double lat2, double long2){
        double dx = 71.5 * (long1 - long2);
        double dy = 111.3 * (lat1 - lat2);
        double distance = Math.sqrt((dx * dx) + (dy * dy));

        return distance<=0.010;
    }

}
