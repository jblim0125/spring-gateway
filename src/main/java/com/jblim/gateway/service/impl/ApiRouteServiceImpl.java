package com.jblim.gateway.service.impl;

import com.jblim.gateway.models.dto.CreateOrUpdateApiRouteRequest;
import com.jblim.gateway.models.entity.AppPredicate;
import com.jblim.gateway.models.entity.AppRouteDefinition;
import com.jblim.gateway.repository.ApiRouteRepository;
import com.jblim.gateway.repository.AppPredicateRepository;
import com.jblim.gateway.service.ApiRouteService;
import com.jblim.gateway.service.GatewayRouteService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ApiRouteServiceImpl implements ApiRouteService {

    private final ApiRouteRepository apiRouteRepository;
    private final AppPredicateRepository appPredicateRepository;

    private final GatewayRouteService gatewayRouteService;

    public ApiRouteServiceImpl(ApiRouteRepository apiRouteRepository,
            AppPredicateRepository appPredicateRepository,
            GatewayRouteService gatewayRouteService) {
        this.apiRouteRepository = apiRouteRepository;
        this.appPredicateRepository = appPredicateRepository;
        this.gatewayRouteService = gatewayRouteService;
    }

    @Override
    public Flux<AppRouteDefinition> findApiRoutes() {
        return apiRouteRepository.findAll().flatMap(this::loadPredicates);
    }

    private Mono<AppRouteDefinition> loadPredicates(AppRouteDefinition routeDefinition) {
        return appPredicateRepository.findByRouteId(routeDefinition.getId())
                .collectList()
                .map(predicates -> {
                    routeDefinition.setPredicate(predicates);
                    return routeDefinition;
                });
    }

    @Override
    public Mono<AppRouteDefinition> findApiRoute(String id) {
        return findAndValidateApiRoute(id);
    }

    @Override
    public Mono<Void> createApiRoute(CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
        AppRouteDefinition routeDefinition = setNewApiRoute(new AppRouteDefinition(),
                createOrUpdateApiRouteRequest);
        return apiRouteRepository.save(routeDefinition)
                .then(Mono.defer( () ->
                            Flux.fromIterable(routeDefinition.getPredicate())
                                    .flatMap(appPredicateRepository::save)
                                    .then(Mono.just(routeDefinition))
                        )
                )
                .doOnSuccess(obj -> gatewayRouteService.refreshRoutes())
                .then();
//        return apiRouteRepository.save(routeDefinition)
//                .flatMap(route -> appPredicateRepository.saveAll(route.getPredicate()) )
//        Flux.fromIterable(route.getPredicate())
//                .doOnNext(predicate -> predicate.setRouteId(route.getId()))
//                .map(appPredicateRepository::save)
//                .then(Mono.just(route)))
//                .doOnSuccess(obj -> gatewayRouteService.refreshRoutes())
//                .then();
    }

    @Override
    public Mono<Void> updateApiRoute(String id,
            CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
        return findAndValidateApiRoute(id)
                .map(routeDefinition -> setNewApiRoute(routeDefinition, createOrUpdateApiRouteRequest))
                .flatMap(apiRouteRepository::save)
                .flatMap(route -> appPredicateRepository.saveAll(setNewApiRoute(route, createOrUpdateApiRouteRequest).getPredicate()).then(Mono.just(route)))
                .doOnSuccess(obj -> gatewayRouteService.refreshRoutes())
                .then();
    }

    @Override
    @Transactional
    public Mono<Void> deleteApiRoute(String id) {
        return findAndValidateApiRoute(id)
                .flatMap(routeDefinition -> appPredicateRepository.deleteByRouteId(routeDefinition.getId()))
                .then(Mono.defer(() -> apiRouteRepository.deleteById(id)))
                .doOnSuccess(obj -> gatewayRouteService.refreshRoutes());
    }

    private Mono<AppRouteDefinition> findAndValidateApiRoute(String id) {
        return apiRouteRepository.findById(id)   // .flatMap(this::loadPredicates)
                .switchIfEmpty(Mono.error(
                        new RuntimeException(String.format("Api route with id %s not found", id))));
    }

    private AppRouteDefinition setNewApiRoute(AppRouteDefinition routeDefinition,
            CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
        routeDefinition.setId(createOrUpdateApiRouteRequest.getId());
        routeDefinition.setUri(createOrUpdateApiRouteRequest.getUri());
        List<AppPredicate> appPredicateList = new ArrayList<>();

        if (createOrUpdateApiRouteRequest.getPath() != null &&
                !createOrUpdateApiRouteRequest.getPath().isEmpty()) {
            // Path predicate
            AppPredicate predicate = new AppPredicate();
            HashMap<String, String> args = new HashMap<>();
            predicate.setRouteId(routeDefinition.getId());
            predicate.setName(routeDefinition.getId() + "-path-predicate");
            args.put("patterns", String.join(",", createOrUpdateApiRouteRequest.getPath()));
            predicate.setArgs(args);
            appPredicateList.add(predicate);
        }
        if (createOrUpdateApiRouteRequest.getMethod() != null &&
                !createOrUpdateApiRouteRequest.getMethod().isEmpty()) {
            // Method predicate
            AppPredicate predicate = new AppPredicate();
            HashMap<String, String> args = new HashMap<>();
            predicate.setRouteId(routeDefinition.getId());
            predicate.setName(routeDefinition.getId() + "-method-predicate");
            args.put("methods", String.join(",", createOrUpdateApiRouteRequest.getMethod()));
            predicate.setArgs(args);
            appPredicateList.add(predicate);
        }
        routeDefinition.setPredicate(appPredicateList);
        return routeDefinition;
    }
}
