package de.dhbw.meetme.domain;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * Käthe, 11.10.15
 */
@Entity
@XmlRootElement
public class Points extends PersistentObject {

    private String team;
    private String username;
    private String userId;
    private int point;

    public String getTeam(){ return team; }
    public void setTeam(String team){ this.team = team; }

    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }

    public String getUserId(){ return userId; }
    public void setUserId(String userId){ this.userId = userId; }

    public int getPoint(){ return point; }
    public void setPoint(int point){ this.point = point; }
    public String getPointS(){ return Integer.toString(point); }
    public void setPointS(String pointS){ this.point = Integer.parseInt(pointS); }


    @Override
    public String toString(){
        return "GPSData{" +
                "id='" + id + '\'' +
                ", team'" + team + '\'' +
                ", username'" + username + '\'' +
                ", userID'" + userId + '\'' +
                ", point'" + point + '\'' +
                '}';
    }
}
