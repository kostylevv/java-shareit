package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;

    private LocalDateTime created;

}
