package de.dhbw.meetme.rest;

import de.dhbw.meetme.database.dao.PointsOverviewDao;
import de.dhbw.meetme.domain.PointsOverview;
import de.dhbw.meetme.domain.User;
import de.dhbw.meetme.logic.*;
import groovy.lang.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Collection;

/**
 *
 */
@Path("/api/test")
@Produces({"application/json"}) // mime type
@Singleton
public class TestService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    //Mutter aller Services muss beide Daos bereitstellen

    @Inject
    Verification verification;
    @Inject
    BasicLogic basicLogic;
    @Inject
    GPSLogic geoLogic;
    @Inject
    MeetLogic meetLogic;
    @Inject
    PointsLogic pointsLogic;


    //VERIFICATION
    @Path("/checkpw/{u}/{pw}")
    @GET
    public boolean checkpw(@PathParam("u") String user, @PathParam("pw") String pw ){
        return verification.checkPassword(user, pw);
    }

    @Path("/checkUsername/{u}")
    @GET
    public boolean checkUsername(@PathParam("u") String u){
        return verification.usernameUnique(u);
    }

    @Path("/checkMail/{mail}")
    @GET
    public boolean checkMail(@PathParam("mail") String mail){
        return verification.checkMail(mail);
    }

    @Path("/checkTeam/{team}")
    @GET
    public boolean checkTeam(@PathParam("team") String team){
        return verification.checkTeam(team);
    }



    //BASICLOGIC
    @Path("/listusers")
    @GET
    public Collection<User> list() {
        log.debug("List users");
        return basicLogic.listUsers();
    }

    @Path("/adduser/{name}/{lastname}/{username}/{e-mail}/{password}/{team}")
    @POST
    public void adduser(@PathParam("name") String firstname, @PathParam("lastname") String lastname, @PathParam("username") String username, @PathParam("e-mail") String email, @PathParam("password") String password, @PathParam("team") String team){
        basicLogic.addUser(firstname, lastname, username, email, password, team);
    }

    @Path("/getuser/{id}")
    @GET
    public User getUser(@PathParam("id") String id){
        return basicLogic.getUser(id);
    }


    //GEOLOGIC
    @Path("/listGeoData")
    @GET
    public String listGeoData(){
        return geoLogic.listGeo();
    }

    @Path("/geoAndColor/{username}")
    @GET
    public String geoAndColor(@PathParam("username") String username){
        return geoLogic.listGeoAndColor(username);
    }

    @Path("/addGeoData/{username}/{latitude}/{longitude}")
    @POST
    public void addGeoData(@PathParam("username") String username, @PathParam("latitude") String latitude, @PathParam("longitude") String longitude){
        geoLogic.updateGPS(username, latitude, longitude);
    }

    @Path("/checkMeeting/{u1}/{u2}")
    @GET
    public boolean checkMeeting(@PathParam("u1") String u1, @PathParam("u2") String u2){
        return geoLogic.checkMeeting(u1, u2);
    }

    @Path("/testGPSexistence/{userId}")
    @GET
    public String testGPSexistence(@PathParam("userId") String id){
        return geoLogic.testExistence(id);
    }



    //POINTSLOGIC
    @Path("/listPoints")
    @GET
    public Collection<PointsOverview> listPoints(){
        return pointsLogic.listPointsOverview();
    }

    @Path("/listPointsTeam/{team}")
    @GET
    public String listPoints(@PathParam("team") String team){
        return pointsLogic.bestOfTeams(team);
    }

    @Path("getScore/{username}")
    @GET
    public int getScore(@PathParam("username") String user){
        return pointsLogic.score(user);
    }

    @Path("testPOexistence/{userId}")
    @GET
    public boolean testExistence(@PathParam("userId") String userId){
        return pointsLogic.testExistence(userId);
    }

    @Path("/maketeams")
    @POST
    public void makeTeam(){
        pointsLogic.persistTeams();
    }

    @Path("/updatePoints/{user}/{user2}{points}")
    @POST
    public void updatePoints(@PathParam("user") String user, @PathParam("user2") String user2, @PathParam("points") int points){
        pointsLogic.updatePoints(user, user2, points);
    }

    @Path("/updatePointsOverview/{user}/{points}")
    @POST
    public void updatePO(@PathParam("user") String user, @PathParam("points") int points){
        pointsLogic.updatePointsOverview(user, points);
    }


    @Path("/addPoints/{username}/{username2}/{points}")
    @POST
    public void addPoints(@PathParam("username") String username, @PathParam("username2") String username2, @PathParam("points") int points){
        meetLogic.updatePoints(username, username2, points);
    }

    @Path("/meeting/{username}/{username2}/{points}")
    @POST
    public void meeting(@PathParam("username") String username, @PathParam("username2") String username2, @PathParam("points") int points){
        meetLogic.coreLogic(username, username2);
    }

    @Path("/bestOf")
    @GET
    public String bestOf(){
        return pointsLogic.bestOf();
    }

    @Path("/listAllMeetings")
    @GET
    public String listAllMeetings(){
        return meetLogic.listAllMeetings();
    }

    @Path("/listUserMeetings/{username}")
    @GET
    public String listUserMeetings(@PathParam("username") String username){
        return meetLogic.listUserMeetings(username);
    }


}
// curl -i -H "Accept: application/json" -X POST http://127.0.0.1:8087/meetmeserver/api/test/


