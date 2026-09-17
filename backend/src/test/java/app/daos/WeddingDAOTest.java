package app.daos;

import app.daos.generic.EntityManagerDAOTest;
import app.daos.generic.IDAO;
import app.entities.User;
import app.entities.Wedding;
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

     @Test
    void getAllByOwnerId() {
        userDAO.create(testUser);

        weddingDAO.create(wedding);
        weddingDAO.create(wedding2);

        em.flush();
        em.clear();

        List<Wedding> found = weddingDAO.getAllByOwnerId(testUser.getId());

        assertEquals(2, found.size());

        assertEquals(dateTwo, found.get(0).getDate());

        assertEquals(date, found.get(1).getDate());
    }

    @Test
    void getByIdAndOwnerId() {
    }
}