package de.dhbw.meetme.logic;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.GPSClassicDao;
import de.dhbw.meetme.database.dao.PointsClassicDao;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.domain.Points;
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

    public void updatePoints(String user)
    {
            transaction.begin();

            String userId = (userClassicDao.idFromName(user)).asString();
            User u = userClassicDao.get(UuidId.fromString(userId));
            String team = u.getTeam();
            pointsClassicDao.updatePoints(team, user, userId, 1);

            transaction.commit();

    }
    //Überprüft, ob Teams gleich sind
    public void checkEqualTeams(String user1, String user2)
    {
        transaction.begin();

        String userId1 = (userClassicDao.idFromName(user1).asString());
        User u1 = userClassicDao.get(UuidId.fromString(userId1));
        String team1 = u1.getTeam();

        String userId2 = (userClassicDao.idFromName(user1).asString());
        User u2 = userClassicDao.get(UuidId.fromString(userId2));
        String team2 = u1.getTeam();

        if(team1==team2)
        {
            //Team ist gleich, beide User bekommen einen Punkt
            updatePoints(user1);
            updatePoints(user2);
        }
        else
        {
            //Team ist ungleich, beide User verlieren einen Punkt
            pointsClassicDao.updatePoints(team1, user1, userId1, -1);
            pointsClassicDao.updatePoints(team2, user2, userId2, -1);
        }
        transaction.commit();
    }

    //Gibt Punkte des Benutzers aus
    public int getPoints(String username){
        String id = userClassicDao.idFromName(username).asString();
        return pointsClassicDao.getPointsAmountByUserId1(id);
    }

    //Gibt Punkte der Teams aus
    public int getTeamPoints(String team){
        if(team.equals("red")){
            Points p = pointsClassicDao.get(UuidId.fromString("eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12"));
            return p.getPoint();
        }else{
            Points p = pointsClassicDao.get(UuidId.fromString("bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12"));
            return p.getPoint();
        }
    }

    public String bestTeams()
    {
        transaction.begin();

        Points p = pointsClassicDao.get(UuidId.fromString("eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12"));
        int pointsRed = p.getPoint();

        Points p2 = pointsClassicDao.get(UuidId.fromString("bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12"));
        int pointsBlue = p2.getPoint();

        transaction.commit();

        if(pointsBlue>pointsRed)
        {
            return "blueTeam";
        }
        else if(pointsRed>pointsBlue)
        {
            return "redTeam";
        }
        else
        {
            return "tie";
        }
    }

}