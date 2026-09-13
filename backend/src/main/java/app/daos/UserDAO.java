package app.daos;

import app.daos.generic.EntityManagerDAO;
import app.entities.User;
import jakarta.persistence.EntityManager;

public class UserDAO extends EntityManagerDAO<User> {

    public UserDAO(EntityManager em) {
        super(em, User.class);
    }

    // ________________________________________________________

    public User getByEmail(String email) {
        String jpql = "SELECT u FROM User u WHERE u.email = :email";

        return executeQuery(() ->
                em.createQuery(jpql, User.class)
                        .setParameter("email", email)
                        .getSingleResult()
        );
    }

    // ________________________________________________________

    public User findByEmailConfirmationToken(String token) {
        String jpql = "SELECT u FROM User u WHERE u.emailConfirmationToken = :token";

        return executeQuery(() ->
                em.createQuery(jpql, User.class)
                        .setParameter("token", token)
                        .getSingleResult()
        );
    }


}

