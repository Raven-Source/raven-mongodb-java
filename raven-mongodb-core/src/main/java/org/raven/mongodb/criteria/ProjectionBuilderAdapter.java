package org.raven.mongodb.criteria;

import lombok.NonNull;
import org.bson.conversions.Bson;
import org.raven.mongodb.EntityMetadata;

public interface ProjectionBuilderAdapter<TEntity, P> extends EntityMetadata<TEntity> {

    P projection(Bson bson);

    default P projection(@NonNull final ProjectionExpression<TEntity> projectionExpression) {
        return projection(projectionExpression.toBson(getEntityType()));
    }

    default P projection(@NonNull final ProjectionBuilder<?> builder) {
        return projection(builder.build());
    }

}
