package app.mappers;

import app.dtos.CategoryDTO;
import app.entities.Category;
import app.entities.Task;
import app.mappers.generic.IMapper;

import java.util.Set;
import java.util.Comparator;
import java.util.LinkedHashSet;
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
                .collect(Collectors.toCollection(LinkedHashSet::new));

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
            .taskCount(entity.getTasks().size())
            .completedTaskCount(entity.getTasks().stream().filter(Task::isCompleted).count())
            .totalEstimatedHours(entity.getTasks().stream().mapToDouble(Task::getEstimatedHours).sum())
            .tasks(
                entity.getTasks().stream()
                .sorted(Comparator.comparingInt(Task::getPosition).thenComparing(Task::getId))
                .map(taskMapper::toDTO)
                .collect(Collectors.toCollection(LinkedHashSet::new))
            )
            .build();
    }
}