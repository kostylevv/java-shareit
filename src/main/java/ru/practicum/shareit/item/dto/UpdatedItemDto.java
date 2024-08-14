package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatedItemDto {
    @NotNull
    @Positive
    private long id;

    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
    private long itemRequestId;
}
