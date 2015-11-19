package de.dhbw.meetme.rest;


import de.dhbw.meetme.logic.*;
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
    MeetLogic meetLogic;
    @Inject
    Verification verification;
    @Inject
    GPSLogic GPSLogic;
    @Inject
    BasicLogic basicLogic;


    @Path("/check/{username}/{password}/{username2}")
    @GET
    public String check(@PathParam("username") String username, @PathParam("password") String password, @PathParam("username2") String username2) {
        String pw = basicLogic.getMD5(password);
        if (!verification.checkPassword(username, pw)) {
            return "Password incorrect";
        }else if(!verification.usernameKnown(username2)){
            return "Meeting rejected - unknown user";
        } else if (GPSLogic.checkMeeting(username, username2)) {
            meetLogic.coreLogic(username, username2);
            return "Meeting confirmed";
        } else {
            return "Meeting rejected - GeoData too far apart.";
        }
    }

    @Path("/listmeetings/{username}")
    @GET
    public String listMeetings(@PathParam("username") String username){
        return meetLogic.listUserMeetings(username);
    }


}
