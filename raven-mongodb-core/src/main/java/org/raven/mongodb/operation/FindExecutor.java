package org.raven.mongodb.operation;

import org.raven.mongodb.CountOptions;
import org.raven.mongodb.EntityInformation;
import org.raven.mongodb.ExistsOptions;
import org.raven.mongodb.FindOptions;

public interface FindExecutor<TEntity, TKey, TSingleResult, TListResult, TCountResult, TExistsResult> {

    EntityInformation<TEntity, TKey> getEntityInformation();

    TSingleResult doFindOne(final FindOptions options);

    TListResult doFindList(final FindOptions options);

    TCountResult doCount(final CountOptions options);

    TExistsResult doExists(final ExistsOptions options);
}
