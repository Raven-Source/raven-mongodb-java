package org.raven.mongodb.criteria;

import org.bson.conversions.Bson;

import java.util.function.Function;

@FunctionalInterface
public interface HintExpression<T> extends Function<HintBuilder<T>, CriteriaBuilder> {

    default Bson toBson(Class<T> entityType) {
        return apply(HintBuilder.create(entityType)).build();
    }
}
