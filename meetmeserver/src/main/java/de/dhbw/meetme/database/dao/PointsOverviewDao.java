package de.dhbw.meetme.database.dao;

import de.dhbw.meetme.domain.Points;
import de.dhbw.meetme.domain.PointsOverview;
import de.dhbw.meetme.domain.UuidId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by vrettos on 21.10.2015.
 */
public class PointsOverviewDao implements Dao<UuidId, PointsOverview> {
    @PersistenceContext
    protected EntityManager entityManager;
    private static final Logger log = LoggerFactory.getLogger(PointsOverviewDao.class);

    private Connection getConnection() {
        return entityManager.unwrap(Connection.class);
        //davor: return (entityManager.unwrap(SessionImpl.class)).connection();
    }


    @Override
    public void persist(PointsOverview entity) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement("insert into pointsoverview (id, team, username, userId, point, version) values (?, ?, ?, ?, ?, ?)");
            statement.setString(1, entity.getId().asString());
            statement.setString(2, entity.getTeam());
            statement.setString(3, entity.getUsername());
            statement.setString(4, entity.getUserId());
            statement.setString(5, entity.getPointS());
            statement.setString(6, entity.getVersionS());
            statement.executeUpdate();
            statement.close();
            log.debug("inserted PointsOverview for " + entity.getUsername());
        } catch (SQLException e) {
            throw new RuntimeException("Could not update database", e);
        } finally {
            try {
                if (statement != null && !statement.isClosed())
                    statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    @Override
    public void delete(UuidId id) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement("delete from pointsoverview where id = ?");
            statement.setString(1, id.asString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update database", e);
        } finally {
            try {
                if (statement != null && !statement.isClosed())
                    statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    @Override
    public PointsOverview get(UuidId id) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        PointsOverview po = null;
        try {
            statement = con.prepareStatement("select id, team, username, userId, point, version from pointsoverview where id = ?");
            statement.setString(1, id.asString());
            result = statement.executeQuery();
            if (!result.next())
                return null;
            if (result.getFetchSize() > 1) {
                throw new RuntimeException("Id not unique!");
            }
            po = new PointsOverview();
            po.setId(id);
            po.setTeam(result.getString(2));
            po.setUsername(result.getString(3));
            po.setUserId(result.getString(4));
            po.setPointS(result.getString(5));
            po.setVersionS(result.getString(6));

            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could not extract data", e);
        } finally {
            try {
                if (result != null && !result.isClosed())
                    result.close();
            } catch (SQLException e) {
                // ignore
            }
            try {
                if (statement != null && !statement.isClosed())
                    statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
        return po;
    }

    @Override
    public Collection<PointsOverview> list() {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<PointsOverview> poList = new ArrayList<>();
        try {
            statement = con.prepareStatement("SELECT id, team, username, userId, point, version FROM pointsoverview ORDER BY point DESC");
            result = statement.executeQuery();

            while(result.next()) {
                PointsOverview po = new PointsOverview();
                po.setId(UuidId.fromString(result.getString(1)));
                po.setTeam(result.getString(2));
                po.setUsername(result.getString(3));
                po.setUserId(result.getString(4));
                po.setPointS(result.getString(5));
                po.setVersionS(result.getString(6));
                poList.add(po);
            }
            result.close();
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException("Could not extract data", e);
        } finally {
            try {
                if (result != null && !result.isClosed())
                    result.close();
            } catch (SQLException e) {
                // ignore
            }
            try {
                if (statement != null && !statement.isClosed())
                    statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
        return poList;
    }

    /*
    public Collection<PointsOverview> listbyTeam(String team){
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<PointsOverview> poList = new ArrayList<>();
        try {
            statement = con.prepareStatement("SELECT id, team, username, userId, point, version FROM pointsoverview WHERE team = ? ORDER BY point DESC");
            statement.setString(1, team);
            result = statement.executeQuery();

            while(result.next()) {
                PointsOverview po = new PointsOverview();
                po.setId(UuidId.fromString(result.getString(1)));
                po.setTeam(result.getString(2));
                po.setUsername(result.getString(3));
                po.setUserId(result.getString(4));
                po.setPointS(result.getString(5));
                po.setVersionS(result.getString(6));
                poList.add(po);
            }
            result.close();
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException("Could not extract data", e);
        } finally {
            try {
                if (result != null && !result.isClosed())
                    result.close();
            } catch (SQLException e) {
                // ignore
            }
            try {
                if (statement != null && !statement.isClosed())
                    statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
        return poList;
    }

*/
    public int getPointsByUserId(String userId){
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        int p = 0;
        try {
            statement = con.prepareStatement("select point from pointsoverview where userid = ?");
            statement.setString(1, userId);
            result = statement.executeQuery();
            if (!result.next()){
                return 0;
            }
            else {
                if (result.getFetchSize() > 1) {
                    throw new RuntimeException("UserId not unique!");
                } else {
                    p = Integer.parseInt(result.getString(1));
                }
            }

            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could not extract data", e);
        } finally {
            try {
                if (result != null && !result.isClosed())
                    result.close();
            } catch (SQLException e) {
                // ignore
            }
            try {
                if (statement != null && !statement.isClosed())
                    statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
        return p;
    }

    public int getVersionByUserId(String userId){
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        int v = 0;
        try {
            statement = con.prepareStatement("SELECT version from pointsoverview where userid = ?");
            statement.setString(1, userId);
            result = statement.executeQuery();
            if (!result.next()){
                return -1;
            }
            else {
                if (result.getFetchSize() > 1) {
                    throw new RuntimeException("UserId not unique!");
                } else {
                    v = Integer.parseInt(result.getString(1));
                }
            }

            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could not extract data", e);
        } finally {
            try {
                if (result != null && !result.isClosed())
                    result.close();
            } catch (SQLException e) {
                // ignore
            }
            try {
                if (statement != null && !statement.isClosed())
                    statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
        return v;
    }

    public void updatePointsOverview(String userId, int newpoints, int version){
        //newpoints wird upgedated, nicht draufgezählt!
        Connection con = getConnection();
        PreparedStatement statement = null;

        try {
            //con.setAutoCommit(false);
            statement = con.prepareStatement("UPDATE pointsoverview SET point = ?, version = ? WHERE userId = ? AND version = ?");
            statement.setString(1, Integer.toString(newpoints)); //p ist schon der neue Punktwert
            statement.setString(2, Integer.toString(version + 1));
            statement.setString(3, userId);
            statement.setString(4, Integer.toString(version));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            /*if(con !=null){
                try {
                    log.debug("Transaction is being rolled back with exception: " + e);
                    con.rollback(); //rollback wegen optimistic locking -> nächster Versuch

                    updatePointsOverview(userId, newpoints, version+1); //version +1, da jetzt eine neuere Version da sein muss
                }catch(SQLException excep){
                    log.debug("rollback failed with exception: "+ excep);
                }
            }*/
            throw new RuntimeException("Could not update database", e);
        } finally {
            try {
                if (statement != null && !statement.isClosed())
                    statement.close();
                con.setAutoCommit(true);
            } catch (SQLException e) {
                // ignore
            }
        }
    }

}
