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
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
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

    //WORK STILL NEEDS TO BE DONE to change Communication with Website from UserServlet to UserService
    /*@Path("/adduser")
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

    public void adduser(@FormParam("name") String firstname, @FormParam("lastname") String lastname, @FormParam("username") String username, @FormParam("e-mail") String email, @FormParam("password") String password, @FormParam("teams") String team){
    transaction.begin();
    Collection<User> users = userClassicDao.list();

    //todo Paul: überprüfen ob Username vergeben
    User user = new User();
    user.setName(username);
    user.setFirstname(firstname);
    user.setLastname(lastname);
    user.setEmail(email);
    user.setPassword(password);
    user.setTeam(team);

    userClassicDao.persist(user);
    transaction.commit();

    users = new ArrayList<>(users); // cloning the read-only list so that we can add something
    users.add(user);
  }
*/

}
