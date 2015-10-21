package de.dhbw.meetme.logic;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.GPSClassicDao;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.domain.UuidId;
import de.dhbw.meetme.rest.GPSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Created by vrettos on 21.10.2015.
 */
public class GPSLogic {
    private static final Logger log = LoggerFactory.getLogger(GPSService.class);

    @Inject
    UserClassicDao userClassicDao;
    @Inject
    GPSClassicDao GPSClassicDao;
    @Inject
    Transaction transaction;

    public String testExistence(String userId){
        //enthält id der olddata bei Vorhandensein, bei nichtvorhandensein einen Leeren String
        String result;

        transaction.begin();
        GPSData olddata = GPSClassicDao.getGPSbyUserId(userId);
        if (olddata != null){
            result = olddata.getId().asString();
        }else{
            result = "";
        }
        return result;
    }

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
            GPSClassicDao.persist(data);
        }else{
            GPSClassicDao.updateGPS(data, oldId);
        }

        transaction.commit();
    }

    public String listGPSData(){
        //gibt im Vergleich zu colGPSData einen String aus, der aber richtig modelliert sein sollte
        StringBuffer sb = new StringBuffer("{\"gPSData\":[");
        Collection<GPSData> list = GPSClassicDao.list();
        for (GPSData data: list){
            sb.append("{\"latitude\":");
            sb.append(data.getLatitude());
            sb.append(",\"longitude\":");
            sb.append(data.getLongitude());
            sb.append(",\"username\":\"");
            sb.append(data.getUsername());
            sb.append("\"},");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");

        return sb.toString();

        //return GPSClassicDao.list();
    }

    public Collection<GPSData> colGPSData(){
        //gibt die Collection wie ursprünglich zurück, bei Bestehen des Fehlers im JSon Parser wieder zurück!
        return GPSClassicDao.list();
    }
}
