package org.raven.mongodb.operation;

import org.raven.commons.data.Entity;
import org.raven.mongodb.EntityMetadata;
import org.raven.mongodb.criteria.CountOptions;
import org.raven.mongodb.criteria.ExistsOptions;
import org.raven.mongodb.criteria.FindOptions;

public interface FindExecutor<TEntity extends Entity<TKey>, TKey, TSingleResult, TListResult, TCountResult, TExistsResult> extends EntityMetadata<TEntity> {

    TSingleResult doFindOne(final FindOptions options);

    TListResult doFindList(final FindOptions options);

    TCountResult doCount(final CountOptions options);

    TExistsResult doExists(final ExistsOptions options);
}
