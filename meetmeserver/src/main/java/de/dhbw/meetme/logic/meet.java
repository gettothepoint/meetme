package de.dhbw.meetme.logic;

import de.dhbw.meetme.database.dao.GPSClassicDao;
import de.dhbw.meetme.domain.UuidId;

/**
 * Created by farke on 12.10.2015.
 */
public class meet {

    public boolean meet(String user1, String user2)
    {
        transaction.begin();

        log.debug("Get User " + username);
        UuidId uID = userClassicDao.idFromName(username);
        String userId = uID.asString();

        log.debug("User found, continue to insert or update");
        GPSClassicDao.updateGPS(username, userId, latitude, longitude);

        transaction.commit();
    }

}
