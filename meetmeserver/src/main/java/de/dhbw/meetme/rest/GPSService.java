package de.dhbw.meetme.rest;

import de.dhbw.meetme.database.Transaction;
        import de.dhbw.meetme.database.dao.UserDao;
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
    Transaction transaction;

    @Path("/{username}/{long}/{lat}")
    @POST
    public String put(@PathParam("username") String username, @PathParam("long") String longitude, @PathParam("lat") String latitude ){
        transaction.begin();
        log.debug("Get User" + username);
        User thisuser = userDao.findByUsername(username);

        thisuser.setLatitude(latitude);
        thisuser.setLongitude(longitude);
        transaction.commit();

        return "updated GPS Data for User "+username;
    }

}
