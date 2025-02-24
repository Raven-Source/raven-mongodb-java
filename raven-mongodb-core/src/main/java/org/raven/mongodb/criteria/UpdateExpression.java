package org.raven.mongodb.criteria;

import org.bson.conversions.Bson;

import java.util.function.Function;

@FunctionalInterface
public interface UpdateExpression<T> extends Function<UpdateBuilder<T>, CriteriaBuilder> {

    default Bson toBson(Class<T> entityType) {
        return apply(UpdateBuilder.create(entityType)).build();
    }
}
