package app.mappers;

import app.dtos.TaskDTO;
import app.entities.Task;
import app.mappers.generic.IMapper;

public class TaskMapper implements IMapper<Task, TaskDTO> {

    @Override
    public Task toEntity(TaskDTO dto) {
        return Task.builder()
            .id(dto.getId())
            .position(dto.getPosition())
            .title(dto.getTitle())
            .priority(dto.getPriority())
            .description(dto.getDescription())
            .price(dto.getPrice())
            .build();
    }

    // ________________________________________________________

    @Override
    public TaskDTO toDTO(Task entity) {
        return TaskDTO.builder()
            .id(entity.getId())
            .categoryId(
                entity.getCategory() != null
                ? entity.getCategory().getId()
                : null
            )
            .position(entity.getPosition())
            .title(entity.getTitle())
            .priority(entity.getPriority())
            .description(entity.getDescription())
            .price(entity.getPrice())
            .build();
    }
}