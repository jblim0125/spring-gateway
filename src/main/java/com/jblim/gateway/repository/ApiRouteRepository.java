package com.jblim.gateway.repository;

import com.jblim.gateway.models.entity.AppRouteDefinition;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ApiRouteRepository extends ReactiveCrudRepository<AppRouteDefinition, String> {

    @Query("INSERT INTO APP_ROUTE_DEFINITION (ID, URI) VALUES (:#{#routeDefinition.id}, :#{#routeDefinition.uri})")
    Mono<AppRouteDefinition> save(AppRouteDefinition routeDefinition);
}
