package app.daos;

import app.daos.generic.EntityManagerDAOTest;
import app.daos.generic.IDAO;
import app.entities.User;
import app.entities.Wedding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

 class WeddingDAOTest extends EntityManagerDAOTest<Wedding> {

    @Override
    protected IDAO<Wedding> getDao(){ return weddingDAO; }

    @Override
    protected Wedding createEntity(){ return wedding; }

    @Override
    protected Wedding createSecondEntity(){ return wedding2; }

    @Override
    protected Object getEntityId(Wedding entity) {
         return entity.getId();
     }

    @Override
    protected Object getMissingEntityId() {
         return UUID.randomUUID();
     }

     @Override
     protected EntityUpdate<Wedding> getEntityUpdate() {
         return new EntityUpdate<>(
                 wedding -> wedding.setLocation("Lyngby updated"),
                 wedding -> assertEquals("Lyngby updated", wedding.getLocation())
         );
     }

     @Override
     protected String getExistingColumnName() { return "location"; }

     @Override
     protected Object getExistingColumnValue(Wedding entity) {
         return entity.getLocation();
     }

     @Override
     protected Object getMissingColumnValue() { return "missing/wrong location"; }

     //______________________________________________________

     @BeforeEach
     void setupWeddingDaoTest() {
         userDAO.create(testUser);
     }

     //______________________________________________________

     @Test
    void getAllByOwnerId() {

        weddingDAO.create(wedding);
        weddingDAO.create(wedding2);

        em.flush();
        em.clear();

        List<Wedding> found = weddingDAO.getAllByOwnerId(testUser.getId());

        assertEquals(2, found.size());

        assertEquals(dateTwo, found.get(0).getDate());

        assertEquals(date, found.get(1).getDate());
    }

     //______________________________________________________

     @Test
     void getAllByOwnerIdOnlyOwnersWeddings() {
         userDAO.create(testUser2);

         Wedding ownersWedding = Wedding.builder()
                 .title("Owners wedding")
                 .date(date)
                 .location("Lyngby")
                 .budget(weddingBudget)
                 .build();
         ownersWedding.setOwner(testUser2);

         weddingDAO.create(wedding);
         weddingDAO.create(wedding2);

         weddingDAO.create(ownersWedding);

         em.flush();
         em.clear();

         List<Wedding> found = weddingDAO.getAllByOwnerId(testUser2.getId());

         assertEquals(1, found.size());

         assertEquals("Owners wedding", found.get(0).getTitle());

     }

     //______________________________________________________

    @Test
    void getByIdAndOwnerId() {
        weddingDAO.create(wedding);

        em.flush();
        em.clear();

        Wedding found = weddingDAO.getByIdAndOwnerId(wedding.getId(), testUser.getId());

        assertEquals(wedding.getId(), found.getId());

        assertEquals(testUser.getId(), found.getOwner().getId());
    }

    //______________________________________________________

     @Test
     void getByIdAndOwnerIdShouldReturnNullWhenOwnerDoesNotMatch() {
         userDAO.create(testUser2);

         weddingDAO.create(wedding);

         UUID weddingId = wedding.getId();

         em.flush();
         em.clear();

         Wedding found =
                 weddingDAO.getByIdAndOwnerId(
                         weddingId,
                         testUser2.getId()
                 );

         assertNull(found);
     }
}