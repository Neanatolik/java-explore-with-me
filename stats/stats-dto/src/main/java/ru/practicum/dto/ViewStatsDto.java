package ru.practicum.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto implements ViewStatsDtoIn {
    private String app;
    private String uri;
    private Integer hits;
}
