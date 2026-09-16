package app.daos;

import app.daos.generic.EntityManagerDAO;
import app.entities.Category;
import app.enums.Categories;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

public class CategoryDAO extends EntityManagerDAO<Category> {

    public CategoryDAO(EntityManager em) {
        super(em, Category.class);
    }

    // ________________________________________________________

    public List<Category> getAllByWeddingIdAndOwnerId(UUID weddingId, UUID ownerId) {
        String jpql = "SELECT c FROM Category c WHERE c.wedding.id = :weddingId AND c.wedding.owner.id = :ownerId ORDER BY c.position";

        return executeQuery(() ->
            em.createQuery(jpql, Category.class)
                .setParameter("weddingId", weddingId)
                .setParameter("ownerId", ownerId)
                .getResultList()
        );
    }

    // ________________________________________________________

    public Category getByIdAndOwnerId(UUID id, UUID ownerId) {
        String jpql = "SELECT c FROM Category c WHERE c.id = :id wedding.owner.id = :ownerId";

        return executeQuery(() ->
            em.createQuery(jpql, Category.class)
                .setParameter("id", id)
                .setParameter("ownerId", ownerId)
                .getSingleResult()
        );
    }

    // ________________________________________________________

    public Category getUncategorized(UUID weddingId, UUID ownerId) {
        String jpql = "SELECT c FROM Category c WHERE c.wedding.id = :weddingId AND c.wedding.owner.id = :ownerId AND c.title = :title";

        return executeQuery(() ->
            em.createQuery(jpql, Category.class)
                .setParameter("weddingId", weddingId)
                .setParameter("ownerId", ownerId)
                .setParameter("title", Categories.UNCATEGORIZED.getDisplayName())
                .getSingleResult()
        );
    }
}