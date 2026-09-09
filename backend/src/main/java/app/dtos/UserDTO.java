package app.dtos;

import app.enums.Role;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private UUID id;
    private String name;
    private String email;
    private String lastName;
    private Set<Role> roles;

}

