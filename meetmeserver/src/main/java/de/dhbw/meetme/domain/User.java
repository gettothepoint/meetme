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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
