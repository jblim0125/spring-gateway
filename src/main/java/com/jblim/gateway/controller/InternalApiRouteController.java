package com.jblim.gateway.controller;

import com.jblim.gateway.models.constant.ApiPath;
import com.jblim.gateway.models.dto.ApiRouteResponse;
import com.jblim.gateway.models.dto.CreateOrUpdateApiRouteRequest;
import com.jblim.gateway.models.entity.AppPredicate;
import com.jblim.gateway.models.entity.AppRouteDefinition;
import com.jblim.gateway.service.ApiRouteService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping(path = ApiPath.INTERNAL_API_ROUTES)
public class InternalApiRouteController {

    private final ApiRouteService apiRouteService;

    public InternalApiRouteController(ApiRouteService apiRouteService) {
        this.apiRouteService = apiRouteService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ApiRouteResponse>> findApiRoutes() {
        return apiRouteService.findApiRoutes()
                .map(this::convertApiRouteIntoApiRouteResponse)
                .collectList()
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiRouteResponse> findApiRoute(@PathVariable String id) {
        return apiRouteService.findApiRoute(id)
                .map(this::convertApiRouteIntoApiRouteResponse)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<?> createApiRoute(
            @RequestBody @Validated CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
        return apiRouteService.createApiRoute(createOrUpdateApiRouteRequest)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<?> updateApiRoute(@PathVariable String id,
            @RequestBody @Validated CreateOrUpdateApiRouteRequest createOrUpdateApiRouteRequest) {
        return apiRouteService.updateApiRoute(id, createOrUpdateApiRouteRequest)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<?> deleteApiRoute(@PathVariable String id) {
        return apiRouteService.deleteApiRoute(id)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private ApiRouteResponse convertApiRouteIntoApiRouteResponse(AppRouteDefinition apiRoute) {
        List<String> path = new ArrayList<>();
        List<String> method = new ArrayList<>();
        for( AppPredicate predicate : apiRoute.getPredicate()) {
            for( String key : predicate.getArgs().keySet()) {
                if( key.equals("patterns")) {
                    path.add(predicate.getArgs().get(key));
                } else if( key.equals("methods")) {
                    method.add(predicate.getArgs().get(key));
                }
            }
        }
        return ApiRouteResponse.builder()
                .id(apiRoute.getId())
                .path(path)
                .method(method)
                .uri(apiRoute.getUri().toString())
                .build();
    }
}
