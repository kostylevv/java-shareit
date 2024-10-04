package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewItemDto {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    @NotNull
    @PositiveOrZero
    private long userId;

    private long itemRequestId;
}