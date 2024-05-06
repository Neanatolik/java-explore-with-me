package ru.practicum.main_service.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class LocationId implements Serializable {
    private Float lat;
    private Float lon;
}
