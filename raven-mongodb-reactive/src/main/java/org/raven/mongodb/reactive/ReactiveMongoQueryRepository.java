package org.raven.mongodb.reactive;

import com.mongodb.reactivestreams.client.ClientSession;
import org.raven.commons.data.Entity;

import javax.annotation.Nullable;


/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public interface ReactiveMongoQueryRepository<TEntity extends Entity<TKey>, TKey>
        extends ReactiveMongoBaseRepository<TEntity>,
        ReactiveReadOperation<TEntity, TKey> {

    ReactiveReadOperation<TEntity, TKey> findWithClientSession(@Nullable ClientSession clientSession);

}
