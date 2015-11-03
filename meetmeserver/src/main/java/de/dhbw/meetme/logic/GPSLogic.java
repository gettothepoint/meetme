package de.dhbw.meetme.logic;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.GPSClassicDao;
import de.dhbw.meetme.database.dao.PointsClassicDao;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.domain.Points;
import de.dhbw.meetme.domain.UuidId;
import de.dhbw.meetme.rest.GPSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Behandelt alle GPSClassicDao zugriffe.
 */
public class GPSLogic {
    private static final Logger log = LoggerFactory.getLogger(GPSService.class);

    @Inject
    UserClassicDao userClassicDao;
    @Inject
    GPSClassicDao GPSClassicDao;
    @Inject
    PointsClassicDao pointsClassicDao;
    @Inject
    Transaction transaction;

    /** wichtige Functions */
    //prüft ob Entfernung <15m
    public boolean checkMeeting(String user1, String user2){
        double spielraum = 0.015;
        boolean b;

        transaction.begin();
        String userId1 = (userClassicDao.idFromName(user1)).asString();
        String userId2 = (userClassicDao.idFromName(user2)).asString();

        GPSData exist1 = GPSClassicDao.getGPSbyUserId(userId1);
        GPSData exist2 = GPSClassicDao.getGPSbyUserId(userId2);
        if(exist1== null || exist2 == null){
            b =  false;
        } else {

            GPSData data1 = GPSClassicDao.getGPSbyUserId(userId1);
            GPSData data2 = GPSClassicDao.getGPSbyUserId(userId2);

            double lat1 = Double.parseDouble(data1.getLatitude());
            double long1 = Double.parseDouble(data1.getLongitude());
            double lat2 = Double.parseDouble(data2.getLatitude());
            double long2 = Double.parseDouble(data2.getLongitude());

            double dx = 71.5 * (long1 - long2);
            double dy = 111.3 * (lat1 - lat2);
            double distance = Math.sqrt((dx * dx) + (dy * dy));

            log.debug("distance: " + distance);
            b= (distance <= spielraum);
        }
        transaction.commit();
        return b;
    }

    //String der GPSDaten aller User
    public String listGeo(){
        //gibt im Vergleich zu colGPSData einen String aus, der aber richtig modelliert sein sollte
        StringBuilder sb = new StringBuilder("{\"gPSData\":[");
        Collection<GPSData> list = GPSClassicDao.list();
        for (GPSData data: list){
            sb.append("{\"latitude\":");
            sb.append(data.getLatitude());
            sb.append(",\"longitude\":");
            sb.append(data.getLongitude());
            sb.append(",\"username\":\"");
            sb.append(data.getUsername());
            sb.append(",\"timestamp\":\"");
            sb.append(data.getTimestamp());
            sb.append("\"},");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");
        log.debug("String erstellt: " + sb);

        return sb.toString();

    }

    //String der GPSDaten aller User mit Colors
    public String listGeoAndColor(String username){
        //gibt im Vergleich zu colGPSData einen String aus, der aber richtig modelliert sein sollte

        StringBuilder sb = new StringBuilder("{\"gPSData\":[");
        Collection<GPSData> geoList = GPSClassicDao.list();

        for (GPSData data: geoList){

            String color = met(username, data.getUsername());

            sb.append("{\"latitude\":");
            sb.append(data.getLatitude());
            sb.append(",\"longitude\":");
            sb.append(data.getLongitude());
            sb.append(",\"username\":\"");
            sb.append(data.getUsername());

            sb.append(",\"color\":\"");
            sb.append(color);

            sb.append("\"},");
        }
        log.debug("String erstellt: " + sb);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");

        return sb.toString();
    }

    //Neue Geo-Daten in die Datenbank
    public void updateGPS(String username, String latitude, String longitude){
        transaction.begin();
        UuidId uID = userClassicDao.idFromName(username);

        String userId = uID.asString();
        GPSData data = new GPSData();
        data.setUsername(username);
        data.setUserId(userId);
        data.setLatitude(latitude);
        data.setLongitude(longitude);

        String oldId = testExistence(userId);

        if(oldId.equals("")){
            log.debug("neue GPSDaten, die erst eingetragen werden müssen");
            GPSClassicDao.persist(data);
        }else{
            log.debug("alte GPSDaten, die upgedated werden");
            GPSClassicDao.updateGPS(data, oldId);
        }

        transaction.commit();
    }


    /** NONO FUNCTION */
    public String testExistence(String userId){
        //enthält id der olddata bei Vorhandensein, bei nichtvorhandensein einen Leeren String
        String result;

        GPSData olddata = GPSClassicDao.getGPSbyUserId(userId);

        log.debug("alte Daten: "+ olddata);
        if (olddata != null){
            result = olddata.getId().asString();
            log.debug("gefunden mit id: " + result);
        }else{
            result = "";
            log.debug("nicht gefunden");
        }
        return result;
    }

    public String met(String user1, String user2){
        String userId = (userClassicDao.idFromName(user1).asString());
        Collection<Points> meetingList = pointsClassicDao.getPointsByUserId(userId);

        if(user1.equals(user2)){
            return "green";
        }

        for(Points meeting: meetingList){
            if(user2.equals(meeting.getUsername2())) {
                return meeting.getTeam2();
            }
        }

        return "";

    }


}
