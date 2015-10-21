package de.dhbw.meetme.logic;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.database.dao.GPSClassicDao;
import de.dhbw.meetme.database.dao.UserClassicDao;
import de.dhbw.meetme.domain.User;
import de.dhbw.meetme.domain.UuidId;
import de.dhbw.meetme.rest.GPSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Alle Abfragen zur Verifikation der Daten finden über diese Klasse statt
 */
public class Verification {
    private static final Logger log = LoggerFactory.getLogger(GPSService.class);

    @Inject
    UserClassicDao userClassicDao;
    @Inject
    Transaction transaction;

    public boolean checkPassword(String username, String password){
        transaction.begin();
        UuidId id = userClassicDao.idFromName(username);
        User u = userClassicDao.get(id);
        transaction.commit();
        return u.getPassword().equals(password);
    }

    public boolean usernameUnique(String username){
        transaction.begin();
        boolean b;
        if (username.equals("teamBlue") || username.equals("teamRed")){
            b= false;
        }else{
            b = (userClassicDao.idFromName(username) == null);
        }
        transaction.commit();
        return b;
    }

    public boolean checkMail(String email) {
        //by Kerstin - Funktion bestätigt!
        String EMAIL_PATTERN =  "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = Pattern.compile(EMAIL_PATTERN);
        Matcher m = p.matcher(email);
        return m.matches();

    }

    public boolean checkTeam(String team){
        return team.equals("red")||team.equals("blue")||team.equals("random");
    }
}
