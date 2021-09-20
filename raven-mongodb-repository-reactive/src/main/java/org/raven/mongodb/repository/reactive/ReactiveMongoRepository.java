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

    /**
     * @param entity TEntity
     */
    Mono<InsertOneResult> insert(TEntity entity);

    /**
     * @param entity       TEntity
     * @param writeConcern WriteConcern
     */
    Mono<InsertOneResult> insert(TEntity entity, WriteConcern writeConcern);

    /**
     * @param entities TEntity
     */
    Mono<InsertManyResult> insertBatch(List<TEntity> entities);

    /**
     * @param entities     TEntity
     * @param writeConcern WriteConcern
     */
    Mono<InsertManyResult> insertBatch(List<TEntity> entities, WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param updateEntity TEntity
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, TEntity updateEntity);

    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param updateEntity TEntity
     * @param isUpsert     Boolean
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, TEntity updateEntity, Boolean isUpsert);

    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param updateEntity TEntity
     * @param isUpsert     Boolean
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, TEntity updateEntity, Boolean isUpsert, WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param updateEntity TEntity
     * @param isUpsert     Boolean
     * @param hint         hint Bson
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, TEntity updateEntity, Boolean isUpsert, Bson hint, WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param filter filter Bson
     * @param update update Bson
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, Bson update);

    /**
     * 修改单条数据
     *
     * @param filter   filter Bson
     * @param update   update Bson
     * @param isUpsert Boolean
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, Bson update, Boolean isUpsert);

    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param update       update Bson
     * @param isUpsert     Boolean
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, Bson update, Boolean isUpsert, WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param update       update Bson
     * @param isUpsert     Boolean
     * @param hint         hint Bson
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<UpdateResult> updateOne(Bson filter, Bson update, Boolean isUpsert, Bson hint, WriteConcern writeConcern);

    /**
     * 修改多条数据
     *
     * @param filter filter Bson
     * @param update update Bson
     * @return
     */
    Mono<UpdateResult> updateMany(Bson filter, Bson update);

    /**
     * 修改多条数据
     *
     * @param filter       filter Bson
     * @param update       update Bson
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<UpdateResult> updateMany(Bson filter, Bson update, WriteConcern writeConcern);

    /**
     * 修改多条数据
     *
     * @param filter       filter Bson
     * @param update       update Bson
     * @param hint         hint Bson
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<UpdateResult> updateMany(Bson filter, Bson update, Bson hint, WriteConcern writeConcern);

    /**
     * @param filter filter Bson
     * @param update update Bson
     * @return
     */
    Mono<TEntity> findOneAndUpdate(Bson filter, Bson update);

    /**
     * @param filter   filter Bson
     * @param update   update Bson
     * @param isUpsert default false
     * @param sort     sort Bson
     * @return
     */
    Mono<TEntity> findOneAndUpdate(Bson filter, Bson update, Boolean isUpsert, Bson sort);

    /**
     * @param filter   filter Bson
     * @param update   update Bson
     * @param isUpsert default false
     * @param sort     sort Bson
     * @param hint     hint Bson
     * @return
     */
    Mono<TEntity> findOneAndUpdate(Bson filter, Bson update, Boolean isUpsert, Bson sort, Bson hint);

    /**
     * @param filter filter Bson
     * @param entity TEntity
     * @return
     */
    Mono<TEntity> findOneAndUpdate(Bson filter, TEntity entity);

    /**
     * @param filter   filter Bson
     * @param entity   TEntity
     * @param isUpsert default false
     * @param sort     sort Bson
     * @return
     */
    Mono<TEntity> findOneAndUpdate(Bson filter, TEntity entity, Boolean isUpsert, Bson sort);

    /**
     * @param filter   filter Bson
     * @param entity   TEntity
     * @param isUpsert default false
     * @param sort     sort Bson
     * @param hint     hint Bson
     * @return
     */
    Mono<TEntity> findOneAndUpdate(Bson filter, TEntity entity, Boolean isUpsert, Bson sort, Bson hint);

    /**
     * @param filter filter Bson
     * @return
     */
    Mono<TEntity> findOneAndDelete(Bson filter);

    /**
     * @param filter filter Bson
     * @param sort   sort Bson
     * @return
     */
    Mono<TEntity> findOneAndDelete(Bson filter, Bson sort);

    /**
     * @param filter filter Bson
     * @param sort   sort Bson
     * @param hint   hint Bson
     * @return
     */
    Mono<TEntity> findOneAndDelete(Bson filter, Bson sort, Bson hint);

    /**
     * @param id TKey
     * @return
     */
    Mono<DeleteResult> deleteOne(TKey id);

    /**
     * @param id           TKey
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<DeleteResult> deleteOne(TKey id, WriteConcern writeConcern);

    /**
     * @param filter
     * @return
     */
    Mono<DeleteResult> deleteOne(Bson filter);

    /**
     * @param filter       filter Bson
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<DeleteResult> deleteOne(Bson filter, WriteConcern writeConcern);

    /**
     * @param filter       filter Bson
     * @param hint         hint Bson
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<DeleteResult> deleteOne(Bson filter, Bson hint, WriteConcern writeConcern);

    /**
     * @param filter filter Bson
     * @return
     */
    Mono<DeleteResult> deleteMany(Bson filter);

    /**
     * @param filter       filter Bson
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<DeleteResult> deleteMany(Bson filter, WriteConcern writeConcern);

    /**
     * @param filter       filter Bson
     * @param hint         hint Bson
     * @param writeConcern WriteConcern
     * @return
     */
    Mono<DeleteResult> deleteMany(Bson filter, Bson hint, WriteConcern writeConcern);

}
