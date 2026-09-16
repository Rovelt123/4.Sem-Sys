package app.services;

import app.daos.UserDAO;
import app.daos.WeddingDAO;
import app.dtos.WeddingDTO;
import app.entities.Category;
import app.entities.User;
import app.entities.Wedding;
import app.enums.Categories;
import app.enums.Notifications;
import app.exceptions.ApiException;
import app.mappers.WeddingMapper;
import app.server.Setup;
import app.utils.ErrorHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WeddingService {

    private final WeddingDAO weddingDAO;
    private final UserDAO userDAO;

    // ________________________________________________________

    public WeddingService() {
        this(new WeddingDAO(Setup.em), new UserDAO(Setup.em));
    }

    // ________________________________________________________

    public WeddingService(WeddingDAO weddingDAO, UserDAO userDAO) {
        this.weddingDAO = weddingDAO;
        this.userDAO = userDAO;
    }

    // ________________________________________________________

    public Wedding createWedding(UUID ownerId, Map<String, String> body) {
        String title = ErrorHandler.tryString(body.get("title"), Notifications.WEDDING_TITLE_REQUIRED.getDisplayName()).strip();
        String description = ErrorHandler.tryString(body.get("description"), Notifications.WEDDING_DESCRIPTION_REQUIRED.getDisplayName()).strip();
        LocalDate date = ErrorHandler.tryParseLocalDate(body.get("date"), Notifications.WEDDING_DATE_INVALID.getDisplayName());
        float budget = validateBudget(body.get("budget"));
        User owner = ErrorHandler.tryEntity(userDAO.getById(ownerId), Notifications.WEDDING_LOGIN_REQUIRED.getDisplayName());

        Wedding wedding = Wedding.builder()
            .title(title)
            .description(description)
            .date(date)
            .location(body.get("location"))
            .budget(budget)
            .build();
        wedding.setOwner(owner);

        int position = 0;

        for (Categories defaultCategory : Categories.values()) {

            Category category = Category.builder()
                    .title(defaultCategory.getDisplayName())
                    .position(position++)
                    .build();

            wedding.addCategory(category);
        }

        return weddingDAO.create(wedding);
    }

    // ________________________________________________________

    public Wedding updateWedding(UUID id, UUID ownerId, Map<String, String> body) {
        String title = ErrorHandler.tryString(body.get("title"), Notifications.WEDDING_TITLE_REQUIRED.getDisplayName()).strip();
        String description = ErrorHandler.tryString(body.get("description"), Notifications.WEDDING_DESCRIPTION_REQUIRED.getDisplayName()).strip();
        LocalDate date = ErrorHandler.tryParseLocalDate(body.get("date"), Notifications.WEDDING_DATE_INVALID.getDisplayName());
        float budget = validateBudget(body.get("budget"));
        Wedding existingWedding = ErrorHandler.tryEntity(weddingDAO.getByIdAndOwnerId(id, ownerId), Notifications.WEDDING_NOT_FOUND.getDisplayName());

        Wedding wedding = Wedding.builder()
            .title(title)
            .description(description)
            .date(date)
            .location(body.get("location"))
            .budget(budget)
            .build();
        wedding.setId(existingWedding.getId());
        wedding.setOwner(existingWedding.getOwner());

        return weddingDAO.update(wedding);
    }

    // ________________________________________________________

    public void deleteWedding(UUID id, UUID ownerId) {
        Wedding wedding = ErrorHandler.tryEntity(weddingDAO.getByIdAndOwnerId(id, ownerId), Notifications.WEDDING_NOT_FOUND.getDisplayName());
        weddingDAO.delete(wedding);
    }

    // ________________________________________________________

    private float validateBudget(String value) {
        if (value == null) {
            return 0;
        }

        float budget = ErrorHandler.tryParseFloat(value, Notifications.WEDDING_BUDGET_INVALID.getDisplayName());
        if (budget < 0) {
            throw new ApiException(400, Notifications.WEDDING_BUDGET_RANGE.getDisplayName());
        }
        return budget;
    }

}