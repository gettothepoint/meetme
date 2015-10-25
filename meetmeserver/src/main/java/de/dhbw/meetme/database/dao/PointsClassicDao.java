package de.dhbw.meetme.database.dao;

import de.dhbw.meetme.domain.UuidId;
import de.dhbw.meetme.domain.Points;
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

/**
 * Käthe, 11.10.15
 * bitte folgende nicht verwenden: persist, persistTeams, updateTeamPoints - der Aufruf findet hier intern statt!
 */
public class PointsClassicDao implements Dao<UuidId, Points>{
    @PersistenceContext
    protected EntityManager entityManager;
    private static final Logger log = LoggerFactory.getLogger(PointsClassicDao.class);


    private Connection getConnection() {
        return entityManager.unwrap(Connection.class);
        //davor: return (entityManager.unwrap(SessionImpl.class)).connection();
    }

    @Override
    public void persist(Points entity) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement("insert into points (id, team, username, userId, team2, username2, userId2, point) values (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, entity.getId().asString());
            statement.setString(2, entity.getTeam());
            statement.setString(3, entity.getUsername());
            statement.setString(4, entity.getUserId());
            statement.setString(5, entity.getTeam2());
            statement.setString(6, entity.getUsername2());
            statement.setString(7, entity.getUserId2());
            statement.setString(8, entity.getPointS());
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
    public void delete(UuidId id) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement("delete from points where id = ?");
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
    public Points get(UuidId id) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        Points points = null;
        try {
            statement = con.prepareStatement("select id, team, username, userId, team2, username2, userId2, point from points where id = ?");
            statement.setString(1, id.asString());
            result = statement.executeQuery();
            if (!result.next())
                return null;
            if (result.getFetchSize() > 1) {
                throw new RuntimeException("Id not unique!");
            }
            points = new Points();
            points.setId(id);
            points.setTeam(result.getString(2));
            points.setUsername(result.getString(3));
            points.setUserId(result.getString(4));
            points.setTeam2(result.getString(5));
            points.setUsername2(result.getString(6));
            points.setUserId2(result.getString(7));
            points.setPointS(result.getString(8));

            result.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could extract data", e);
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
        return points;
    }

    @Override
    public Collection<Points> list() {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<Points> pointlist = new ArrayList<>();
        try {
            statement = con.prepareStatement("select id, team, username, userId, team2, username2, userId2, point from points");
            result = statement.executeQuery();

            while(result.next()) {
                Points points = new Points();
                points.setId(UuidId.fromString(result.getString(1)));
                points.setTeam(result.getString(2));
                points.setUsername(result.getString(3));
                points.setUserId(result.getString(4));
                points.setTeam2(result.getString(5));
                points.setUsername2(result.getString(6));
                points.setUserId2(result.getString(7));
                points.setPointS(result.getString(8));
                pointlist.add(points);
            }
            result.close();
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException("Could not update database", e);
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
        return pointlist;
    }

    public Collection<Points> getPointsByUserId(String userId){
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<Points> pointslist = new ArrayList<>();
        try {
            statement = con.prepareStatement("select id, team, username, userId, team2, username2, userId2, point from points where userId = ?");
            statement.setString(1, userId);
            result = statement.executeQuery();
           /* if(!result.next())
                return null; */

            while(result.next()) {
                Points points = new Points();
                points.setId(UuidId.fromString(result.getString(1)));
                points.setTeam(result.getString(2));
                points.setUsername(result.getString(3));
                points.setUserId(result.getString(4));
                points.setTeam2(result.getString(5));
                points.setUsername2(result.getString(6));
                points.setUserId2(result.getString(7));
                points.setPointS(result.getString(8));
                pointslist.add(points);
            }
            result.close();
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException("Could not update database", e);
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
        return pointslist;
    }

}
