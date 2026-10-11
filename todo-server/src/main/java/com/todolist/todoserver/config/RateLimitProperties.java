package com.todolist.todoserver.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "app.rate-limiting")
@Getter
@Setter
@NoArgsConstructor
public class RateLimitProperties {
    private Map<String, ConfigRoutes> routes;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ConfigRoutes {
        private String path;
        private int capacity;
        private int timeMinutes;
    }
}
