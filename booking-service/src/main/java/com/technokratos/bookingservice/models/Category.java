package com.technokratos.bookingservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;

    @Column(length = 1000)
    private String categoryDescription;

    @ManyToMany(mappedBy = "categories")
    private List<Event> events;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return categoryId != null && categoryId.equals(category.categoryId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder("Category(")
                .append("categoryId=").append(categoryId)
                .append(", categoryName=").append(categoryName)
                .append(", categoryDescription= ").append(categoryDescription)
                .append(')').toString();
    }
}
