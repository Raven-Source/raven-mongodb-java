package org.raven.mongodb.criteria;

import lombok.NonNull;
import org.bson.conversions.Bson;
import org.raven.mongodb.EntityMetadata;

public interface UpdateBuilderAdapter<TEntity, P> extends EntityMetadata<TEntity> {

    P update(Bson bson);

    default P update(@NonNull final UpdateExpression<TEntity> updateExpression) {
        return update(updateExpression.toBson(getEntityType()));
    }

    default P update(@NonNull final UpdateBuilder<?> updateBuilder) {
        return update(updateBuilder.build());
    }
}
