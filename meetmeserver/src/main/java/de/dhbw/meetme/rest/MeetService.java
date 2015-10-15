package de.dhbw.meetme.rest;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.GPSClassicDao;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.database.dao.UserDao;
import de.dhbw.meetme.logic.Meet;
import groovy.lang.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Created by Paul on 15.10.2015.
 */

@Path("/api/meet")
@Produces({"application/json"})
@Singleton

public class MeetService {
    private static final Logger log = LoggerFactory.getLogger(GPSService.class);

    @Inject
    UserDao userDao;
    @Inject
    UserClassicDao userClassicDao;
    @Inject
    de.dhbw.meetme.database.dao.GPSClassicDao GPSClassicDao;
    @Inject
    Transaction transaction;


    @Path("/check/{username}/{password}/{username2}")
    @POST
    public boolean check(@PathParam("username") String username, @PathParam("password") String password, @PathParam("username2") String username2 ) {
        transaction.begin();
        if (userClassicDao.rightpassword(username, password)) {
            Meet mt = new Meet();
            transaction.commit();
            return mt.meeting(username, username2);
        }
        else
        {
            transaction.commit();
            return false;
        }
    }

}
