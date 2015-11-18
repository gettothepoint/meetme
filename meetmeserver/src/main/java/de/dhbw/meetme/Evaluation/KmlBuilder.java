package de.dhbw.meetme.Evaluation;

import de.dhbw.meetme.database.Transaction;
import de.dhbw.meetme.domain.GPSData;
import de.dhbw.meetme.logic.GPSLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

/**
 * Created by vrettos on 18.11.2015.
 */
public class KmlBuilder {
    private static final Logger log = LoggerFactory.getLogger(KmlBuilder.class);

    @Inject
    GPSLogic geoLogic;
    @Inject
    Transaction transaction;


    public void buildKml(String username){
        Collection<GPSData> list = geoLogic.listGeoandTimeStamp(username);
        for (GPSData data: list){

        }
    }

    //public void readKml(file)
}
