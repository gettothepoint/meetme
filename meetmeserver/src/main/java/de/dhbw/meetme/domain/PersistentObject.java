package de.dhbw.meetme.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 *
 */
@MappedSuperclass
//has no separate table defined for it
//mapping information is applied to the entities inheriting

abstract public class PersistentObject {
  @EmbeddedId protected UuidId id;
  //only one embeddedId assigned and no further id
  //used as primary key
  //as an embedded Object UuidId is not an entity itself!

  public PersistentObject() {
    id = new UuidId();
    //wird initialisiert, damit jedes Objekt auch eine ID hat
  }

  public UuidId getId() {
    return id;
  }

  public void setId(UuidId id) {
    this.id = id;
  }

  @Override
  //just because it overrides a method declaration in a supertype
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PersistentObject)) return false;

    PersistentObject that = (PersistentObject) o;

    return id.equals(that.id);

  }

  @Override
  public int hashCode() {
    return id.hashCode(); //ist aktuell noch leer --> Sinn?
  }

  @Override
  public String toString() {
    return "PersistentObject{" +
        "id=" + id +
        '}';
  }
}
