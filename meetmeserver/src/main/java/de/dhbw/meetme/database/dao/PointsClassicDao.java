package de.dhbw.meetme.database.dao;

import de.dhbw.meetme.domain.UuidId;
import de.dhbw.meetme.domain.Points;
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

    private Connection getConnection() {
        return entityManager.unwrap(Connection.class);
        //davor: return (entityManager.unwrap(SessionImpl.class)).connection();
    }

    @Override
    public void persist(Points entity) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement("insert into points (id, team, username, userId, point) values (?, ?, ?, ?, ?)");
            statement.setString(1, entity.getId().asString());
            statement.setString(2, entity.getTeam());
            statement.setString(3, entity.getUsername());
            statement.setString(4, entity.getUserId());
            statement.setString(5, entity.getPointS());
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
            statement = con.prepareStatement("select id, team, username, userId, point from points where id = ?");
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
            points.setPointS(result.getString(5));

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
        return points;
    }

    @Override
    public Collection<Points> list() {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<Points> pointlist = new ArrayList<>();
        try {
            statement = con.prepareStatement("select id, team, username, userId, point from points");
            result = statement.executeQuery();

            while(result.next()) {
                Points points = new Points();
                points.setId(UuidId.fromString(result.getString(1)));
                points.setTeam(result.getString(2));
                points.setUsername(result.getString(3));
                points.setUserId(result.getString(4));
                points.setPointS(result.getString(5));
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
            statement = con.prepareStatement("select id, team, username, userId, point from points where userId = ?");
            statement.setString(1, userId);
            result = statement.executeQuery();
            if(!result.next())
                return null;

            while(result.next()) {
                Points points = new Points();
                points.setId(UuidId.fromString(result.getString(1)));
                points.setTeam(result.getString(2));
                points.setUsername(result.getString(3));
                points.setUserId(result.getString(4));
                points.setPointS(result.getString(5));
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

    //zählt in der Methode alle Werte aus der Tabelle zusammen
    public int getPointsAmountByUserId1(String userId){
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        int p = 0;
        try {
            statement = con.prepareStatement("select point from points where userId = ?");
            statement.setString(1, userId);
            result = statement.executeQuery();
            if(!result.next())
                return 0;

            while(result.next()) {
                p += Integer.parseInt(result.getString(1));
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
        return p;
    }

    //zählt mithilfe der SQL Sum funktion zusammen
    public int getPointsAmountByUserId2(String userId){
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        int p = 0;
        try {
            statement = con.prepareStatement("select sum(point) as sum from points where userId = ?");
            statement.setString(1, userId);
            result = statement.executeQuery();

            p = Integer.parseInt(result.getString(1));

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
        return p;
    }

    //updated die Punkte des Users und des Teams
    public void updatePoints(String team, String username, String userId, int p){
        //zuerst werden die Punkte des Users abgespeichert
        Points point = new Points();
        point.setTeam(team);
        point.setUsername(username);
        point.setUserId(userId);
        point.setPoint(p);
        persist(point);

        //danach sollen die Teampoints upgedaten werden - also zuerst erzeugen:
        Points teamPoints = new Points();
        if(team.equals("blue")) {
            teamPoints.setId(UuidId.fromString("bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12"));
            //wird benötigt, da sonst get() nicht aufgerufen werden kann
            teamPoints.setUserId("bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12");
        } else if(team.equals("red")){
            teamPoints.setId(UuidId.fromString("eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12"));
            teamPoints.setUserId("eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12");
        } else {
            throw new RuntimeException("unknown Team!");
        }

        //prüfen, ob es die Teampoints schon gibt und erzeugen, falls nicht
        if(get(teamPoints.getId())== null){
            persistTeams();
        }

        //teampoints updated (UserId wird übergeben
        // weil sie nicht noch zum String gemacht werden muss in updateTeamPoints
        //todo Käthe: Version abprüfen!
        int newpoints = getPointsAmountByUserId1(teamPoints.getUserId()) + p;
        updateTeamPoints(teamPoints.getUserId(), newpoints);

    }

    //führt Teams ein, falls noch nicht vorhanden - wird aus updatePoints aufgerufen
    public void persistTeams(){
        Connection con = getConnection();
        PreparedStatement statement = null;

        try {
            statement = con.prepareStatement("insert into points (id, team, username, userId, point) values (?, ?, ?, ?, ?), (?, ?, ?, ?, ?)");
            statement.setString(1, "bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12");
            statement.setString(2, "blue");
            statement.setString(3, "teamBlue");
            statement.setString(4, "bbbbbbb8-bbb4-bbb4-bbb4-bbbbbbbbbb12");
            statement.setString(5, "0");
            statement.setString(6, "eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12");
            statement.setString(7, "red");
            statement.setString(8, "teamRed");
            statement.setString(9, "eeeeeee8-eee4-eee4-eee4-eeeeeeeeee12");
            statement.setString(10, "0");
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
    //erhöht die Teampoints - wird aus updatePoints aufgerufen
    public void updateTeamPoints(String id, int newpoints){
        Connection con = getConnection();
        PreparedStatement statement = null;
        String p = Integer.toString(newpoints);
        try {
            statement = con.prepareStatement("UPDATE Points SET points = ? WHERE id = ?");
            statement.setString(1, p); //p ist schon der neue Punktwert
            statement.setString(2, id);
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
}
