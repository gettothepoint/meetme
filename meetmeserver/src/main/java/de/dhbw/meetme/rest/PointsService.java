package de.dhbw.meetme.rest;

import de.dhbw.meetme.domain.User;
import de.dhbw.meetme.logic.BasicLogic;
import de.dhbw.meetme.logic.Verification;
import de.dhbw.meetme.logic.PointsLogic;
import groovy.lang.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Collection;

/**
 * Created by Paul on 29.10.2015.
 */

@Path("/api/points")
@Produces({"application/json"}) // mime type
@Singleton

public class PointsService {

    @Inject
    BasicLogic basicLogic;
    @Inject
    Verification verification;
    @Inject
    PointsLogic pointsLogic;


    @Path("/score/{username}/{password}")
    @GET
    public String score(@PathParam("username") String username, @PathParam("password") String password){
        String pw = basicLogic.getMD5(password);
        if (!verification.checkPassword(username, pw)) {
            return "Password incorrect";
        } else {
            return String.valueOf(pointsLogic.score(username));
        }
    }

    @Path("/best/team")
    @GET
    public String bestTeam(){
        return String.valueOf(pointsLogic.bestTeam());
    }

    @Path("/best/teamscore")
    @GET
    public String bestTeamScore(){
        String bestTeam = pointsLogic.bestTeam();
        if (bestTeam.equals("red Team")){
            return String.valueOf(pointsLogic.score("teamRed"));
        }
        else if (bestTeam.equals("blue Team")){
            return String.valueOf(pointsLogic.score("teamBlue"));
        }
        else if (bestTeam.equals("tie")){
            return String.valueOf(pointsLogic.score("teamBlue"));
        }
        else {
            return "Fehler bei der Ausgabe des besten Teams.";
        }

    }

    @Path("/best/users")
    @GET
    public String best(){
        return String.valueOf(pointsLogic.bestOf());
    }

    @Path("/best/teamusers/{team}")
    @GET
    public String bestTeamUsers(@PathParam("team")String team){
        return String.valueOf(pointsLogic.bestOfTeams(team));
    }

    @Path("/teamscore/{team}")
    @GET
    public String teamScore (@PathParam("team") String team){
        return String.valueOf(pointsLogic.score(team));
    }

}
