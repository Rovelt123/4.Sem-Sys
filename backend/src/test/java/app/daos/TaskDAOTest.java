package app.daos;

import app.daos.generic.EntityManagerDAOTest;
import app.daos.generic.IDAO;
import app.entities.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskDAOTest extends EntityManagerDAOTest<Task> {

    @Override
    protected IDAO<Task> getDao(){ return weddingDAO; }

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
    void setUp() {
    }

    @Test
    void getAllByCategoryIdAndOwnerId() {
    }

    @Test
    void getByIdAndOwnerId() {
    }
}