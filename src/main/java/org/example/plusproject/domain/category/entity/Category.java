package org.example.plusproject.domain.category.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.plusproject.common.entity.BaseRemovableEntity;
import org.example.plusproject.domain.product.entity.Product;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Category extends BaseRemovableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @Builder
    private Category(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static Category of(String name, String description) {
        return new Category(null, name, description);
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
