package app;

import app.configs.HibernateConfig;
import app.server.Setup;

public class Main {


    public static Setup setup;
    private static final int port = 9292;

    // ________________________________________________________

    public static void main(String[] args) {
        setup = new Setup(HibernateConfig.getEntityManagerFactory().createEntityManager(), port);

        setup.initialize();

        //setup.endSession();
    }
}
