package org.raven.mongodb.criteria;

import lombok.NonNull;
import org.bson.conversions.Bson;
import org.raven.mongodb.EntityMetadata;

public interface SortBuilderAdapter<TEntity, P> extends EntityMetadata<TEntity> {

    P sort(Bson bson);

    default P sort(@NonNull final SortExpression<TEntity> sortExpression) {
        return sort(sortExpression.toBson(getEntityType()));
    }

    default P sort(@NonNull final SortBuilder<?> builder) {
        return sort(builder.build());
    }
}
