package de.dhbw.meetme.domain;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Tabelle für die Punkteübersicht - user und team
 */

@Entity
@XmlRootElement
public class PointsOverview extends PersistentObject{

    private String team;
    private String username;
    private String userId;
    private int point;
    private int version;

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

    public int getVersion(){ return version; }
    public void setVersion(int version){ this.version = version; }
    public String getVersionS(){ return Integer.toString(version); }
    public void setVersionS(String versionS){ this.version = Integer.parseInt(versionS); }

    @Override
    public String toString(){
        return "Points{" +
                "id='" + id + '\'' +
                ", team'" + team + '\'' +
                ", username'" + username + '\'' +
                ", userID'" + userId + '\'' +
                ", point'" + point + '\'' +
                '}';
    }
}

