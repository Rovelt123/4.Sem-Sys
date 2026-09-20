package app.daos;

import app.daos.generic.EntityManagerDAOTest;
import app.daos.generic.IDAO;
import app.entities.Category;
import app.entities.Task;
import app.enums.Categories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskDAOTest extends EntityManagerDAOTest<Task> {

    @Override
    protected IDAO<Task> getDao(){ return taskDAO; }

    @Override
    protected Task createEntity(){ return task; }

    @Override
    protected Task createSecondEntity(){ return task2; }

    @Override
    protected Object getEntityId(Task entity) {
        return entity.getId();
    }

    @Override
    protected Object getMissingEntityId() {
        return UUID.randomUUID();
    }

    @Override
    protected EntityUpdate<Task> getEntityUpdate() {
        return new EntityUpdate<>(
                task -> task.setTitle("Find chef"),
                task -> assertEquals("Find chef", task.getTitle())
        );
    }

    @Override
    protected String getExistingColumnName() { return "title"; }

    @Override
    protected Object getExistingColumnValue(Task entity) {
        return entity.getTitle();
    }

    @Override
    protected Object getMissingColumnValue() { return "missing/wrong title"; }

    //______________________________________________________

        @BeforeEach
        void setupTaskDaoTest() {
            userDAO.create(testUser);

            weddingDAO.create(wedding);
            weddingDAO.create(wedding2);

            categoryDAO.create(category);
            categoryDAO.create(category2);

            em.flush();
            em.clear();

        }

    // ________________________________________________________

    @Test
    void getAllByCategoryIdAndOwnerId() {

        taskDAO.create(task);
        taskDAO.create(task2);

        em.flush();
        em.clear();

        List<Task> found = taskDAO.getAllByCategoryIdAndOwnerId(category.getId(),testUser.getId());

        assertEquals(2, found.size());

    }

    // ________________________________________________________

    @Test
    void getAllByCategoryIdAndOwnerIdFailWrongOwner() {

        userDAO.create(testUser2);

        taskDAO.create(task);

        em.flush();
        em.clear();

        List<Task> found = taskDAO.getAllByCategoryIdAndOwnerId(category.getId(),testUser2.getId());

        assertTrue(found.isEmpty());
    }

    // ________________________________________________________

    @Test
    void getAllByCategoryIdAndOwnerIdFailWrongCategory() {

       Category category3 = Category.builder()
                .title(Categories.VENUE.getDisplayName())
                .position(2)
                .wedding(wedding)
                .build();

        Task task3 = Task.builder()
                .title("find liquid company")
                .position(0)
                .category(category3)
                .build();

        categoryDAO.create(category3);

        taskDAO.create(task);
        taskDAO.create(task3);

        em.flush();
        em.clear();

        // using wrong category id from category instead of category3
        List<Task> found = taskDAO.getAllByCategoryIdAndOwnerId(category.getId(), testUser.getId());

        assertEquals(1, found.size());
        assertNotEquals(task3.getId(), found.get(0).getId());
    }

    // ________________________________________________________

    @Test
    void getByIdAndOwnerId() {

        taskDAO.create(task);

        em.flush();
        em.clear();

     Task found = taskDAO.getByIdAndOwnerId(task.getId(),testUser.getId());

        assertNotNull(found);

    }

    // ________________________________________________________

    @Test
    void getByIdAndOwnerIdWrongTaskId() {

        taskDAO.create(task);

        em.flush();
        em.clear();

        Task found = taskDAO.getByIdAndOwnerId(UUID.randomUUID(),testUser.getId());

        assertNull(found);
    }

    // ________________________________________________________

    @Test
    void getByIdAndOwnerIdWrongOwnerId() {

        userDAO.create(testUser2);

        taskDAO.create(task);

        em.flush();
        em.clear();

        // task belongs to testUser not testUser2
        Task found = taskDAO.getByIdAndOwnerId(task.getId(),testUser2.getId());

        assertNull(found);

    }

}