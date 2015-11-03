package de.dhbw.meetme.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

/**
 * Käthe, 11.10.15
 */
@Entity
@XmlRootElement
public class Points extends PersistentObject {

    private String team;
    private String username;
    private String userId;
    private String username2;
    private String userId2;
    private String team2;
    private int point;
    @Basic(optional = false)
    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp timestamp;

    public String getTeam(){ return team; }
    public void setTeam(String team){ this.team = team; }

    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }

    public String getUserId(){ return userId; }
    public void setUserId(String userId){ this.userId = userId; }

    public String getTeam2(){ return team2; }
    public void setTeam2(String team){ this.team2 = team; }

    public String getUsername2(){ return username2; }
    public void setUsername2(String username){ this.username2 = username; }

    public String getUserId2(){ return userId2; }
    public void setUserId2(String userId){ this.userId2 = userId; }

    public int getPoint(){ return point; }
    public void setPoint(int point){ this.point = point; }
    public String getPointS(){ return Integer.toString(point); }
    public void setPointS(String pointS){ this.point = Integer.parseInt(pointS); }

    public Timestamp getTimestamp(){return timestamp;}
    public void setTimestamp (String ts){ this.timestamp = Timestamp.valueOf(ts);}

    @Override
    public String toString(){
        return "Points{" +
                "id='" + id + '\'' +
                ", team'" + team + '\'' +
                ", username'" + username + '\'' +
                ", userID'" + userId + '\'' +
                ", team2'" + team2 + '\'' +
                ", username2'" + username2 + '\'' +
                ", userID2'" + userId2 + '\'' +
                ", point'" + point + '\'' +
                ", timestamp'" + timestamp + '\'' +
                '}';
    }
}
