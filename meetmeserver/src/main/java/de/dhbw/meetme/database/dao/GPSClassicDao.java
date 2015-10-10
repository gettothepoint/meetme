package de.dhbw.meetme.database.dao;

import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.domain.UuidId;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by Käthe on 08.10.2015.
 * testing noch durchzuführen!
 */
public class GPSClassicDao implements Dao<UuidId, GPSData> {
    @PersistenceContext
    protected EntityManager entityManager;

    private Connection getConnection() {
        return entityManager.unwrap(Connection.class);
        //davor: return (entityManager.unwrap(SessionImpl.class)).connection();
        }

    @Override
    public void persist(GPSData entity) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement("insert into GPSData (id, username, userid, latitude, longitude) values (?, ?, ?, ?, ?)");
            statement.setString(1, entity.getId().asString());
            statement.setString(2, entity.getUsername());
            statement.setString(3, entity.getUserId().asString());
            statement.setString(4, entity.getLatitude());
            statement.setString(5, entity.getLongitude());
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
            statement = con.prepareStatement("delete from GPSData where id = ?");
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

    @Override //id ist hier die ID des GPSData Objektes - nicht des Users
    public GPSData get(UuidId id) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        GPSData Data = null;
        try {
            statement = con.prepareStatement("select id, username, userId, latitude, longitude from GPSData where id = ?");
            statement.setString(1, id.asString());
            result = statement.executeQuery();
            if (!result.next())
                return null;
            if (result.getFetchSize() > 1) {
                throw new RuntimeException("Id not unique!");
            }
            Data = new GPSData();
            Data.setId(id);
            Data.setUsername(result.getString(2));
            Data.setUserIdfromString(result.getString(3));     //VORSICHT HIER - UNSICHER OB DAS GEHT
            Data.setLatitude(result.getString(4));
            Data.setLongitude(result.getString(5));

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
        return Data;
    }

    @Override
    public Collection<GPSData> list() {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<GPSData> datalist = new ArrayList<>();
        try {
            statement = con.prepareStatement("select id, username, userId, latitude, longitude from user");
            result = statement.executeQuery();

            while(result.next()) {
                GPSData data = new GPSData();
                data.setId(UuidId.fromString(result.getString(1)));
                data.setUsername(result.getString(2));
                data.setUserIdfromString(result.getString(3));     //VORSICHT HIER - UNSICHER OB DAS GEHT
                data.setLatitude(result.getString(4));
                data.setLongitude(result.getString(5));

                datalist.add(data);
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
        return datalist;
    }

    //später ändern, wenn die UserId häufiger vorkommt -> auf Datum überprüfen
    public GPSData getGPSbyUserId(UuidId id) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        GPSData Data = null;
        try {
            statement = con.prepareStatement("select id, username, userId, latitude, longitude from GPSData where userId = ?");
            statement.setString(1, id.asString());
            result = statement.executeQuery();
            if (!result.next())
                return null;
            if (result.getFetchSize() > 1) {
                throw new RuntimeException("Id not unique!");
            }
            Data = new GPSData();
            Data.setId(id);
            Data.setUsername(result.getString(2));
            Data.setUserIdfromString(result.getString(3));     //VORSICHT HIER - UNSICHER OB DAS GEHT
            Data.setLatitude(result.getString(4));
            Data.setLongitude(result.getString(5));

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
        return Data;
    }


    public void updateGPS(String username, UuidId userId, String latitude, String longitude) {

        GPSData data = new GPSData();
        data.setUsername(username);
        data.setUserId(userId);
        data.setLatitude(latitude);
        data.setLongitude(longitude);

        GPSData olddata = getGPSbyUserId(userId);

        if(olddata == null){
            persist(data);  //sollte der betreffende User noch keine Daten haben
        } else {
            String id = olddata.getId().asString();

            Connection con = getConnection();
            PreparedStatement statement = null;
            try {
                statement = con.prepareStatement("UPDATE GPSData SET latitude = ?, longitude = ? WHERE id = ?");
                statement.setString(1, latitude);
                statement.setString(2, longitude);
                statement.setString(3, id);
                //hier wird die ID der GeoData nicht überschrieben sondern bleibt die alte - später mit Datum noch abändern
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

}
