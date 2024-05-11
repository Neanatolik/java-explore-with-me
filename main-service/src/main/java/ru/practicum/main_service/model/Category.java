package ru.practicum.main_service.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "categories", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
