package de.dhbw.meetme.database.dao;

import de.dhbw.meetme.domain.User;
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

/**
 * The same class the User Dao, but implemented in the traditional way.
 * You can use either this or the the other.
 * Decide yourself!
 */
public class UserClassicDao implements Dao<UuidId, User> {
    @PersistenceContext
    protected EntityManager entityManager;

    private Connection getConnection() {
        return entityManager.unwrap(Connection.class);
        //davor: return (entityManager.unwrap(SessionImpl.class)).connection();
    }

    @Override
    public void persist(User entity) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement("insert into user (id, name, firstname, lastname, email, password, team) values (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, entity.getId().asString());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getFirstname());
            statement.setString(4, entity.getLastname());
            statement.setString(5, entity.getEmail());
            statement.setString(6, entity.getPassword());
            statement.setString(7, entity.getTeam());
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
            statement = con.prepareStatement("delete from user where id = ?");
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
    public User get(UuidId id) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        User user = null;
        try {
            statement = con.prepareStatement("select id, name, firstname, lastname, email, password, team from user where id = ?");
            statement.setString(1, id.asString());
            result = statement.executeQuery();
            if (!result.next())
                return null;
            if (result.getFetchSize() > 1) {
                throw new RuntimeException("Id not unique!");
            }
            user = new User();
            user.setId(id);
            user.setName(result.getString(2));
            user.setFirstname(result.getString(3));
            user.setLastname(result.getString(4));
            user.setEmail(result.getString(5));
            user.setPassword(result.getString(6));
            user.setTeam(result.getString(7));

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
        return user;
    }

    @Override
    public Collection<User> list() {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<User> users = new ArrayList<>();
        try {
            statement = con.prepareStatement("select id, name, firstname, lastname, email, password, team from user");
            result = statement.executeQuery();

            while(result.next()) {
                User user = new User();
                user.setId(UuidId.fromString(result.getString(1)));
                user.setName(result.getString(2));
                user.setFirstname(result.getString(3));
                user.setLastname(result.getString(4));
                user.setEmail(result.getString(5));
                user.setPassword(result.getString(6));
                user.setTeam(result.getString(7));
                users.add(user);
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
        return users;
    }

    //eher suboptimal - gibt aktuell nicht alle Attribute des Users zurück
    public Collection<User> findByName(String name) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<User> users = new ArrayList<>();
        try {
            statement = con.prepareStatement("select id, name from user where name = ?");
            //hier fehlen noch die anderen Variablen
            statement.setString(1, name);
            result = statement.executeQuery();

            while(result.next()) {
                User user = new User();
                user.setId(UuidId.fromString(result.getString(1)));
                user.setName(result.getString(2));
                users.add(user);
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
        return users;
    }

    //ungetestet - macht aus username die UuId
    public UuidId idFromName(String username) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        UuidId id = null;
        try {
            statement = con.prepareStatement("select id from user where name = ?");
            statement.setString(1, username);
            result = statement.executeQuery();
            if (!result.next())
                return null;
            if (result.getFetchSize() > 1) {
                throw new RuntimeException("Name not unique!");
            }
            id = UuidId.fromString(result.getString(1));

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
        return id;
    }


}
