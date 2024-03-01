package org.raven.mongodb;

import com.mongodb.client.ClientSession;
import org.raven.commons.data.Entity;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public interface MongoRepository<TEntity extends Entity<TKey>, TKey>
        extends MongoReadonlyRepository<TEntity, TKey>
        , SyncModifyOperation<TEntity, TKey> {

    SyncModifyOperation<TEntity, TKey> modifyWithClientSession(ClientSession clientSession);

}
