package com.jblim.gateway.service.impl;

import com.jblim.gateway.models.entity.AppPredicate;
import com.jblim.gateway.models.entity.AppRouteDefinition;
import com.jblim.gateway.service.ApiRouteService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class ApiPathRouteLocatorImpl implements RouteLocator {

    private final ApiRouteService apiRouteService;

    private final RouteLocatorBuilder routeLocatorBuilder;

    @Override
    public Flux<Route> getRoutes() {
        Builder routesBuilder = routeLocatorBuilder.routes();
        return apiRouteService.findApiRoutes()
                .map(routeDefinition ->
                        routesBuilder.route(routeDefinition.getId(),
                                predicateSpec -> setPredicateSpec(routeDefinition, predicateSpec)))
                .collectList()
                .flatMapMany(builders -> routesBuilder.build()
                        .getRoutes());
    }

    private Buildable<Route> setPredicateSpec(AppRouteDefinition routeDefinition,
            PredicateSpec predicateSpec) {
        BooleanSpec booleanSpec = null;
        List<AppPredicate> predicates = routeDefinition.getPredicate();
        if (predicates != null && !predicates.isEmpty()) {
            // Path
            for (AppPredicate appPredicate : predicates) {
                String path = null;
                if (appPredicate.getName().contains("path")) {
                    path = appPredicate.getArgs().get("patterns");
                }
                if (path == null) {
                    continue;
                }
                if (booleanSpec == null) {
                    booleanSpec = predicateSpec.path(path);
                    continue;
                }
                booleanSpec.or().path(path);
            }
            // Method
            for (AppPredicate appPredicate : predicates) {
                String methods = null;
                if (appPredicate.getName().contains("method")) {
                    methods = appPredicate.getArgs().get("methods");
                }
                if (methods == null) {
                    continue;
                }
                if (booleanSpec == null) {
                    booleanSpec = predicateSpec.method(methods);
                    continue;
                }
                booleanSpec.and().method(methods);
            }
        }
        if (booleanSpec != null) {
            return booleanSpec.uri(routeDefinition.getUri());
        }
        return null;
    }
}
