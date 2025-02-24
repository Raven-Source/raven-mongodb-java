package org.raven.mongodb.criteria;

import lombok.NonNull;
import org.bson.conversions.Bson;
import org.raven.mongodb.EntityMetadata;

public interface FilterBuilderAdapter<TEntity, P> extends EntityMetadata<TEntity> {

    P filter(Bson bson);

    P hint(Bson bson);

    default P filter(@NonNull final FilterExpression<TEntity> filterExpression) {
        return filter(filterExpression.toBson(getEntityType()));
    }

    default P filter(@NonNull final FilterBuilder<?> filterBuilder) {
        return filter(filterBuilder.build());
    }

    default P hint(@NonNull final HintExpression<TEntity> hintExpression) {
        return hint(hintExpression.toBson(getEntityType()));
    }

    default P hint(@NonNull final HintBuilder<?> hintBuilder) {
        return hint(hintBuilder.build());
    }
}
