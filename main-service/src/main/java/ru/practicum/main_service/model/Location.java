package ru.practicum.main_service.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "locations", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float lat;
    private Float lon;
}
