package de.dhbw.meetme.logic;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.GPSClassicDao;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.domain.GPSData;
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
    GPSClassicDao GPSClassicDao;
    @Inject
    Transaction transaction;

    UserClassicDao userDao = new UserClassicDao();

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

    }

    public void updateGPS(String username, String latitude, String longitude){

        transaction.begin();

        log.debug("Get User " + username);
        UuidId uID = userClassicDao.idFromName(username);
        String userId = uID.asString();

        log.debug("User found, continue to insert or update");
        GPSClassicDao.updateGPS(username, userId, latitude, longitude);

        transaction.commit();

    }

    public String listGPSData(){
        StringBuffer sb = new StringBuffer("{\"gPSData\"[");
        Collection<GPSData> list = GPSClassicDao.list();
        for (GPSData data: list){
            sb.append("{\"latitude\":");
            sb.append(data.getLatitude());
            sb.append(",\"longitude\":");
            sb.append(data.getLongitude());
            sb.append(",\"username\":\"");
            sb.append(data.getUsername());
            sb.append("\"},");
        }
        sb.append("]}");

        return sb.toString();

        //return GPSClassicDao.list();
    }

    public Collection<GPSData> colGPSData(){
        return GPSClassicDao.list();
    }

    //by Pia - todo: nochmal Funktion klären!
    public static String getMD5(String input) {
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


}
