package app.security;

import app.entities.User;
import app.enums.Role;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.RouteRole;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SecurityServiceTest {

    private final SecurityService securityService = new SecurityService();

    @Test
    void authorizeShouldReturnTrueWhenUserHasAllowedRole() {
        User user = User.builder()
                .roles(Set.of(Role.USER))
                .build();

        Set<RouteRole> allowedRoles = Set.of(Role.USER);

        assertTrue(securityService.authorize(user, allowedRoles));
    }

    @Test
    void authorizeShouldReturnFalseWhenUserDoesNotHaveAllowedRole() {
        User user = User.builder()
                .roles(Set.of(Role.USER))
                .build();

        Set<RouteRole> allowedRoles = Set.of(Role.ADMIN);

        assertFalse(securityService.authorize(user, allowedRoles));
    }

    @Test
    void authorizeShouldAlwaysAllowOwner() {
        User user = User.builder()
                .roles(Set.of(Role.OWNER))
                .build();

        Set<RouteRole> allowedRoles = Set.of(Role.ADMIN);

        assertTrue(securityService.authorize(user, allowedRoles));
    }

    @Test
    void authorizeShouldThrowWhenUserIsNull() {
        assertThrows(
                UnauthorizedResponse.class,
                () -> securityService.authorize(null, Set.of(Role.USER))
        );
    }
}
