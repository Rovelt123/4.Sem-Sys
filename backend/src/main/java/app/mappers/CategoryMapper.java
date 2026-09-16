package app.mappers;

import app.dtos.CategoryDTO;
import app.entities.Category;
import app.entities.Task;
import app.mappers.generic.IMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class CategoryMapper implements IMapper<Category, CategoryDTO> {

    private final TaskMapper taskMapper = new TaskMapper();

    @Override
    public Category toEntity(CategoryDTO dto) {

        Category category = Category.builder()
            .id(dto.getId())
            .position(dto.getPosition())
            .title(dto.getTitle())
            .build();

        if (dto.getTasks() != null) {
            Set<Task> tasks = dto.getTasks().stream()
                .map(taskMapper::toEntity)
                .collect(Collectors.toSet());

            tasks.forEach(task -> task.setCategory(category));

            category.setTasks(tasks);
        }

        return category;
    }

    // ________________________________________________________

    @Override
    public CategoryDTO toDTO(Category entity) {
        return CategoryDTO.builder()
            .id(entity.getId())
            .weddingId(
                entity.getWedding() != null
                ? entity.getWedding().getId()
                : null
            )
            .position(entity.getPosition())
            .title(entity.getTitle())
            .tasks(
                entity.getTasks().stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toSet())
            )
            .build();
    }
}