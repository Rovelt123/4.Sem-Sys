package app.entities;

import jakarta.persistence.*;
import lombok.*;
import app.enums.Role;

import java.time.LocalDateTime;
import java.util.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstname;

    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private boolean emailConfirmed = false;

    private String emailConfirmationToken;

    private LocalDateTime emailConfirmationExpiresAt;

    private String passwordResetToken;

    private LocalDateTime passwordResetExpiresAt;

}

