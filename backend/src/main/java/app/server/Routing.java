package app.server;

import app.controllers.CategoryController;
import app.controllers.TaskController;
import app.controllers.UserController;
import app.controllers.WeddingController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routing {

    public static EndpointGroup registerRoutes() {
        return () -> {
            path("/api", () -> {
                UserController.registerRoutes().addEndpoints();
                WeddingController.registerRoutes().addEndpoints();
                CategoryController.registerRoutes().addEndpoints();
                TaskController.registerRoutes().addEndpoints();

                get("/health", ctx -> ctx.result("Health OK"));
            });
        };
    }
}
