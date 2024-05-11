package ru.practicum.main_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    private Set<Long> events;
    private Boolean pinned;
    @Length(max = 50)
    private String title;

}
