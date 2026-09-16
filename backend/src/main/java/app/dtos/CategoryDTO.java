package app.dtos;

import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDTO {

    private UUID id;
    private int position;
    private UUID weddingId;
    private String title;

    @Builder.Default
    private Set<TaskDTO> tasks = new HashSet<>();
}