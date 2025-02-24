package org.raven.mongodb.criteria;

import org.bson.conversions.Bson;

import java.util.function.Function;

@FunctionalInterface
public interface SortExpression<T> extends Function<SortBuilder<T>, CriteriaBuilder> {

    default Bson toBson(Class<T> entityType) {
        return apply(SortBuilder.create(entityType)).build();
    }
}
