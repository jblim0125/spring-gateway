package com.jblim.gateway.models.entity;

import com.jblim.gateway.models.constant.TableName;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(TableName.APP_ROUTE_DEFINITION)
public class AppRouteDefinition {

    @Id
    @Column("ID")
    private String id;

    @Column("URI")
    private String uri;

    @Transient
    private List<AppPredicate> predicate;

    /*
    private List<AppFilterDefinition> filters = new ArrayList();
    private Map<String, Object> metadata = new HashMap();
    private int order = 0;
    */
}