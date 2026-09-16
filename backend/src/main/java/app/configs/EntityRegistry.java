package app.configs;


import app.entities.*;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Wedding.class);
        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(Task.class);
    }
}
