package de.dhbw.meetme.logic;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.PointsClassicDao;
import de.dhbw.meetme.database.dao.PointsOverviewDao;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.domain.Points;
import de.dhbw.meetme.domain.PointsOverview;
import de.dhbw.meetme.domain.User;
import de.dhbw.meetme.domain.UuidId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Verwaltet die Aufrufe von PointsOverviewDao
 */
public class PointsLogic {

    private static final Logger log = LoggerFactory.getLogger(PointsLogic.class);

    @Inject
    PointsOverviewDao poDao;
    @Inject
    PointsClassicDao pointsClassicDao;
    @Inject
    UserClassicDao userClassicDao;
    @Inject
    Transaction transaction;

    /** Functions für Paul */
    //gibt das beste Team aus: red Team, blue Team und tie
    public String bestTeam() {
        int pointsBlue = score("teamBlue");
        int pointsRed = score("teamRed");

        if(pointsBlue>pointsRed)
        {
            return "blue Team";
        }
        else if(pointsRed>pointsBlue)
        {
            return "red Team";
        }
        else
        {
            return "tie";
        }
    }

    //als String User + Score absteigend nur von Team
    public String bestOfTeams(String team){
        //zugelassen: red und blue
        log.debug("best of Team " + team + " wird ausgegeben:");

        StringBuilder sb = new StringBuilder("{\"pointsOverview\":[");
        Collection<PointsOverview> list = poDao.list();
        for (PointsOverview data: list){
                if(data.getTeam().equals(team)) {
                sb.append("{\"username\":");
                sb.append(data.getUsername());
                sb.append(",\"points\":");
                sb.append(data.getPointS());
                sb.append("\"},");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");

        log.debug("String erstellt: " + sb);

        return sb.toString();
    }

    //als String alle User + Scores absteigend
    public String bestOf(){

        StringBuilder sb = new StringBuilder("{\"pointsOverview\":[");
        Collection<PointsOverview> list = poDao.list();
        for (PointsOverview data: list){
            if(!data.getTeam().equals("team")) {
                sb.append("{\"username\":\"");
                sb.append(data.getUsername());
                sb.append("\",\"points\":\"");
                sb.append(data.getPointS());
                sb.append("\"},");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");

        log.debug("String erstellt: " + sb);

        return sb.toString();
    }

    //gibt Score des Users zurück
    public int score(String username){
        transaction.begin();
        int result;
        if(username.equals("teamRed")){
            log.debug("rotes Team wird ausgegeben");
            result = poDao.getPointsByUserId("eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12");
        }else if (username.equals("teamBlue")){
            log.debug("blaues Team wird ausgegeben");
            result = poDao.getPointsByUserId("bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12");
        }else {
            String id = (userClassicDao.idFromName(username)).asString();
            result = poDao.getPointsByUserId(id);
            log.debug("user " + username + " wird ausgegeben");
        }
        transaction.commit();

        return result;
    }



    /** weniger wichtig */
    public Collection<PointsOverview> listPointsOverview(){
        return poDao.list();
    }



    /**NONO-FUNCTIONS */
    public void updatePoints(String user, String user2, int points){

        transaction.begin();

        Points entity = new Points();
        String userId = (userClassicDao.idFromName(user)).asString();
        User u = userClassicDao.get(UuidId.fromString(userId));
        String userId2 = (userClassicDao.idFromName(user2)).asString();
        User u2 = userClassicDao.get(UuidId.fromString(userId2));


        entity.setUserId(userId);
        entity.setUsername(user);
        entity.setTeam(u.getTeam());
        entity.setUsername2(user2);
        entity.setUserId2(userId2);
        entity.setTeam2(u2.getTeam());
        entity.setPoint(points);

        pointsClassicDao.persist(entity);
        log.debug("erstellt: " + entity);
        transaction.commit();

    }

    public void createPointsOverview(String user, String userId, String team){
        PointsOverview po = new PointsOverview();
        po.setUserId(userId);
        po.setUsername(user);
        po.setTeam(team);
        po.setPoint(0);
        po.setVersion(0);

        transaction.begin();
        poDao.persist(po);
        transaction.commit();
    }

    public void updatePointsOverview(String user, int points){
        transaction.begin();

        String userId = (userClassicDao.idFromName(user)).asString();
        User u = userClassicDao.get(UuidId.fromString(userId));

        String team = u.getTeam();
        int userversion = poDao.getVersionByUserId(userId);
        poDao.updatePointsOverview(userId, points, userversion);


        if(team.equals("red")){
            String redId = "eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12";
            int teamversion = poDao.getVersionByUserId(redId);
            poDao.updatePointsOverview(redId, points, teamversion);
        }else{
            String blueId = "bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12";
            int teamversion = poDao.getVersionByUserId(blueId);
            poDao.updatePointsOverview(blueId, points, teamversion);
        }
        transaction.commit();
        log.debug("update abgeschlossen!");
    }

    public boolean testExistence(String userId){
        //true, wenn ein User besteht
        boolean b = poDao.getVersionByUserId(userId)!= -1;
        log.debug(userId + " bestehen: " + b);

        return b;
    }

    public void persistTeams(){
        PointsOverview pored = new PointsOverview();
        pored.setUserId("eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12");
        pored.setUsername("teamRed");
        pored.setTeam("team");
        pored.setPoint(0);
        pored.setVersion(0);
        log.debug("redTeam erstellt");

        PointsOverview poblue = new PointsOverview();
        poblue.setUserId("bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12");
        poblue.setUsername("teamBlue");
        poblue.setTeam("team");
        poblue.setPoint(0);
        poblue.setVersion(0);
        log.debug("blueTeam erstellt");

       // transaction.begin();
        poDao.persist(pored);
        log.debug("redTeam persisted");
        poDao.persist(poblue);
        log.debug("blueTeam persisted");
       // transaction.commit();
    }
}
