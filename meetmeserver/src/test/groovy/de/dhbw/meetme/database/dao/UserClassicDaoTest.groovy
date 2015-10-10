package de.dhbw.meetme.database.dao

import de.dhbw.meetme.database.Transaction
import de.dhbw.meetme.domain.User
import de.dhbw.meetme.domain.UuidId
import spock.lang.Specification
/**
 * Created by vrettos on 09.10.2015.
 * strg+shift+t für testklassen
 */
class UserClassicDaoTest extends Specification {

    @Delegate DbTestUtil dbTestUtil = new DbTestUtil()
/*
    private User createUser(UserClassicDao dao, String name){
        User u = new User();
        u.name = name
        u.firstname = "Testfirstname"
        u.lastname = "Testlastname"
        u.email = "Testemail"
        u.password = "Testpassword"
        u.team = "blue"

        dao.persist(u);
        u
    }

    void "See if User persists"() {
        setup:
        UserClassicDao dao = new UserClassicDao()
        dbTestUtil.em.getTransaction().begin();
        dao.entityManager = dbTestUtil.em
        User u = createUser(dao, "testUsername")
        UuidId id = u.id
        dbTestUtil.em.getTransaction().commit();
        dbTestUtil.em.close()
        dbTestUtil.createEntityManager()
        dao.entityManager = dbTestUtil.em

        when:
        User foundUser = dao.get(id)

        then:
        foundUser
        foundUser.id == id
        foundUser.name == "testUsername"
    }
*/
    void testDelete() {

    }

    void testGet() {

    }

    void testList() {

    }

    void testFindByName() {

    }

    void testIdFromName() {

    }
}
