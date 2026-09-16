package app.services;

import app.daos.CategoryDAO;
import app.daos.WeddingDAO;
import app.entities.Category;
import app.entities.Task;
import app.entities.Wedding;
import app.enums.Categories;
import app.enums.Notifications;
import app.exceptions.ApiException;
import app.server.Setup;
import app.utils.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CategoryService {

    private final CategoryDAO categoryDAO;
    private final WeddingDAO weddingDAO;

    // ________________________________________________________

    public CategoryService() {
        this(new CategoryDAO(Setup.em), new WeddingDAO(Setup.em));
    }

    // ________________________________________________________

    public CategoryService(CategoryDAO categoryDAO, WeddingDAO weddingDAO) {
        this.categoryDAO = categoryDAO;
        this.weddingDAO = weddingDAO;
    }

    // ________________________________________________________

    public Category createCategory(UUID weddingId, UUID ownerId, Map<String, String> body) {

        String title = ErrorHandler.tryString(body.get("title"), Notifications.CATEGORY_TITLE_REQUIRED.getDisplayName());

        Wedding wedding = ErrorHandler.tryEntity(weddingDAO.getByIdAndOwnerId(weddingId, ownerId), Notifications.WEDDING_NOT_FOUND.getDisplayName());

        int position = categoryDAO.getAllByWeddingIdAndOwnerId(weddingId, ownerId).size();

        Category category = Category.builder()
            .title(title)
            .position(position)
            .wedding(wedding)
            .build();

        return categoryDAO.create(category);
    }

    // ________________________________________________________

    public Category updateCategory(UUID id, UUID ownerId, Map<String, String> body) {

        Category category = ErrorHandler.tryEntity(categoryDAO.getByIdAndOwnerId(id, ownerId), Notifications.CATEGORY_NOT_FOUND.getDisplayName());

        String title = ErrorHandler.tryString(body.get("title"), Notifications.CATEGORY_TITLE_REQUIRED.getDisplayName());

        category.setTitle(title);

        return categoryDAO.update(category);
    }

    // ________________________________________________________

    public void deleteCategory(UUID id, UUID ownerId) {

        Category category = ErrorHandler.tryEntity(categoryDAO.getByIdAndOwnerId(id, ownerId), Notifications.CATEGORY_NOT_FOUND.getDisplayName());

        if (Categories.UNCATEGORIZED.getDisplayName().equals(category.getTitle())) {
            throw new ApiException(400, Notifications.CATEGORY_UNCATEGORIZED_DELETE.getDisplayName());
        }

        Category uncategorized = ErrorHandler.tryEntity(
            categoryDAO.getUncategorized(category.getWedding().getId(), ownerId),
            Notifications.CATEGORY_UNCATEGORIZED_NOT_FOUND.getDisplayName()
        );

        List<Task> tasks = new ArrayList<>(category.getTasks());

        for (Task task : tasks) {
            category.getTasks().remove(task);
            uncategorized.addTask(task);
        }

        categoryDAO.update(uncategorized);
        categoryDAO.delete(category);
    }
}