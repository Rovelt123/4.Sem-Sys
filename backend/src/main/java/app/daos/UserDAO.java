package app.daos;

import app.daos.generic.EntityManagerDAO;
import app.entities.User;
import jakarta.persistence.EntityManager;

public class UserDAO extends EntityManagerDAO<User> {

    public UserDAO(EntityManager em) {
        super(em, User.class);
    }

    // ________________________________________________________

    public User getByUsername(String username) {
        String jpql = "SELECT u FROM User u WHERE u.username = :username";

        return executeQuery(() ->
                em.createQuery(jpql, User.class)
                        .setParameter("username", username)
                        .getSingleResult()
        );
    }

    // ________________________________________________________


}

