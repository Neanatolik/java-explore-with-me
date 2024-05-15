package ru.practicum.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.hit.service.HitService;

@RestController
@RequestMapping(path = "/hit")
@RequiredArgsConstructor
@Slf4j
public class HitController {

    private final HitService hitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto saveHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("POST /hit");
        return hitService.saveHit(endpointHitDto);
    }
}
