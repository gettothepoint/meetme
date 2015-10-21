package de.dhbw.meetme.rest;

import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.logic.BasicLogic;
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
    BasicLogic basicLogic;


    @Path("/{username}/{password}/{lat}/{long}")
    @POST
    public String putGPS(@PathParam("username") String username, @PathParam("password") String password, @PathParam("lat") String latitude, @PathParam("long") String longitude ){
        if (basicLogic.checkPassword(username, password))
        {
            basicLogic.updateGPS(username, latitude, longitude);
            return "updated GPS Data for User " + username;
        }
        else {return "Password incorrect";}
    }

    // Martin: die übergibt den neuen String :)
    @Path("/list")
    @GET
    public String list() {
        log.debug("List GPSData");
        return basicLogic.listGPSData();
    }


    //curl -i -H "Accept: application/json" -X POST http://127.0.0.1:8087/meetmeserver/api/login/

}
