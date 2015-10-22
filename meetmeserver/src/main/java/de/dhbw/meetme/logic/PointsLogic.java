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

    public Collection<PointsOverview> listPointsOverview(){
        return poDao.list();
    }

    public Collection<PointsOverview> bestOfTeams(String team){
        return poDao.listbyTeam(team);
        //todo ändern, sodass richtige Reihenfolge ausgegeben wird
    }

    public int score(String username){
        transaction.begin();
        int result;
        if(username.equals("teamRed")){
            result = poDao.getPointsByUserId("eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12");
        }else if (username.equals("teamBlue")){
            result = poDao.getPointsByUserId("bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12");
        }else {
            String id = (userClassicDao.idFromName(username)).asString();
            result = poDao.getPointsByUserId(id);
        }
        transaction.commit();

        return result;
    }

    public void updatePoints(String user, int points){
        //updated nur die Tabelle Points
        transaction.begin();

        Points entity = new Points();
        String userId = (userClassicDao.idFromName(user)).asString();
        User u = userClassicDao.get(UuidId.fromString(userId));

        entity.setUserId(userId);
        entity.setUsername(user);
        entity.setTeam(u.getTeam());
        entity.setPoint(points);

        pointsClassicDao.persist(entity);
        transaction.commit();

    }

    public void updatePointsOverview(String user, int points){
        transaction.begin();
        PointsOverview po = new PointsOverview();
        String userId = (userClassicDao.idFromName(user)).asString();
        User u = userClassicDao.get(UuidId.fromString(userId));

        po.setUserId(userId);
        po.setUsername(user);
        po.setTeam(u.getTeam());
        po.setPoint(points);
        po.setVersion(0);


        //speichern oder updaten des Users in PointsOverview
        if(!testExistence(userId)){
            poDao.persist(po);
        }else{
            int version = poDao.getVersionByUserId(userId);
            int userPoints = points + poDao.getPointsByUserId(userId);
            poDao.updatePointsOverview(userId, userPoints, version);
        }

        //updaten des Teams in PointsOverview
        if(po.getTeam().equals("red")){
            String redId = "eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12";
            int version = poDao.getVersionByUserId(redId);
            int teamPoints = points + poDao.getPointsByUserId(redId);
            poDao.updatePointsOverview(redId, teamPoints, version);
        }else{
            String blueId = "bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12";
            int version = poDao.getVersionByUserId(blueId);
            int teamPoints = points + poDao.getPointsByUserId(blueId);
            poDao.updatePointsOverview(blueId, teamPoints, version);
        }
        transaction.commit();
    }

    public boolean testExistence(String userId){
        //true, wenn ein User besteht
        return poDao.get(UuidId.fromString(userId))!= null;
    }

    public void persistTeams(){
        PointsOverview pored = new PointsOverview();
        pored.setUserId("eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12");
        pored.setUsername("teamRed");
        pored.setPoint(0);
        pored.setVersion(0);

        PointsOverview poblue = new PointsOverview();
        pored.setUserId("bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12");
        pored.setUsername("teamBlue");
        pored.setPoint(0);
        pored.setVersion(0);

        transaction.begin();
        poDao.persist(pored);
        poDao.persist(poblue);
        transaction.commit();
    }
}
