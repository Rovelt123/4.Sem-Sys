package app.daos;

import app.daos.generic.EntityManagerDAO;
import app.entities.Task;
import app.entities.Category;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Comparator;
import java.util.UUID;

public class TaskDAO extends EntityManagerDAO<Task> {

    public TaskDAO(EntityManager em) {
        super(em, Task.class);
    }

    // ________________________________________________________

    public List<Task> getAllByCategoryIdAndOwnerId(UUID categoryId, UUID ownerId) {
        String jpql = "SELECT t FROM Task t WHERE t.category.id = :categoryId AND t.category.wedding.owner.id = :ownerId ORDER BY t.position, t.id";

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


    // ________________________________________________________

    public Task moveTask(Task task, Category target, List<Task> targetTasks) {
        return executeQuery(() -> {

            Category source = task.getCategory();

            if (!source.getId().equals(target.getId())) {
                source.getTasks().remove(task);
                target.addTask(task);
                updatePositions(orderedTasks(source));
            }

            updatePositions(targetTasks);
            return update(task);

        });
    }

    // ________________________________________________________

    public void deleteAndUpdatePositions(Task task) {
        executeQuery(() -> {
            task.getCategory().getTasks().remove(task);
            updatePositions(orderedTasks(task.getCategory()));
            delete(task);
            return null;
        });
    }

    // ________________________________________________________

    private List<Task> orderedTasks(Category category) {
        return category.getTasks().stream()
            .sorted(Comparator.comparingInt(Task::getPosition).thenComparing(Task::getId)).toList();
    }

    // ________________________________________________________

    private void updatePositions(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setPosition(i);
        }
    }
}
