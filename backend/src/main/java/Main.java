import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "7070"));

        Javalin app = Javalin.create()
                .get("/api/health", ctx -> ctx.result("API OK"));

        app.start(port);
    }
}
