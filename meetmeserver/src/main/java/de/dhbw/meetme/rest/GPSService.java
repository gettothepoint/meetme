package de.dhbw.meetme.rest;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.GPSClassicDao;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.database.dao.UserDao;
import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.domain.User;
        import de.dhbw.meetme.domain.UuidId;
        import groovy.lang.Singleton;
import org.hibernate.annotations.UpdateTimestamp;
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
    UserDao userDao;
    @Inject
    UserClassicDao userClassicDao;
    @Inject
    GPSClassicDao GPSClassicDao;
    @Inject
    Transaction transaction;


    @Path("/{username}/{long}/{lat}")
    @POST
    public String put(@PathParam("username") String username, @PathParam("long") String longitude, @PathParam("lat") String latitude ){
        transaction.begin();

        log.debug("Get User " + username);
        UuidId uID = userClassicDao.idFromName(username);
        String userId = uID.asString();

        log.debug("User found, continue to insert or update");
        GPSClassicDao.updateGPS(username, userId, latitude, longitude);

        transaction.commit();

        return "updated GPS Data for User "+username;
    }

    // für Käthe's Testzwecke bitte drin lassen
    @Path("/list")
    @GET
    public Collection<GPSData> list() {
        log.debug("List GPSData");
        return GPSClassicDao.list();
    }
    //curl -i -H "Accept: application/json" -X POST http://127.0.0.1:8087/meetmeserver/api/login/

}
