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

/**
 * Kerstin und Käthe - meet enthält boolean meeting um ein Treffen zu prüfen
 */
public class Meet {

    private static final Logger log = LoggerFactory.getLogger(GPSService.class);

    @Inject
    UserClassicDao userClassicDao;
    @Inject
    GPSClassicDao GPSClassicDao;
    @Inject
    Transaction transaction;

    public boolean meeting(String user1, String user2)
    {
        double spielraum = 0.015;

        transaction.begin();

        log.debug("Get User " + user1);
        UuidId uID1 = userClassicDao.idFromName(user1);
        String userId1 = uID1.asString();
        log.debug("User1 found, continue to insert or update");

        log.debug("Get User " + user2);
        UuidId uID2 = userClassicDao.idFromName(user2);
        String userId2 = uID2.asString();
        log.debug("User2 found, continue to insert or update");

        GPSData data1 = GPSClassicDao.getGPSbyUserId(userId1);
        GPSData data2 = GPSClassicDao.getGPSbyUserId(userId2);

        transaction.commit();

        double lat1 = Double.parseDouble(data1.getLatitude());
        double long1 = Double.parseDouble(data1.getLongitude());
        double lat2 = Double.parseDouble(data2.getLatitude());
        double long2 = Double.parseDouble(data2.getLongitude());

        double dx = 71.5 * (long1-long2);
        double dy = 111.3 * (lat1-lat2);
        double distance = Math.sqrt((dx*dx)+(dy*dy));


        return distance <= spielraum;
    }

}