package ru.practicum.hit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHitDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/hit")
@Validated
@Slf4j
public class HitController {
    private final HitClient hitClient;

    @Autowired
    public HitController(HitClient hitClient) {
        this.hitClient = hitClient;
    }

    @PostMapping
    public ResponseEntity<Object> saveHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("POST /hit");
        return hitClient.saveHit(endpointHitDto);
    }
}
