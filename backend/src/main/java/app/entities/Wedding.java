package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weddings")
@Getter
@Setter
public class Wedding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, updatable = false)
    private User owner;

    @Column(nullable = false)
    private String title;

    @Column(name = "wedding_date", nullable = false)
    private LocalDate date;

    private String location;

    private float budget;

    private String description;

    @Builder.Default
    @OneToMany(
            mappedBy = "wedding",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        categories.add(category);
        category.setWedding(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }
}
