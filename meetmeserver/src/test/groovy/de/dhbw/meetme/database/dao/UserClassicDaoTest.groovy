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

    private User createUser(UserClassicDao dao, int num){
        User u = new User();
        u.name = "Testusername" +num;
        u.firstname = "Testfirstname" +num;
        u.lastname = "Testlastname" +num;
        u.email = "Testemail" +num;
        u.password = "Testpassword" +num;
        u.team = "blue"

        dao.persist(u);
        u
    }
/* setup:
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


    void "test User"(){
        setup:
        UserClassicDao dao = new UserClassicDao();
        dao.inTest = true;
        dbTestUtil.em.getTransaction().begin();
        dao.entityManager = dbTestUtil.em;
        User u1 = createUser(dao, 1);
        User u2 = createUser(dao, 2);
        UuidId id1 = u1.getId();
        UuidId id2 = u2.getId();

        when:
        User foundUser = dao.get(id1);

        then:
        foundUser
        foundUser.id == id1;
        foundUser.name.equals("Testusername1");

        cleanup:
        dbTestUtil.em.getTransaction().commit();
    }

    void testDelete() {

    }

    void testGet() {

    }

    void testList() {

    }

    def "Give out right id if username is inserted"() {
        setup:
        UserClassicDao dao = new UserClassicDao();
        dao.inTest = true;
        dbTestUtil.em.getTransaction().begin();
        dao.entityManager = dbTestUtil.em;
        User u = createUser(dao, 1);
        UuidId idu = u.getId();
        String id = idu.asString();
        String name = u.getName();


        when:
        String foundid = (UserClassicDao.idFromName(name)).asString();

        expect:
        foundid
        foundid == id;

        cleanup:
        dbTestUtil.em.getTransaction().commit();
    }

    void testIdFromName() {

    }
    */
}
