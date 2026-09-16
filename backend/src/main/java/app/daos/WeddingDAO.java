package app.daos;

import app.daos.generic.EntityManagerDAO;
import app.entities.Wedding;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

public class WeddingDAO extends EntityManagerDAO<Wedding> {

    public WeddingDAO(EntityManager em) {
        super(em, Wedding.class);
    }

    // ________________________________________________________

    public List<Wedding> getAllByOwnerId(UUID ownerId) {
        String jpql = "SELECT w FROM Wedding w WHERE w.owner.id = :ownerId ORDER BY w.date, w.id";

        return executeQuery(() ->
            em.createQuery(jpql, Wedding.class)
                .setParameter("ownerId", ownerId)
                .getResultList()
        );
    }

    // ________________________________________________________

    public Wedding getByIdAndOwnerId(UUID id, UUID ownerId) {
        String jpql = "SELECT w FROM Wedding w WHERE w.id = :id AND w.owner.id = :ownerId";

        return executeQuery(() ->
            em.createQuery(jpql, Wedding.class)
                .setParameter("id", id)
                .setParameter("ownerId", ownerId)
                .getSingleResult()
        );
    }
}
