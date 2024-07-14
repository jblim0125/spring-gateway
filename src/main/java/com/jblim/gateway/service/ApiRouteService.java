package com.jblim.gateway.service;

import com.jblim.gateway.models.dto.CreateOrUpdateApiRouteRequest;
import com.jblim.gateway.models.entity.AppRouteDefinition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApiRouteService {

    Flux<AppRouteDefinition> findApiRoutes();

    Mono<AppRouteDefinition> findApiRoute(String id);

    Mono<Void> createApiRoute(CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest);

    Mono<Void> updateApiRoute(String id,
            CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest);

    Mono<Void> deleteApiRoute(String id);
}
