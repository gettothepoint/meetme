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
        return (entityManager.unwrap(SessionImpl.class)).connection();
    }

    @Override
    public void persist(User entity) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement("insert into user (id, name, firstname, lastname, email, password, team, breitengrad, laengengrad) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, entity.getId().asString());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getFirstname());
            statement.setString(4, entity.getLastname());
            statement.setString(5, entity.getEmail());
            statement.setString(6, entity.getPassword());
            statement.setString(7, entity.getTeam());
            statement.setString(8, entity.getBreitengrad());
            statement.setString(9, entity.getLaengengrad());
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
            statement = con.prepareStatement("select id, name from user where id = ?");
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
            statement = con.prepareStatement("select id, name, firstname, lastname, email, password, team, breitengrad, laengengrad from user");
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


    public Collection<User> findByName(String name) {
        Connection con = getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<User> users = new ArrayList<>();
        try {
            statement = con.prepareStatement("select id, name from user where name = ?");
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


}
