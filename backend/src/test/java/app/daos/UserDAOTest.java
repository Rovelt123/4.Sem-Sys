package app.daos;

import app.daos.generic.IDAO;
import app.entities.User;
import app.enums.Role;
import app.daos.generic.EntityManagerDAOTest;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest extends EntityManagerDAOTest<User> {

    @Override
    protected IDAO<User> getDao() {
        return userDAO;
    }

    @Override
    protected User createEntity() {
        return testUser;
    }

    @Override
    protected User createSecondEntity() {
        return testUser2;
    }

    @Override
    protected Object getEntityId(User entity) {
        return entity.getId();
    }

    @Override
    protected Object getMissingEntityId() {
        return UUID.randomUUID();
    }

    @Override
    protected EntityUpdate<User> getEntityUpdate() {
        return new EntityUpdate<>(
                user -> user.setFirstname("Updated"),
                user -> assertEquals("Updated", user.getFirstname())
        );
    }

    @Override
    protected String getExistingColumnName() {
        return "email";
    }

    @Override
    protected Object getExistingColumnValue(User entity) {
        return entity.getEmail();
    }

    @Override
    protected Object getMissingColumnValue() {
        return "missing@test.dk";
    }

    // ________________________________________________________

    @Test
    void getByEmail() {
        userDAO.create(testUser);

        em.flush();
        em.clear();

        User found = userDAO.getByEmail("john123@test.dk");

        assertNotNull(found);
        assertEquals("john123@test.dk", found.getEmail());
        assertEquals(Set.of(Role.USER), found.getRoles());
    }

    // ________________________________________________________

    @Test
    void getByEmailNull() {
        assertNull(userDAO.getByEmail("missing@test.dk"));
    }
}
