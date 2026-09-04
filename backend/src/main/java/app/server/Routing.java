package app.server;

import app.controllers.UserController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routing {

    public static EndpointGroup registerRoutes() {
        return () -> {
            path("/api", () -> {
                UserController.registerRoutes().addEndpoints();

                get("/health", ctx -> ctx.result("Health OK"));
            });
        };
    }
}
