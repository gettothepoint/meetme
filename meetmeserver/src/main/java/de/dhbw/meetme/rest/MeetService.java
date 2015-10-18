package de.dhbw.meetme.rest;


import de.dhbw.meetme.logic.BasicLogic;
import de.dhbw.meetme.logic.MeetLogic;
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


    @Path("/check/{username}/{password}/{username2}")
    @POST
    public String check(@PathParam("username") String username, @PathParam("password") String password, @PathParam("username2") String username2 ) {
        if (!basicLogic.checkPassword(username,password)){
            return "Password incorrect";
        }
        else if (meetLogic.checkMeeting(username, username2)) {
           return "Meeting confirmed";
        }
        else {
            return "Meeting rejected";
        }

    }
/*
    @Path("/checkpw/{username}/{password}")
    @GET
    public boolean checkpw(@PathParam("username") String username, @PathParam("password") String pw){
        return basicLogic.checkPassword(username, pw);

    }

    @Path("/checkmeet/{username}/{username2}")
    @GET
    public boolean checkMeet(@PathParam("username") String username, @PathParam("username2") String username2){
        return meetLogic.checkMeeting(username, username2);
    }

    @Path("checkunique/{name}")
    @GET
    public boolean checkUnique(@PathParam("name") String name){
        return basicLogic.usernameUnique(name);
    }
*/
}
