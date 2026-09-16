package app.controllers;

import app.controllers.generic.BaseController;
import app.daos.CategoryDAO;
import app.dtos.CategoryDTO;
import app.entities.Category;
import app.entities.User;
import app.enums.Notifications;
import app.enums.Role;
import app.mappers.CategoryMapper;
import app.server.Setup;
import app.services.CategoryService;
import app.services.UserService;
import app.utils.ErrorHandler;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CategoryController extends BaseController<Category, CategoryDTO> {

    private final CategoryDAO categoryDAO = new CategoryDAO(Setup.em);
    private final CategoryMapper categoryMapper = new CategoryMapper();
    private final CategoryService categoryService = new CategoryService();
    private final UserService userService = new UserService();

    // ________________________________________________________

    public CategoryController() {
        super(Category.class, new CategoryMapper());
    }

    // ________________________________________________________

    public static EndpointGroup registerRoutes() {

        CategoryController controller = new CategoryController();

        return () -> {
            get("/weddings/{weddingId}/categories", controller::getAllCategories, Role.USER);
            get("/categories/{id}", controller::getByID, Role.USER);
            post("/weddings/{weddingId}/categories", controller::createCategory, Role.USER);
            put("/categories/{id}", controller::updateCategory, Role.USER);
            delete("/categories/{id}", controller::deleteCategory, Role.USER);
        };
    }

    // ________________________________________________________

    @Override
    protected List<Category> getAllEntities() {
        return categoryDAO.getAll();
    }

    // ________________________________________________________

    @Override
    protected Category getEntityById(UUID id) {
        return categoryDAO.getById(id);
    }

    // ________________________________________________________

    private void getAllCategories(Context ctx) {

        UUID weddingId = ErrorHandler.tryParseUUID(ctx.pathParam("weddingId"), Notifications.WEDDING_ID_INVALID.getDisplayName());

        List<CategoryDTO> categories = categoryDAO
            .getAllByWeddingIdAndOwnerId(weddingId, userService.getOwnerId(ctx))
            .stream()
            .map(categoryMapper::toDTO)
            .toList();

        respond(ctx, 200, Notifications.CATEGORY_GET_ALL.getDisplayName(),
        Map.of("data", categories));
    }

    // ________________________________________________________

    private void createCategory(Context ctx) {

        UUID weddingId = ErrorHandler.tryParseUUID(ctx.pathParam("weddingId"), Notifications.WEDDING_ID_INVALID.getDisplayName());

        Map<String, String> body = ErrorHandler.tryBodyMap(ctx, Notifications.BODY_EMPTY.getDisplayName());

        Category category = categoryService.createCategory(weddingId, userService.getOwnerId(ctx), body);

        respond(ctx, 201, Notifications.CATEGORY_CREATED.getDisplayName(), Map.of("data", categoryMapper.toDTO(category)));
    }

    // ________________________________________________________

    private void updateCategory(Context ctx) {

        UUID id = ErrorHandler.tryParseUUID(ctx.pathParam("id"), Notifications.CATEGORY_ID_INVALID.getDisplayName());

        Map<String, String> body = ErrorHandler.tryBodyMap(ctx, Notifications.BODY_EMPTY.getDisplayName());

        Category category = categoryService.updateCategory(id, userService.getOwnerId(ctx), body);

        respond(ctx, 200, Notifications.CATEGORY_UPDATED.getDisplayName(), Map.of("data", categoryMapper.toDTO(category)));
    }

    // ________________________________________________________

    private void deleteCategory(Context ctx) {

        UUID id = ErrorHandler.tryParseUUID(ctx.pathParam("id"), Notifications.CATEGORY_ID_INVALID.getDisplayName());

        categoryService.deleteCategory(id,userService.getOwnerId(ctx));

        respond(ctx, 200, Notifications.CATEGORY_DELETED.getDisplayName(), null);
    }
}