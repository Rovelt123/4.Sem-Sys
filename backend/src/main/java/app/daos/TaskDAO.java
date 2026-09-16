package app.daos;

import app.daos.generic.EntityManagerDAO;
import app.entities.Task;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

public class TaskDAO extends EntityManagerDAO<Task> {

    public TaskDAO(EntityManager em) {
        super(em, Task.class);
    }

    // ________________________________________________________

    public List<Task> getAllByCategoryIdAndOwnerId(UUID categoryId, UUID ownerId) {
        String jpql = "SELECT t FROM Task t WHERE t.category.id = :categoryId AND t.category.wedding.owner.id = :ownerId ORDER BY t.position";

        return executeQuery(() ->
            em.createQuery(jpql, Task.class)
                .setParameter("categoryId", categoryId)
                .setParameter("ownerId", ownerId)
                .getResultList()
        );
    }

    // ________________________________________________________

    public Task getByIdAndOwnerId(UUID id, UUID ownerId) {
        String jpql = "SELECT t FROM Task t WHERE t.id = :id AND t.category.wedding.owner.id = :ownerId";

        return executeQuery(() ->
            em.createQuery(jpql, Task.class)
                .setParameter("id", id)
                .setParameter("ownerId", ownerId)
                .getSingleResult()
        );
    }
}