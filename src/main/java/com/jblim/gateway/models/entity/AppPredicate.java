package com.jblim.gateway.models.entity;

import com.jblim.gateway.models.constant.TableName;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableName.APP_PREDICATE)
public class AppPredicate {

    @Column("ROUTE_ID")
    private String routeId;

    @Column("NAME")
    private String name;

    @Column("ARGS")
    private Map<String, String> args;

}
