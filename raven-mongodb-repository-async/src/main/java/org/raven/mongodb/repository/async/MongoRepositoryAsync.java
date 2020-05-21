package org.raven.mongodb.repository.async;

import com.mongodb.WriteConcern;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public interface MongoRepositoryAsync<TEntity, TKey>
        extends MongoReaderRepositoryAsync<TEntity, TKey> {

    /**
     * @return
     */
    CompletableFuture<Long> createIncIDAsync();

    /**
     * @param inc
     * @return
     */
    CompletableFuture<Long> createIncIDAsync(long inc);

    //#region get

    /**
     * 创建自增ID
     *
     * @param entity
     */
    CompletableFuture createIncIDAsync(TEntity entity);

    /**
     * 创建ObjectID
     *
     * @param entity
     */
    void createObjectID(TEntity entity);

    /**
     * @param entity
     */
    CompletableFuture insertAsync(TEntity entity);

    /**
     * @param entity
     * @param writeConcern
     */
    CompletableFuture insertAsync(TEntity entity, WriteConcern writeConcern);

    /**
     * @param entitys
     */
    CompletableFuture insertBatchAsync(List<TEntity> entitys);

    /**
     * @param entitys
     * @param writeConcern
     */
    CompletableFuture insertBatchAsync(List<TEntity> entitys, WriteConcern writeConcern);


    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @return
     */
    CompletableFuture<UpdateResult> updateOneAsync(Bson filter, TEntity updateEntity);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @param isUpsert
     * @return
     */
    CompletableFuture<UpdateResult> updateOneAsync(Bson filter, TEntity updateEntity, Boolean isUpsert);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @param isUpsert
     * @param writeConcern
     * @return
     */
    CompletableFuture<UpdateResult> updateOneAsync(Bson filter, TEntity updateEntity, Boolean isUpsert, WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @return
     */
    CompletableFuture<UpdateResult> updateOneAsync(Bson filter, Bson update);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @param isUpsert
     * @return
     */
    CompletableFuture<UpdateResult> updateOneAsync(Bson filter, Bson update, Boolean isUpsert);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @param isUpsert
     * @param writeConcern
     * @return
     */
    CompletableFuture<UpdateResult> updateOneAsync(Bson filter, Bson update, Boolean isUpsert, WriteConcern writeConcern);

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @return
     */
    CompletableFuture<UpdateResult> updateManyAsync(Bson filter, Bson update);

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @param writeConcern
     * @return
     */
    CompletableFuture<UpdateResult> updateManyAsync(Bson filter, Bson update, WriteConcern writeConcern);

    /**
     * @param filter
     * @param update
     * @return
     */
    CompletableFuture<TEntity> findOneAndUpdateAsync(Bson filter, Bson update);

    /**
     * @param filter
     * @param update
     * @param isUpsert default false
     * @param sort
     * @return
     */
    CompletableFuture<TEntity> findOneAndUpdateAsync(Bson filter, Bson update, Boolean isUpsert, Bson sort);

    /**
     * @param filter
     * @param entity
     * @return
     */
    CompletableFuture<TEntity> findOneAndUpdateAsync(Bson filter, TEntity entity);

    /**
     * @param filter
     * @param entity
     * @param isUpsert default false
     * @param sort
     * @return
     */
    CompletableFuture<TEntity> findOneAndUpdateAsync(Bson filter, TEntity entity, Boolean isUpsert, Bson sort);

    /**
     * @param filter
     * @return
     */
    CompletableFuture<TEntity> findOneAndDeleteAsync(Bson filter);

    /**
     * @param filter
     * @param sort
     * @return
     */
    CompletableFuture<TEntity> findOneAndDeleteAsync(Bson filter, Bson sort);

    /**
     * @param id
     * @return
     */
    CompletableFuture<DeleteResult> deleteOneAsync(TKey id);

    /**
     * @param id
     * @param writeConcern
     * @return
     */
    CompletableFuture<DeleteResult> deleteOneAsync(TKey id, WriteConcern writeConcern);

    /**
     * @param filter
     * @return
     */
    CompletableFuture<DeleteResult> deleteOneAsync(Bson filter);

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    CompletableFuture<DeleteResult> deleteOneAsync(Bson filter, WriteConcern writeConcern);

    /**
     * @param filter
     * @return
     */
    CompletableFuture<DeleteResult> deleteManyAsync(Bson filter);

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    CompletableFuture<DeleteResult> deleteManyAsync(Bson filter, WriteConcern writeConcern);
}
