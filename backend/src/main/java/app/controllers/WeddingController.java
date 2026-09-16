package app.controllers;

import app.controllers.generic.BaseController;
import app.daos.WeddingDAO;
import app.dtos.WeddingDTO;
import app.entities.User;
import app.entities.Wedding;
import app.enums.Notifications;
import app.enums.Role;
import app.mappers.WeddingMapper;
import app.server.Setup;
import app.services.UserService;
import app.services.WeddingService;
import app.utils.ErrorHandler;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.javalin.apibuilder.ApiBuilder.*;

public class WeddingController extends BaseController<Wedding, WeddingDTO> {

    private final WeddingDAO weddingDAO = new WeddingDAO(Setup.em);
    private final WeddingMapper weddingMapper = new WeddingMapper();
    private final WeddingService weddingService = new WeddingService();
    private final UserService userService = new UserService();

    // ________________________________________________________

    public WeddingController() {
        super(Wedding.class, new WeddingMapper());
    }

    // ________________________________________________________

    public static EndpointGroup registerRoutes() {
        WeddingController controller = new WeddingController();
        return () -> {
            get("/weddings", controller::getAll, Role.USER);
            get("/weddings/{id}", controller::getByID, Role.USER);
            post("/weddings", controller::createWedding, Role.USER);
            put("/weddings/{id}", controller::updateWedding, Role.USER);
            delete("/weddings/{id}", controller::deleteWedding, Role.USER);
        };
    }

    // ________________________________________________________

    @Override
    protected List<Wedding> getAllEntities() {
        return weddingDAO.getAll();
    }

    // ________________________________________________________

    @Override
    protected Wedding getEntityById(UUID id) {
        return weddingDAO.getById(id);
    }

    // ________________________________________________________

    private void createWedding(Context ctx) {

        Map<String, String> body = ErrorHandler.tryBodyMap(ctx, Notifications.BODY_EMPTY.getDisplayName());

        Wedding wedding = weddingService.createWedding(userService.getOwnerId(ctx), body);
        WeddingDTO dto = weddingMapper.toDTO(wedding);

        respond(ctx, 201, Notifications.WEDDING_CREATED.getDisplayName(), Map.of("data", dto));
    }

    // ________________________________________________________

    private void updateWedding(Context ctx) {
        UUID id = ErrorHandler.tryParseUUID(ctx.pathParam("id"), Notifications.WEDDING_ID_INVALID.getDisplayName());
        Map<String, String> body = ErrorHandler.tryBodyMap(ctx, Notifications.BODY_EMPTY.getDisplayName());

        Wedding wedding = weddingService.updateWedding(id, userService.getOwnerId(ctx), body);
        WeddingDTO dto = weddingMapper.toDTO(wedding);

        respond(ctx, 200, Notifications.WEDDING_UPDATED.getDisplayName(), Map.of("data", dto));
    }

    // ________________________________________________________

    private void deleteWedding(Context ctx) {
        UUID id = ErrorHandler.tryParseUUID(ctx.pathParam("id"), Notifications.WEDDING_ID_INVALID.getDisplayName());

        weddingService.deleteWedding(id, userService.getOwnerId(ctx));

        respond(ctx, 200, Notifications.WEDDING_DELETED.getDisplayName(), null);
    }

}
