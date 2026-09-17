package app.services;

import app.daos.UserDAO;
import app.daos.WeddingDAO;
import app.entities.Category;
import app.entities.User;
import app.entities.Wedding;
import app.enums.Categories;
import app.enums.Notifications;
import app.server.Setup;
import app.utils.ErrorHandler;
import io.javalin.http.Context;

import java.time.LocalDate;
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

    public Wedding createWedding(UUID ownerId, Context ctx) {

        Map<String, String> body = ErrorHandler.tryBodyMap(ctx, Notifications.BODY_EMPTY.getDisplayName());
        User owner = ErrorHandler.tryEntity(userDAO.getById(ownerId), Notifications.WEDDING_LOGIN_REQUIRED.getDisplayName());
        Wedding wedding = new Wedding();
        applyDetails(wedding, body);
        wedding.setOwner(owner);

        int position = 0;
        for (Categories defaultCategory : Categories.values()) {
            wedding.addCategory(Category.builder()
                    .title(defaultCategory.getDisplayName())
                    .position(position++)
                    .build());
        }
        return weddingDAO.create(wedding);
    }

    // ________________________________________________________

    public Wedding updateWedding(UUID id, UUID ownerId, Context ctx) {

        Map<String, String> body = ErrorHandler.tryBodyMap(ctx, Notifications.BODY_EMPTY.getDisplayName());
        Wedding wedding = ErrorHandler.tryEntity(weddingDAO.getByIdAndOwnerId(id, ownerId), Notifications.WEDDING_NOT_FOUND.getDisplayName());
        applyDetails(wedding, body);
        return weddingDAO.update(wedding);
    }

    // ________________________________________________________

    public void deleteWedding(UUID id, UUID ownerId) {
        Wedding wedding = ErrorHandler.tryEntity(weddingDAO.getByIdAndOwnerId(id, ownerId), Notifications.WEDDING_NOT_FOUND.getDisplayName());
        weddingDAO.delete(wedding);
    }

    // ________________________________________________________

    private void applyDetails(Wedding wedding, Map<String, String> body) {
        String title = ErrorHandler.tryString(body.get("title"), Notifications.WEDDING_TITLE_REQUIRED.getDisplayName()).strip();
        String description = ErrorHandler.tryString(body.get("description"), Notifications.WEDDING_DESCRIPTION_REQUIRED.getDisplayName()).strip();
        LocalDate date = ErrorHandler.tryParseLocalDate(body.get("date"), Notifications.WEDDING_DATE_INVALID.getDisplayName());
        float budget = ErrorHandler.tryParseFloat(body.get("budget"), Notifications.MUST_BE_FLOAT.getDisplayName());
        String location = body.get("location");

        wedding.setTitle(title);
        wedding.setDescription(description);
        wedding.setDate(date);
        wedding.setBudget(budget);
        wedding.setLocation(location);
    }
}
