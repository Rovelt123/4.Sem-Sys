package app.daos;

import app.daos.generic.EntityManagerDAO;
import app.entities.Category;
import app.entities.Task;
import app.enums.Categories;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Comparator;
import java.util.UUID;

public class CategoryDAO extends EntityManagerDAO<Category> {

    public CategoryDAO(EntityManager em) {
        super(em, Category.class);
    }

    // ________________________________________________________

    public List<Category> getAllByWeddingIdAndOwnerId(UUID weddingId, UUID ownerId) {
        String jpql = "SELECT c FROM Category c WHERE c.wedding.id = :weddingId AND c.wedding.owner.id = :ownerId ORDER BY c.position, c.id";

        return executeQuery(() ->
            em.createQuery(jpql, Category.class)
                .setParameter("weddingId", weddingId)
                .setParameter("ownerId", ownerId)
                .getResultList()
        );
    }

    // ________________________________________________________

    public Category getByIdAndOwnerId(UUID id, UUID ownerId) {
        String jpql = "SELECT c FROM Category c WHERE c.id = :id AND wedding.owner.id = :ownerId";

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


    // ________________________________________________________

    public void updatePositions(List<Category> categories) {
        executeQuery(() -> {
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                category.setPosition(i);
                update(category);
            }
            return null;
        });
    }

    // ________________________________________________________

    public void deleteAndMoveTasks(Category category, Category uncategorized) {
        executeQuery(() -> {

            if (uncategorized.getId() == null) {
                category.getWedding().addCategory(uncategorized);
                create(uncategorized);
            }

            int position = uncategorized.getTasks().stream().mapToInt(Task::getPosition).max().orElse(-1) + 1;

            for (Task task : category.getTasks().stream()
                .sorted(Comparator.comparingInt(Task::getPosition).thenComparing(Task::getId)).toList()) {
                category.getTasks().remove(task);
                task.setPosition(position++);
                uncategorized.addTask(task);
            }

            category.getWedding().getCategories().remove(category);

            List<Category> remaining = category.getWedding().getCategories().stream()
                .sorted(Comparator.comparingInt(Category::getPosition).thenComparing(Category::getId)).toList();

            updatePositions(remaining);
            delete(category);

            return null;
        });
    }
}
