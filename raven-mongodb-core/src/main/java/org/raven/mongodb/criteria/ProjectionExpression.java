package org.raven.mongodb.criteria;

import org.bson.conversions.Bson;

import java.util.function.Function;

@FunctionalInterface
public interface ProjectionExpression<T> extends Function<ProjectionBuilder<T>, CriteriaBuilder> {

    default Bson toBson(Class<T> entityType) {
        return apply(ProjectionBuilder.create(entityType)).build();
    }
}
