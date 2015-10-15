package de.dhbw.meetme.rest;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.database.dao.UserDao;
import de.dhbw.meetme.domain.User;
import de.dhbw.meetme.domain.UuidId;
import groovy.lang.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Collection;

/**
 *
 */
@Path("/api/user")
@Produces({"application/json"}) // mime type
@Singleton
public class UserService {
  private static final Logger log = LoggerFactory.getLogger(UserService.class);

  //Mutter aller Services muss beide Daos bereitstellen

  @Inject
  UserDao userDao;
  @Inject
  UserClassicDao userClassicDao;
  @Inject
  Transaction transaction;

  @Path("/list")
  @GET
  public Collection<User> list() {
    log.debug("List users");
    return userClassicDao.list();
  }

  @Path("/get/{id}")
  @GET
  public User get(@PathParam("id") String id) {
    log.debug("Get user " + id);
    return userClassicDao.get(UuidId.fromString(id));
  }

  @Path("/delete/{id}")
  @DELETE
  public void delete(@PathParam("id") String id) {
    transaction.begin();
    log.debug("Delete user " + id);
    userDao.delete(UuidId.fromString(id));
    transaction.commit();
  }

  @Path("/save")
  @PUT
  public void save(@PathParam("user") User user) {
    userClassicDao.persist(user);
    log.debug("Save user " + user);
  }
/* test von Käthe
  @Path("newuser/{username}/{firstname}/{lastname}/{email}/{password}/{team}")
  @POST
  public void newuser(@PathParam("username") String username, @PathParam("firstname") String firstname, @PathParam("lastname") String lastname,
                      @PathParam("email") String email, @PathParam("password") String password, @PathParam("team") String team){
    User u = new User();
    u.setName(username);
    u.setFirstname(firstname);
    u.setLastname(lastname);
    u.setEmail(email);
    u.setPassword(password);
    u.setTeam(team);

    userClassicDao.persist(u);
    log.debug("new user created and inserted");
  }
  */
}
// curl -i -H "Accept: application/json" -X POST http://127.0.0.1:8087/meetmeserver/api/user/newuser/
