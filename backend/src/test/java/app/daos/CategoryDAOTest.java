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

        assertEquals(1, found.size());
        assertEquals("Food & drinks", found.get(0).getTitle());
        assertEquals(wedding.getId(), found.get(0).getWedding().getId());

    }

    // ________________________________________________________

    @Test
    void getAllByWeddingIdAndOwnerIdOnlyGetCategoriesForRequestedWedding() {


        categoryDAO.create(category);  // category belongs to wedding, and is Food & drinks
        categoryDAO.create(category2); // category2 belongs to wedding2

        em.flush();
        em.clear();

        List<Category> found = categoryDAO.getAllByWeddingIdAndOwnerId(wedding.getId(),testUser.getId());

        assertEquals(1, found.size());
        assertEquals("Food & drinks", found.get(0).getTitle());
        // testUser have to weddings with FOOD_AND_DRINK, corret one have position 1, the wrong one have position 2
        assertEquals(1, found.get(0).getPosition());

    }

    // ________________________________________________________

    @Test
    void getByIdAndOwnerId() {

        Category category3 = Category.builder()
                .title(Categories.VENUE.getDisplayName())
                .position(2)
                .wedding(wedding)
                .build();

        categoryDAO.create(category);
        categoryDAO.create(category3);

        em.flush();
        em.clear();

        Category found = categoryDAO.getByIdAndOwnerId(category3.getId(), testUser.getId());

        assertNotNull(found);
        // Enum VENUE -> "Venue"
        assertEquals("Venue", found.getTitle());
        assertEquals(wedding.getId(), found.getWedding().getId());

    }

    // ________________________________________________________

    @Test
    void getByIdAndOwnerIdReturnNullWhenOwnerIsWrong() {

        userDAO.create(testUser2);

        // category belongs to testUser´wedding not testUser2
        categoryDAO.create(category);

        em.flush();
        em.clear();

        Category found = categoryDAO.getByIdAndOwnerId(category.getId(), testUser2.getId());

        assertNull(found);

    }

    // ________________________________________________________

    @Test
    void getUncategorized() {

       Category uncategorized = Category.builder()
                .title(Categories.UNCATEGORIZED.getDisplayName())
                .position(3)
                .wedding(wedding)
                .build();

        categoryDAO.create(uncategorized);

        em.flush();
        em.clear();

        Category found = categoryDAO.getUncategorized(wedding.getId(), testUser.getId());

        assertNotNull(found);
        assertEquals(Categories.UNCATEGORIZED.getDisplayName(), found.getTitle());

    }

    // ________________________________________________________

    @Test
    void getUncategorizedWrongWeddingID() {

        Category uncategorized = Category.builder()
                .title(Categories.UNCATEGORIZED.getDisplayName())
                .position(3)
                .wedding(wedding)
                .build();

        categoryDAO.create(uncategorized);

        em.flush();
        em.clear();

        // using wrong weddingID from same owner
        Category found = categoryDAO.getUncategorized(wedding2.getId(), testUser.getId());

        assertNull(found);

    }

    // ________________________________________________________

    @Test
    void getUncategorizedWrongOwnerID() {

        Category uncategorized = Category.builder()
                .title(Categories.UNCATEGORIZED.getDisplayName())
                .position(3)
                .wedding(wedding)
                .build();

        categoryDAO.create(uncategorized);

        userDAO.create(testUser2);

        em.flush();
        em.clear();

        // using wrong owner for correct weddingId
        Category found = categoryDAO.getUncategorized(wedding.getId(), testUser2.getId());

        assertNull(found);

    }
}