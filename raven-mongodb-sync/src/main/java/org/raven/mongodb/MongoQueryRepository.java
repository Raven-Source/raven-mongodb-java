package org.raven.mongodb;

import com.mongodb.client.ClientSession;
import org.raven.commons.data.Entity;

/**
 * MongoReaderRepository
 *
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public interface MongoQueryRepository<TEntity extends Entity<TKey>, TKey>
        extends MongoBaseRepository<TEntity>,
        SyncReadOperation<TEntity, TKey> {

    SyncReadOperation<TEntity, TKey> findWithClientSession(ClientSession clientSession);


}
