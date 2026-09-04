package app.mappers;

import app.dtos.UserDTO;
import app.entities.User;
import app.mappers.generic.IMapper;


public class UserMapper implements IMapper<User, UserDTO> {

    @Override
    public User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .firstname(dto.getName())
                .lastname(dto.getLastName())
                .username(dto.getUsername())
                .build();
    }

    @Override
    public UserDTO toDTO(User entity) {
        return UserDTO.builder().id(entity.getId())
                .name(entity.getFirstname())
                .lastName(entity.getLastname())
                .username(entity.getUsername())
                .build();
    }
}
