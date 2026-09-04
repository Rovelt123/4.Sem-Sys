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
        return "username";
    }

    @Override
    protected Object getExistingColumnValue(User entity) {
        return entity.getUsername();
    }

    @Override
    protected Object getMissingColumnValue() {
        return "missing-user";
    }

    // ________________________________________________________

    @Test
    void getByUsername() {
        userDAO.create(testUser);

        em.flush();
        em.clear();

        User found = userDAO.getByUsername("john123");

        assertNotNull(found);
        assertEquals("john123", found.getUsername());
        assertEquals(Set.of(Role.USER), found.getRoles());
    }

    // ________________________________________________________

    @Test
    void getByUsernameNull() {
        assertNull(userDAO.getByUsername("missing-user"));
    }
}
