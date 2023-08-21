package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validation.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {
    private String userId;

    @Size(min = 3, max = 15, message = "Invalid Name !!")
    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 5, max = 15, message = "Password Size should be between 5 to 10 characters")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid Gender !!")
    @NotBlank(message = "Enter Gender")
    private String gender;

    @NotBlank(message = "Write About Yourself")
    private String about;

    @ImageNameValid
    private String imageName;
}
