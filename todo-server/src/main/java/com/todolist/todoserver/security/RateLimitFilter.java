package com.todolist.todoserver.security;

import com.todolist.todoserver.config.RateLimitProperties;
import com.todolist.todoserver.infra.errors.RateLimitException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final RateLimitProperties rateLimitProperties;
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private final AntPathMatcher matcher = new AntPathMatcher();
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;

    public RateLimitFilter(RateLimitProperties rateLimitProperties, HandlerExceptionResolver handlerExceptionResolver) {
        this.rateLimitProperties = rateLimitProperties;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        RateLimitProperties.ConfigRoutes foundRoute = null;
        String identifiedRoute = "";

        if (rateLimitProperties.getRoutes() != null) {
            for (Map.Entry<String, RateLimitProperties.ConfigRoutes> entry : rateLimitProperties.getRoutes().entrySet()) {
                if (matcher.match(entry.getValue().getPath(), requestURI)) {
                    foundRoute = entry.getValue();
                    identifiedRoute = entry.getKey();
                    break;
                }
            }
        }

        if (foundRoute == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        String cacheKey = ip + ":" + identifiedRoute;

        final RateLimitProperties.ConfigRoutes configFinal =  foundRoute;

        Bucket bucket = cache.computeIfAbsent(cacheKey, k -> {
            Bandwidth limit = Bandwidth.builder()
                    .capacity(configFinal.getCapacity())
                    .refillIntervally(configFinal.getCapacity(), Duration.ofMinutes(configFinal.getTimeMinutes()))
                    .build();

            return Bucket.builder().addLimit(limit).build();
        });

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            handlerExceptionResolver.resolveException(
                    request, response, null, new RateLimitException("Limite de requisicoes excedido para esta funcionalidade")
            );
        }
    }
}
