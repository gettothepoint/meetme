package de.dhbw.meetme.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 */
@Embeddable
//instances are stored as intrinsic part of an owning entity and share the identity of the entity
//embedded objects are mapped to the database table for the entity
public class UuidId implements Serializable {
  @Column(length = 512)
  private String id;

  private UuidId(String id) {
    this.id = id;
  }

  public UuidId() {
    id = UUID.randomUUID().toString();
  }

  public void setId(String id) {
    this.id = UUID.fromString(id).toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UuidId id1 = (UuidId) o;

    return id.equals(id1.id);
  }

  @Override
  public int hashCode() {

    return id.hashCode(); //?!?!?!?
  }

  @Override
  public String toString() {
    return "UuidId{" +
        "id=" + id +
        '}';
  }

  public String asString() {
    return id;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static UuidId fromString(String id) {
    UUID.fromString(id); // validate
    return new UuidId(id);
  }
}