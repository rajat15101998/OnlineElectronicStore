package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.validation.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class CategoryDto {

    private String categoryId;
    @NotBlank(message = "Title is required")
    @Size(min = 4, message = "Title must be at-least 4 characters")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;
    @ImageNameValid
    private String coverImage;
}
