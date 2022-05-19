package com.guusto.security;

import com.guusto.security.config.RedisHashComponent;
import com.guusto.security.dto.ApiKey;
import com.guusto.security.util.AppConstants;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@EnableCaching
@SpringBootApplication
public class GatewaySecurityApplication {


    @Autowired
    private RedisHashComponent redisHashComponent;

    @PostConstruct
    public void initKeysToRedis() {
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("343C-ED0B-4137-B27E", Stream.of(AppConstants.CARD_SERVICE_KEY,
                AppConstants.BALANCE_SERVICE_KEY).collect(Collectors.toList())));
        apiKeys.add(new ApiKey("FA48-EF0C-427E-8CCF", Stream.of(AppConstants.BALANCE_SERVICE_KEY)
                .collect(Collectors.toList())));
        List<Object> lists = redisHashComponent.hValues(AppConstants.RECORD_KEY);
        if (lists.isEmpty()) {
            apiKeys.forEach(k -> redisHashComponent.hSet(AppConstants.RECORD_KEY, k.getKey(), k));
        }
    }
@Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(AppConstants.CARD_SERVICE_KEY,
						r -> r.path("/guusto-service/buy-gift").and().method(String.valueOf(HttpMethod.POST))
                        .filters(f -> f.setPath("/guusto-service/buy-gift")).uri("http://card-service:8081"))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewaySecurityApplication.class, args);
    }

}
