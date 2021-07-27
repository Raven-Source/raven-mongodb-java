package org.raven.mongodb.repository.reactive;

import com.mongodb.WriteConcern;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 * @since JDK11
 */
public interface ReactiveMongoRepository<TEntity, TKey>
        extends ReactiveMongoReaderRepository<TEntity, TKey> {

//    /**
//     * @return
//     */
//    long createIncId();
//
//    /**
//     * @param inc
//     * @return
//     */
//    long createIncId(long inc);
//
//    /**
//     * @param inc
//     * @param iteration
//     * @return
//     */
//    long createIncId(long inc, int iteration);
//
//    //#region get
//
//    /**
//     * 创建自增ID
//     *
//     * @param entity
//     */
//    void createIncId(TEntity entity);
//
//    /**
//     * 创建ObjectID
//     *
//     * @param entity
//     */
//    void createObjectId(TEntity entity);

    /**
     * @param entity
     */
    Mono<InsertOneResult> insert(TEntity entity);

    /**
     * @param entity
     * @param writeConcern
     */
    Mono<InsertOneResult> insert(TEntity entity, WriteConcern writeConcern);

    /**
     * @param entitys
     */
    Mono<InsertManyResult> insertBatch(List<TEntity> entitys);

    /**
     * @param entitys
     * @param writeConcern
     */
    Mono<InsertManyResult> insertBatch(List<TEntity> entitys, WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, TEntity updateEntity);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @param isUpsert
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, TEntity updateEntity, Boolean isUpsert);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @param isUpsert
     * @param writeConcern
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, TEntity updateEntity, Boolean isUpsert, WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, Bson update);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @param isUpsert
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, Bson update, Boolean isUpsert);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @param isUpsert
     * @param writeConcern
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, Bson update, Boolean isUpsert, WriteConcern writeConcern);

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @return
     */
    Mono<UpdateResult> updateMany(Bson filter, Bson update);

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @param writeConcern
     * @return
     */
    Mono<UpdateResult> updateMany(Bson filter, Bson update, WriteConcern writeConcern);

    /**
     * @param filter
     * @param update
     * @return
     */
    Mono<TEntity> findOneAndUpdate(Bson filter, Bson update);

    /**
     * @param filter
     * @param update
     * @param isUpsert default false
     * @param sort
     * @return
     */
    Mono<TEntity> findOneAndUpdate(Bson filter, Bson update, Boolean isUpsert, Bson sort);

    /**
     * @param filter
     * @param entity
     * @return
     */
    Mono<TEntity> findOneAndUpdate(Bson filter, TEntity entity);

    /**
     * @param filter
     * @param entity
     * @param isUpsert default false
     * @param sort
     * @return
     */
    Mono<TEntity> findOneAndUpdate(Bson filter, TEntity entity, Boolean isUpsert, Bson sort);

    /**
     * @param filter
     * @return
     */
    Mono<TEntity> findOneAndDelete(Bson filter);

    /**
     * @param filter
     * @param sort
     * @return
     */
    Mono<TEntity> findOneAndDelete(Bson filter, Bson sort);

    /**
     * @param id
     * @return
     */
    Mono<DeleteResult> deleteOne(TKey id);

    /**
     * @param id
     * @param writeConcern
     * @return
     */
    Mono<DeleteResult> deleteOne(TKey id, WriteConcern writeConcern);

    /**
     * @param filter
     * @return
     */
    Mono<DeleteResult> deleteOne(Bson filter);

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<DeleteResult> deleteOne(Bson filter, WriteConcern writeConcern);

    /**
     * @param filter
     * @return
     */
    Mono<DeleteResult> deleteMany(Bson filter);

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<DeleteResult> deleteMany(Bson filter, WriteConcern writeConcern);

}
