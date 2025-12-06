package org.raven.mongodb.reactive;

import com.mongodb.reactivestreams.client.ClientSession;
import org.raven.commons.data.Entity;


/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public interface ReactiveMongoRepository<TEntity extends Entity<TKey>, TKey>
        extends ReactiveMongoQueryRepository<TEntity, TKey>
        , ReactiveWriteOperation<TEntity, TKey> {

    ReactiveWriteOperation<TEntity, TKey> modifyWithClientSession(ClientSession clientSession);

}
