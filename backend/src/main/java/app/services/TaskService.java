package app.services;

import app.daos.CategoryDAO;
import app.daos.TaskDAO;
import app.entities.Category;
import app.entities.Task;
import app.enums.Notifications;
import app.enums.Priority;
import app.exceptions.ApiException;
import app.server.Setup;
import app.utils.ErrorHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaskService {

    private final TaskDAO taskDAO;
    private final CategoryDAO categoryDAO;

    // ________________________________________________________

    public TaskService() {
        this(new TaskDAO(Setup.em), new CategoryDAO(Setup.em));
    }

    // ________________________________________________________

    public TaskService(TaskDAO taskDAO, CategoryDAO categoryDAO) {
        this.taskDAO = taskDAO;
        this.categoryDAO = categoryDAO;
    }

    // ________________________________________________________

    public Task createTask(UUID categoryId, UUID ownerId, Map<String, String> body) {

        Category category = ErrorHandler.tryEntity(categoryDAO.getByIdAndOwnerId(categoryId, ownerId), Notifications.CATEGORY_NOT_FOUND.getDisplayName());

        String title = ErrorHandler.tryString(body.get("title"), Notifications.TASK_TITLE_REQUIRED.getDisplayName());

        float price = ErrorHandler.tryParseFloat(body.get("price"),Notifications.MUST_BE_FLOAT.getDisplayName());

        String link = body.get("link");

        LocalDate deadline = ErrorHandler.tryParseLocalDate(body.get("deadline"), Notifications.TASK_DEADLINE_INVALID.getDisplayName());

        float estimatedHours = validateEstimatedHours(body.get("estimatedHours"));

        Priority priority = body.get("priority") == null
            ? Priority.LOW
            : ErrorHandler.tryParseEnum(Priority.class, body.get("priority"), Notifications.TASK_PRIORITY_INVALID.getDisplayName()
        );

        int position = taskDAO.getAllByCategoryIdAndOwnerId(categoryId, ownerId).size();

        Task task = Task.builder()
            .title(title)
            .description(body.get("description"))
            .link(link)
            .price(price)
            .deadline(deadline)
            .estimatedHours(estimatedHours)
            .priority(priority)
            .position(position)
            .completed(false)
            .category(category)
            .build();

        return taskDAO.create(task);
    }

    // ________________________________________________________

    public Task updateTask(UUID id, UUID ownerId, Map<String, String> body) {

        Task task = ErrorHandler.tryEntity(taskDAO.getByIdAndOwnerId(id, ownerId), Notifications.TASK_NOT_FOUND.getDisplayName());

        String title = ErrorHandler.tryString(body.get("title"), Notifications.TASK_TITLE_REQUIRED.getDisplayName());

        task.setTitle(title);
        task.setDescription(body.get("description"));
        task.setLink(body.get("link"));
        task.setPrice(ErrorHandler.tryParseFloat(body.get("price"), Notifications.TASK_PRICE_INVALID.getDisplayName()));
        task.setDeadline(ErrorHandler.tryParseLocalDate(body.get("deadline"), Notifications.WEDDING_DATE_INVALID.getDisplayName()));
        task.setEstimatedHours(validateEstimatedHours(body.get("estimatedHours")));

        if (body.get("priority") != null) {
            task.setPriority(ErrorHandler.tryParseEnum(Priority.class, body.get("priority"), Notifications.TASK_PRIORITY_INVALID.getDisplayName()));
        }

        return taskDAO.update(task);
    }

    // ________________________________________________________

    public Task toggleCompleted(UUID id, UUID ownerId) {

        Task task = ErrorHandler.tryEntity(taskDAO.getByIdAndOwnerId(id, ownerId), Notifications.TASK_NOT_FOUND.getDisplayName());

        task.setCompleted(!task.isCompleted());

        return taskDAO.update(task);
    }

    // ________________________________________________________

    public void deleteTask(UUID id, UUID ownerId) {
        Task task = ErrorHandler.tryEntity(taskDAO.getByIdAndOwnerId(id, ownerId), Notifications.TASK_NOT_FOUND.getDisplayName());
        taskDAO.delete(task);
    }

    // ________________________________________________________

    private float validateEstimatedHours(String value) {

        if (value == null || value.isBlank()) {
            return 0;
        }

        float hours = ErrorHandler.tryParseFloat(value, Notifications.TASK_ESTIMATE_INVALID.getDisplayName());

        if (hours < 0) {
            throw new ApiException(400, Notifications.TASK_ESTIMATE_RANGE.getDisplayName());
        }

        return hours;
    }


}