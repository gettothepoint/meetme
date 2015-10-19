package de.dhbw.meetme.logic;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.GPSClassicDao;
import de.dhbw.meetme.database.dao.PointsClassicDao;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.domain.User;
import de.dhbw.meetme.domain.UuidId;
import de.dhbw.meetme.rest.GPSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Kerstin und Käthe - meet enthält boolean meeting um ein Treffen zu prüfen
 */
public class MeetLogic {

    private static final Logger log = LoggerFactory.getLogger(GPSService.class);

    @Inject
    UserClassicDao userClassicDao;
    @Inject
    GPSClassicDao GPSClassicDao;
    @Inject
    PointsClassicDao pointsClassicDao;
    @Inject
    Transaction transaction;


    public boolean checkMeeting(String user1, String user2)
    {
        double spielraum = 0.015;

        transaction.begin();
        String userId1 = (userClassicDao.idFromName(user1)).asString();

        String userId2 = (userClassicDao.idFromName(user2)).asString();

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

        log.debug("distance: " + distance);
        return distance <= spielraum;
    }

    public void updatePoints(String user1, String user2)
    {
        if(checkMeeting(user1, user2))
        {
            transaction.begin();

            String userId1 = (userClassicDao.idFromName(user1)).asString();
            String userId2 = (userClassicDao.idFromName(user2)).asString();

            User u1 = userClassicDao.get(UuidId.fromString(userId1));
            User u2 = userClassicDao.get(UuidId.fromString(userId2));

            String team1 = u1.getTeam();
            String team2 = u2.getTeam();

            pointsClassicDao.updatePoints(team1, user1, userId1, 1);
            pointsClassicDao.updatePoints(team2, user2, userId2, 1);

            transaction.commit();
        }
    }

}