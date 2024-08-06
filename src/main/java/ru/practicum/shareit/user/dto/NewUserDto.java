package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Builder
public class NewUserDto {
    @NotBlank
    private String name;

    @Email
    private String email;
}
