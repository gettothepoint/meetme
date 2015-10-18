package de.dhbw.meetme.rest;

import de.dhbw.meetme.domain.User;
import de.dhbw.meetme.logic.BasicLogic;
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
  BasicLogic basicLogic;

  @Path("/list")
  @GET
  public Collection<User> list() {
    log.debug("List users");
    return basicLogic.listUsers();
  }

  @Path("/get/{id}")
  @GET
  public User get(@PathParam("id") String id) {
    log.debug("Get user " + id);
    return basicLogic.getUser(id);
  }

  @Path("/delete/{id}")
  @DELETE
  public void delete(@PathParam("id") String id) {
    log.debug("Delete user " + id);
    basicLogic.deleteUser(id);
  }

  @Path("/save")
  @PUT
  public void save(@PathParam("user") User user) {
    String firstname = user.getFirstname();
    String lastname = user.getLastname();
    String username = user.getName();
    String email = user.getEmail();
    String password = user.getPassword();
    String team = user.getTeam();
    basicLogic.addUser(firstname, lastname, username, email, password, team);
    log.debug("Save user " + user);
  }

    /*@Path("/adduser")
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

    public void adduser(@FormParam("name") String firstname, @FormParam("lastname") String lastname, @FormParam("username") String username, @FormParam("e-mail") String email, @FormParam("password") String password, @FormParam("teams") String team){
    transaction.begin();
    Collection<User> users = userClassicDao.list();

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
// curl -i -H "Accept: application/json" -X POST http://127.0.0.1:8087/meetmeserver/api/user/newuser/
