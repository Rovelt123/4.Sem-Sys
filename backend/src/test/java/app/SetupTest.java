package app;

import app.configs.TestHibernateConfig;
import app.daos.TaskDAO;
import app.daos.CategoryDAO;
import app.daos.UserDAO;
import app.daos.WeddingDAO;
import app.entities.Category;
import app.entities.User;
import app.entities.Wedding;
import app.enums.Categories;
import app.enums.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class SetupTest {

    protected static EntityManagerFactory emf;
    protected EntityManager em;

    protected UserDAO userDAO;
    protected WeddingDAO weddingDAO;
    protected CategoryDAO categoryDAO;
    protected TaskDAO taskDAO;

    protected User testUser;
    protected User testUser2;

    protected Wedding wedding;
    protected Wedding wedding2;

    public LocalDate date = LocalDate.of(2026, 12, 24);
    public LocalDate dateTwo = LocalDate.of(2026, 11, 11);

    protected float weddingBudget = 100;

    protected Category category;
    protected Category category2;

    // ________________________________________________________

    @BeforeAll
    static void setupAll() {
        emf = TestHibernateConfig.getTestEmf();
    }

    // ________________________________________________________

    @AfterAll
    static void closeAll() {
        emf.close();
    }

    // ________________________________________________________

    @BeforeEach
    protected void setup() {
        em = emf.createEntityManager();
        em.getTransaction().begin();

        userDAO = new UserDAO(em);
        weddingDAO = new WeddingDAO(em);
        categoryDAO = new CategoryDAO(em);
        taskDAO = new TaskDAO(em);

        testUser = User.builder()
                .firstname("John")
                .lastname("Doe")
                .roles(Set.of(Role.USER))
                .email("john123@test.dk")
                .password("123")
                .build();

        testUser2 = User.builder()
                .firstname("Gert")
                .lastname("Hansen")
                .roles(Set.of(Role.USER))
                .email("testuser2@test.dk")
                .password("123")
                .build();

        wedding = Wedding.builder()
                .title("Our wedding")
                .date(date)
                .location("Lyngby")
                .budget(weddingBudget)
                .build();
        wedding.setOwner(testUser);

        wedding2 = Wedding.builder()
                .title("A wedding")
                .date(dateTwo)
                .location("Virum")
                .budget(weddingBudget)
                .build();
        wedding2.setOwner(testUser);

      category = Category.builder()
                .title(Categories.CATERING.getDisplayName())
                .position(1)
                .wedding(wedding)
                .build();

      category2 = Category.builder()
              .title(Categories.CATERING.getDisplayName())
              .position(2)
              .wedding(wedding2)
              .build();
    }

    // ________________________________________________________

    @AfterEach
    protected void cleanUp() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }
}
