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

    private static final Logger log = LoggerFactory.getLogger(MeetLogic.class);

    @Inject
    UserClassicDao userClassicDao;
    @Inject
    PointsLogic pointsLogic;
    @Inject
    Transaction transaction;



    //Überprüft, ob Teams gleich sind
    public void checkEqualTeams(String user1, String user2)
    {
        transaction.begin();

        String userId1 = (userClassicDao.idFromName(user1).asString());
        User u1 = userClassicDao.get(UuidId.fromString(userId1));
        String team1 = u1.getTeam();

        String userId2 = (userClassicDao.idFromName(user2).asString());
        User u2 = userClassicDao.get(UuidId.fromString(userId2));
        String team2 = u1.getTeam();

        transaction.commit();

        if(team1.equals(team2))
        {
            //Team ist gleich, beide User bekommen einen Punkt
            updatePoints(user1, 1);
            updatePoints(user2, 1);
        }
        else
        {
            //Team ist ungleich, beide User verlieren einen Punkt
            updatePoints(user1, -1);
            updatePoints(user2, -1);
        }
    }

    public void updatePoints(String user, int points)
    {
        pointsLogic.updatePoints(user, points);
        pointsLogic.updatePointsOverview(user, points);
    }

    public String bestTeam()
    {
        int pointsBlue = pointsLogic.score("teamBlue");
        int pointsRed = pointsLogic.score("teamRed");

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

   /*
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
    */



}