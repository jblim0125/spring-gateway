package com.jblim.gateway.repository;

import com.jblim.gateway.models.entity.AppPredicate;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AppPredicateRepository extends ReactiveCrudRepository<AppPredicate, UUID> {

    @Query("SELECT * FROM APP_PREDICATE WHERE ROUTE_ID = :#{#routeId}")
    Flux<AppPredicate> findByRouteId(String routeId);

    Mono<Void> deleteByRouteId(String routeId);
}
