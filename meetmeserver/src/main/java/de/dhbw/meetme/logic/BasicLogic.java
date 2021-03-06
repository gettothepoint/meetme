package de.dhbw.meetme.logic;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.GPSClassicDao;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.domain.Points;
import de.dhbw.meetme.domain.User;
import de.dhbw.meetme.domain.UuidId;
import de.dhbw.meetme.rest.GPSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Versuch eine Klasse zu erstellen, die alle grundlegenden logischen Aufrufe der Services bedient..
 */
public class BasicLogic {
    private static final Logger log = LoggerFactory.getLogger(GPSService.class);

    @Inject
    UserClassicDao userClassicDao;
    @Inject
    PointsLogic pointsLogic;
    @Inject
    Transaction transaction;

    public void addUser(String firstname, String lastname, String username, String email, String password, String team){
        User u = new User();
        u.setName(username);
        u.setFirstname(firstname);
        u.setLastname(lastname);
        u.setEmail(email);
        u.setPassword(password);
        if(team.equals("random"))
            u.chooseTeam();
        else
            u.setTeam(team);

        transaction.begin();
        userClassicDao.persist(u);
        transaction.commit();

        pointsLogic.createPointsOverview(username, u.getId().asString(), team);
    }

    public void updateLoggedin(String username, boolean login){
        transaction.begin();
        String id = userClassicDao.idFromName(username).asString();
        User u = getUser(id);

        u.setLoggedin(login);
        userClassicDao.updateUser(u);
        transaction.commit();
    }

    public boolean getLoggedin(String username){
        transaction.begin();
        String id = userClassicDao.idFromName(username).asString();
        User u = getUser(id);
        boolean loggedin = u.getLoggedin();
        transaction.commit();
        return loggedin;
    }


    public String getMD5(String input) {
        byte[] source;
        try {
            //Get byte according by specified coding.
            source = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            source = input.getBytes();
        }
        String result = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            //The result should be one 128 integer
            byte temp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = temp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            result = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




    public Collection<User> listUsers(){
        return userClassicDao.list();
    }

    public User getUser(String id){
        return userClassicDao.get(UuidId.fromString(id));
    }

    public void deleteUser(String id){
        transaction.begin();
        userClassicDao.delete(UuidId.fromString(id));
        transaction.commit();
    }



}
