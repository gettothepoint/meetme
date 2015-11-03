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
import java.util.Collection;

/**
 * Kerstin und Käthe - meet enthält boolean meeting um ein Treffen zu prüfen
 */
public class MeetLogic {

    private static final Logger log = LoggerFactory.getLogger(MeetLogic.class);

    @Inject
    UserClassicDao userClassicDao;
    @Inject
    PointsClassicDao pointsClassicDao;
    @Inject
    PointsLogic pointsLogic;
    @Inject
    Transaction transaction;


    //Entählt Spiellogik
    public void coreLogic(String user1, String user2) {
        transaction.begin();

        String userId1 = (userClassicDao.idFromName(user1).asString());
        User u1 = userClassicDao.get(UuidId.fromString(userId1));
        String team1 = u1.getTeam();

        String userId2 = (userClassicDao.idFromName(user2).asString());
        User u2 = userClassicDao.get(UuidId.fromString(userId2));
        String team2 = u2.getTeam();

        transaction.commit();

        if(team1.equals(team2))
        {
            //Team ist gleich, beide User bekommen einen Punkt
            updatePoints(user1, user2, 3);
            updatePoints(user2, user1, 3);
        }
        else
        {
            //Team ist ungleich, beide User verlieren einen Punkt
            updatePoints(user1, user2, 1);
            updatePoints(user2, user1, 1);
        }
    }

    //Listet alle alle Meetings (also auch doppelte): user1, team1, user2, team2, points
    public String listAllMeetings(){
        StringBuilder sb = new StringBuilder("{\"meetings\":[");
        Collection<Points> list = pointsClassicDao.list();
        for (Points data: list){
            sb.append("{\"user1\":\"");
            sb.append(data.getUsername());
            sb.append("\", \"team1\":\"");
            sb.append(data.getTeam());
            sb.append("\", \"user2\":\"");
            sb.append(data.getUsername2());
            sb.append("\", \"team2\":\"");
            sb.append(data.getTeam2());
            sb.append("\", \"points\":");
            sb.append(data.getPointS());
            sb.append(",\"timestamp\":\"");
            sb.append(data.getTimestamp());
            sb.append("\"},");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");
        log.debug("String erstellt: " + sb);

        return sb.toString();
    }

    //Listet alle Meetings des Users: user2, team2, points
    public String listUserMeetings(String username){
        String userId = (userClassicDao.idFromName(username).asString());
        log.debug("userId found: " + userId);

        StringBuilder sb = new StringBuilder("{\"meetings\":[");
        Collection<Points> list = pointsClassicDao.getPointsByUserId(userId);
        for (Points data: list){
            log.debug("folgendes: " + data);
            sb.append("{\"user2\":\"");
            sb.append(data.getUsername2());
            sb.append("\", \"team2\":\"");
            sb.append(data.getTeam2());
            sb.append("\", \"points\":");
            sb.append(data.getPointS());
            sb.append(", \"timestamp\":\"");
            sb.append(data.getTimestamp());
            sb.append("\"},");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");
        log.debug("String erstellt: " + sb);

        return sb.toString();
    }


    /** NONO FUNCTION */
    public void updatePoints(String user, String user2, int points){
        pointsLogic.updatePoints(user, user2, points);
        pointsLogic.updatePointsOverview(user, points);
    }
}