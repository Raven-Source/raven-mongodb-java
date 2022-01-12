package org.raven.mongodb.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.operation.ModifyOperation;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public interface MongoRepository<TEntity extends Entity<TKey>, TKey>
        extends MongoReaderRepository<TEntity, TKey>
        , ModifyOperation<TEntity, TKey, InsertOneResult, InsertManyResult, UpdateResult, TEntity, DeleteResult> {

}
