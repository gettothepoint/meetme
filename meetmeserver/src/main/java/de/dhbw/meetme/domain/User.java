package de.dhbw.meetme.domain;


import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@Entity // also add to persistence.xml !! -> done
@XmlRootElement // needed for REST JSON marshalling --> maps class to enum type or XML Element
//causes a global element declaration associated with the XML schema type to which the class is mapped
//wieso nicht @Table?
public class User extends PersistentObject {
    //nicht vergessen: alle Funktionen aus Persistent Object werden übernommen
    private String name;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String team;
    //zufall oder freiwillig, beides offen lassen



    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getTeam() {
        return team;
    }
    public void setTeam(String team) {
        this.team = team;
    }

    public String getName() {
    return name;
    }
    public void setName(String name) {
    this.name = name;
  }



    //Methode für die zufällige Verteilung der Teams
    public void chooseTeam()
    {
        int t = (int) Math.floor(Math.random()*2);
        if(t==0)
        {
            team = "green";
        }
        else
        {
            team = "blue";
        }
    }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", username='" + name + '\'' +
        '}';
  }
}
