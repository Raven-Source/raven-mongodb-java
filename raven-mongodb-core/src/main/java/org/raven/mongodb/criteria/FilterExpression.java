package org.raven.mongodb.criteria;

import org.bson.conversions.Bson;

import java.util.function.Function;

@FunctionalInterface
public interface FilterExpression<T> extends Function<FilterBuilder<T>, CriteriaBuilder> {

    default Bson toBson(Class<T> entityType) {
        return apply(FilterBuilder.create(entityType)).build();
    }

}
