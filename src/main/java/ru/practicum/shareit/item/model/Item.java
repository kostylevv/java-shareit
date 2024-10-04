package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String description;

    @Column(name = "is_available")
    boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;

    @OneToOne
    @JoinColumn(name = "request_id")
    ItemRequest itemRequest;
}
