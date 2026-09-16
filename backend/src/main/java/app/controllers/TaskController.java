package app.controllers;

import app.controllers.generic.BaseController;
import app.daos.TaskDAO;
import app.dtos.TaskDTO;
import app.entities.Task;
import app.entities.User;
import app.enums.Notifications;
import app.enums.Role;
import app.mappers.TaskMapper;
import app.server.Setup;
import app.services.TaskService;
import app.services.UserService;
import app.utils.ErrorHandler;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TaskController extends BaseController<Task, TaskDTO> {

    private final TaskDAO taskDAO = new TaskDAO(Setup.em);
    private final TaskMapper taskMapper = new TaskMapper();
    private final TaskService taskService = new TaskService();
    private final UserService userService = new UserService();

    // ________________________________________________________

    public TaskController() {
        super(Task.class, new TaskMapper());
    }

    // ________________________________________________________

    public static EndpointGroup registerRoutes() {

        TaskController controller = new TaskController();

        return () -> {
            get("/categories/{categoryId}/tasks", controller::getAllTasks, Role.USER);
            get("/tasks/{id}", controller::getByID, Role.USER);
            post("/categories/{categoryId}/tasks", controller::createTask, Role.USER);
            put("/tasks/{id}", controller::updateTask, Role.USER);
            patch("/tasks/{id}/completed", controller::toggleCompleted, Role.USER);
            delete("/tasks/{id}", controller::deleteTask, Role.USER);
        };
    }

    // ________________________________________________________

    @Override
    protected List<Task> getAllEntities() {
        return taskDAO.getAll();
    }

    // ________________________________________________________

    @Override
    protected Task getEntityById(UUID id) {
        return taskDAO.getById(id);
    }

    // ________________________________________________________

    private void getAllTasks(Context ctx) {

        UUID categoryId = ErrorHandler.tryParseUUID(ctx.pathParam("categoryId"), Notifications.CATEGORY_ID_INVALID.getDisplayName());
        List<TaskDTO> tasks = taskDAO
            .getAllByCategoryIdAndOwnerId(categoryId, userService.getOwnerId(ctx))
            .stream()
            .map(taskMapper::toDTO)
            .toList();

        respond(ctx, 200, Notifications.TASK_GET_ALL.getDisplayName(), Map.of("data", tasks));
    }

    // ________________________________________________________

    private void createTask(Context ctx) {

        UUID categoryId = ErrorHandler.tryParseUUID(ctx.pathParam("categoryId"), Notifications.CATEGORY_ID_INVALID.getDisplayName());

        Map<String, String> body = ErrorHandler.tryBodyMap(ctx, Notifications.BODY_EMPTY.getDisplayName());

        Task task = taskService.createTask(categoryId, userService.getOwnerId(ctx), body);

        respond(ctx, 201, Notifications.TASK_CREATED.getDisplayName(), Map.of("data", taskMapper.toDTO(task)));
    }

    // ________________________________________________________

    private void updateTask(Context ctx) {

        UUID id = ErrorHandler.tryParseUUID(ctx.pathParam("id"), Notifications.TASK_ID_INVALID.getDisplayName());

        Map<String, String> body = ErrorHandler.tryBodyMap(ctx, Notifications.BODY_EMPTY.getDisplayName());

        Task task = taskService.updateTask(id, userService.getOwnerId(ctx), body);

        respond(ctx, 200, Notifications.TASK_UPDATED.getDisplayName(), Map.of("data", taskMapper.toDTO(task)));
    }

    // ________________________________________________________

    private void toggleCompleted(Context ctx) {

        UUID id = ErrorHandler.tryParseUUID(ctx.pathParam("id"), Notifications.TASK_ID_INVALID.getDisplayName());

        Task task = taskService.toggleCompleted(id, userService.getOwnerId(ctx));

        respond(ctx, 200, Notifications.TASK_COMPLETED_UPDATED.getDisplayName(), Map.of("data", taskMapper.toDTO(task)));
    }

    // ________________________________________________________

    private void deleteTask(Context ctx) {

        UUID id = ErrorHandler.tryParseUUID(ctx.pathParam("id"), Notifications.TASK_ID_INVALID.getDisplayName());

        taskService.deleteTask(id, userService.getOwnerId(ctx));

        respond(ctx, 200, Notifications.TASK_DELETED.getDisplayName(), null);
    }
}