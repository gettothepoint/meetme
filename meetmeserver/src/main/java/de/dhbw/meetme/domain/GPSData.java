package de.dhbw.meetme.domain;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by vrettos on 08.10.2015.
 */
@Entity
@XmlRootElement
public class GPSData extends PersistentObject {


    private String username;
    private String userId;
    private String latitude;
    private String longitude;
    //TODO Datum und Zeit einfügen - dafür noch Variablen einfügen und Datentyp abstimmen


    public String getUsername(){ return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserId() { return userId; }
    public void setUserId(String userId){ this.userId = userId; }

    public String getLatitude() {return latitude; }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) {this.longitude = longitude; }



    @Override
    public String toString() {
        return "GeoData{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                "userid='" + userId + '\'' +
                "latitude='" + latitude + '\'' +
                "longitude='" + longitude + '\'' +
                '}';
    }
}
