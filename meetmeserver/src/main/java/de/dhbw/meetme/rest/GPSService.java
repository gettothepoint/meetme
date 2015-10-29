package de.dhbw.meetme.rest;

import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.logic.BasicLogic;
import de.dhbw.meetme.logic.GPSLogic;
import de.dhbw.meetme.logic.Verification;
import groovy.lang.Singleton;
import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import javax.inject.Inject;
        import javax.ws.rs.*;
        import java.util.Collection;


/**
 * Created by Paul on 28.09.2015.
 */

@Path("/api/login")
@Produces({"application/json"})
@Singleton

public class GPSService {

    private static final Logger log = LoggerFactory.getLogger(GPSService.class);

    @Inject
    Verification verification;
    @Inject
    GPSLogic GPSLogic;
    @Inject
    BasicLogic basicLogic;


    @Path("/{username}/{password}/{lat}/{long}")
    @GET
    public String putGPS(@PathParam("username") String username, @PathParam("password") String password, @PathParam("lat") String latitude, @PathParam("long") String longitude ){
        String pw = basicLogic.getMD5(password);
        if (verification.checkPassword(username, pw)){
            GPSLogic.updateGPS(username, latitude, longitude);
            return "updated GPS Data for " + username;
        } else {
            return "Password incorrect";
        }
    }

    // Martin: die übergibt den neuen String :)
    @Path("/list")
    @GET
    public String list() {
        log.debug("List GPSData");
        return GPSLogic.listGeo();
    }


    //curl -i -H "Accept: application/json" -X POST http://127.0.0.1:8087/meetmeserver/api/login/

}
