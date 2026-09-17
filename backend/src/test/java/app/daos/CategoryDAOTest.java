package app.daos;

import app.daos.generic.EntityManagerDAOTest;
import app.daos.generic.IDAO;
import app.entities.Category;
import app.entities.Wedding;
import app.enums.Categories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDAOTest extends EntityManagerDAOTest<Category> {

    @Override
    protected IDAO<Category> getDao(){ return categoryDAO; }

    @Override
    protected Category createEntity(){ return category; }

    @Override
    protected Category createSecondEntity(){ return category2; }

    @Override
    protected Object getEntityId(Category entity) {
        return entity.getId();
    }

    @Override
    protected Object getMissingEntityId() {
        return UUID.randomUUID();
    }

    @Override
    protected EntityManagerDAOTest.EntityUpdate<Category> getEntityUpdate() {
        return new EntityManagerDAOTest.EntityUpdate<>(
                category -> category.setPosition(2),
                category -> assertEquals(2, category.getPosition())
        );
    }

    @Override
    protected String getExistingColumnName() { return "title"; }

    @Override
    protected Object getExistingColumnValue(Category entity) {
        return entity.getTitle();
    }

    @Override
    protected Object getMissingColumnValue() { return "missing/wrong title"; }

// ________________________________________________________

    @BeforeEach
    void setupCategoryDaoTest() {
        userDAO.create(testUser);

        weddingDAO.create(wedding);
        weddingDAO.create(wedding2);

        em.flush();
        em.clear();

    }

    // ________________________________________________________

    @Test
    void getAllByWeddingIdAndOwnerId() {

        categoryDAO.create(category);

        em.flush();
        em.clear();

        List<Category> found = categoryDAO.getAllByWeddingIdAndOwnerId(wedding.getId(),testUser.getId());

        assertNotNull(found);
        assertEquals("Catering", found.get(0).getTitle());
        assertEquals(wedding.getId(), found.get(0).getWedding().getId());

    }

    // ________________________________________________________

    @Test
    void getAllByWeddingIdAndOwnerIdOnlyGetCategoriesForThisWedding() {

        categoryDAO.create(category);
        categoryDAO.create(category2);

        em.flush();
        em.clear();

        List<Category> found = categoryDAO.getAllByWeddingIdAndOwnerId(wedding.getId(),testUser.getId());

        assertEquals(1, found.size());
        assertEquals("Catering", found.get(0).getTitle());
        assertEquals(1, found.get(0).getPosition());

    }

    // ________________________________________________________

    @Test
    void getByIdAndOwnerId() {
        userDAO.create(testUser2);

        categoryDAO.create(category);

        em.flush();
        em.clear();

        Category found = categoryDAO.getByIdAndOwnerId(category.getId(), testUser2.getId());

        assertNull(found);

    }

    // ________________________________________________________

    @Test
    void getByIdAndOwnerIdReturnNullWhenOwnerIsWrong() {

        Category category3 = Category.builder()
                .title(Categories.DRINKS.getDisplayName())
                .position(2)
                .wedding(wedding)
                .build();

        categoryDAO.create(category);
        categoryDAO.create(category3);

        em.flush();
        em.clear();

        Category found = categoryDAO.getByIdAndOwnerId(category3.getId(), testUser.getId());

        assertNotNull(found);
        assertEquals("Alcohol & soft drinks", found.getTitle());
        assertEquals(wedding.getId(), found.getWedding().getId());

    }

    // ________________________________________________________

    @Test
    void getUncategorized() {

       Category uncategorized = Category.builder()
                .title(Categories.UNCATEGORIZED.getDisplayName())
                .position(3)
                .wedding(wedding)
                .build();

        categoryDAO.create(category);

        em.flush();
        em.clear();


    }
}