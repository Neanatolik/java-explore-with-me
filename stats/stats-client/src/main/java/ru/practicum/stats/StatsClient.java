package ru.practicum.stats;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/stats";

    @Autowired
    public StatsClient(@Value("${stats-client-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getHits(List<String> uris, String start, String end, boolean unique) {
        Map<String, Object> parameters = Map.of("start", encodeValue(start), "end", encodeValue(end), "unique", unique);
        System.out.println("URIS: " + Objects.isNull(uris));
        StringBuilder path = new StringBuilder("?start={start}&end={end}&unique={unique}");
        if (Objects.nonNull(uris)) {
            path.append("&uris=").append(String.join("&uris=", uris));
        }
        System.out.println("PATH: " + path);
        return get(path.toString(), parameters);
    }

    @SneakyThrows
    private String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }
}
