package de.dhbw.meetme.rest;


import de.dhbw.meetme.logic.BasicLogic;
import de.dhbw.meetme.logic.MeetLogic;
import de.dhbw.meetme.logic.Verification;
import groovy.lang.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;

/**
 * Created by Paul on 15.10.2015.
 */

@Path("/api/meet")
@Produces({"application/json"})
@Singleton

public class MeetService {
    private static final Logger log = LoggerFactory.getLogger(GPSService.class);

    @Inject
    BasicLogic basicLogic;
    @Inject
    MeetLogic meetLogic;
    @Inject
    Verification verification;


    @Path("/check/{username}/{password}/{username2}")
    @POST
    public String check(@PathParam("username") String username, @PathParam("password") String password, @PathParam("username2") String username2) {
        if (!verification.checkPassword(username, password)) {
            return "Password incorrect";
        } else if (meetLogic.checkMeeting(username, username2)) {
            return "Meeting confirmed";
        } else {
            return "Meeting rejected";
        }
    }

    @Path("/tryEqualTeams/{user1}/{user2}")
    @POST
    public String tryEqualTeams(@PathParam("user1") String user1, @PathParam("user2") String user2) {
        meetLogic.checkEqualTeams(user1, user2);

        int points1 = meetLogic.getPoints(user1);
        int points2 = meetLogic.getPoints(user2);
        int teamred = meetLogic.getTeamPoints("red");
        int teamblue = meetLogic.getTeamPoints("blue");
        return "The Points have been updated"+
                "The new Points of " + user1 + " are " + points1 +
                "The new Points of " + user2 + " are " + points2 +
                "The new Points of the red team are " + teamred +
                "The new Points of the blue team are " + teamblue +
                "WATCH OUT MEETING NOT CHECKED";

    }

    @Path("/bestteam")
    @GET
    public String best(){
        return meetLogic.bestTeams();
    }


    @Path("/points/{username}/{username2}")
    @POST
    public String check(@PathParam("username") String username, @PathParam("username2") String username2 ) {
        if (meetLogic.checkMeeting(username, username2)) {

            meetLogic.updatePoints(username, 1);
            meetLogic.updatePoints(username2, 1);
            int points1 = meetLogic.getPoints(username);
            int points2 = meetLogic.getPoints(username2);
            int teamred = meetLogic.getTeamPoints("red");
            int teamblue = meetLogic.getTeamPoints("blue");
            return "Meeting confirmed, Points added. " +
                    "The new Points of " + username + " are " + points1 +
                    "The new Points of " + username2 + " are " + points2 +
                    "The new Points of the red team are " + teamred +
                    "The new Points of the blue team are " + teamblue;

        }
        return "Eure GeoDaten sind zu weit auseinander!";
    }


}
