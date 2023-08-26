package com.lcwd.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "category_id")
    private String categoryId;
    @Column(name = "category_title", length = 60, nullable = false)
    private String title;
    private String description;
    private String coverImage;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
}
