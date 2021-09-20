package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 * @since JDK11
 */
public interface MongoRepository<TEntity, TKey>
        extends MongoReaderRepository<TEntity, TKey> {

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
     * @param entity TEntity
     */
    InsertOneResult insert(TEntity entity);

    /**
     * @param entity       TEntity
     * @param writeConcern WriteConcern
     */
    InsertOneResult insert(TEntity entity, WriteConcern writeConcern);

    /**
     * @param entities TEntity
     */
    InsertManyResult insertBatch(List<TEntity> entities);

    /**
     * @param entities     TEntity
     * @param writeConcern WriteConcern
     */
    InsertManyResult insertBatch(List<TEntity> entities, WriteConcern writeConcern);


    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param updateEntity TEntity
     * @return
     */
    UpdateResult updateOne(Bson filter, TEntity updateEntity);

    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param updateEntity TEntity
     * @param isUpsert     default false
     * @return
     */
    UpdateResult updateOne(Bson filter, TEntity updateEntity, Boolean isUpsert);

    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param updateEntity TEntity
     * @param isUpsert     default false
     * @param writeConcern WriteConcern
     * @return
     */
    UpdateResult updateOne(Bson filter, TEntity updateEntity, Boolean isUpsert, WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param updateEntity TEntity
     * @param isUpsert     default false
     * @param hint         hint Bson
     * @param writeConcern WriteConcern
     * @return
     */
    UpdateResult updateOne(Bson filter, TEntity updateEntity, Boolean isUpsert, Bson hint, WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param filter filter Bson
     * @param update update Bson
     * @return
     */
    UpdateResult updateOne(Bson filter, Bson update);

    /**
     * 修改单条数据
     *
     * @param filter   filter Bson
     * @param update   update Bson
     * @param isUpsert default false
     * @return
     */
    UpdateResult updateOne(Bson filter, Bson update, Boolean isUpsert);

    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param update       update Bson
     * @param isUpsert     default false
     * @param writeConcern WriteConcern
     * @return
     */
    UpdateResult updateOne(Bson filter, Bson update, Boolean isUpsert, WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param filter       filter Bson
     * @param update       update Bson
     * @param isUpsert     default false
     * @param hint         hint Bson
     * @param writeConcern WriteConcern
     * @return
     */
    UpdateResult updateOne(Bson filter, Bson update, Boolean isUpsert, Bson hint, WriteConcern writeConcern);

    /**
     * 修改多条数据
     *
     * @param filter filter Bson
     * @param update update Bson
     * @return
     */
    UpdateResult updateMany(Bson filter, Bson update);

    /**
     * 修改多条数据
     *
     * @param filter       filter Bson
     * @param update       update Bson
     * @param writeConcern WriteConcern
     * @return
     */
    UpdateResult updateMany(Bson filter, Bson update, WriteConcern writeConcern);

    /**
     * 修改多条数据
     *
     * @param filter       filter Bson
     * @param update       update Bson
     * @param hint         hint Bson
     * @param writeConcern WriteConcern
     * @return
     */
    UpdateResult updateMany(Bson filter, Bson update, Bson hint, WriteConcern writeConcern);

    /**
     * @param filter filter Bson
     * @param update update Bson
     * @return
     */
    TEntity findOneAndUpdate(Bson filter, Bson update);

    /**
     * @param filter   filter Bson
     * @param update   update Bson
     * @param isUpsert default false
     * @param sort     sort Bson
     * @return
     */
    TEntity findOneAndUpdate(Bson filter, Bson update, Boolean isUpsert, Bson sort);

    /**
     * @param filter   filter Bson
     * @param update   update Bson
     * @param isUpsert default false
     * @param sort     sort Bson
     * @param hint     hint Bson
     * @return
     */
    TEntity findOneAndUpdate(Bson filter, Bson update, Boolean isUpsert, Bson sort, Bson hint);

    /**
     * @param filter filter Bson
     * @param entity TEntity
     * @return
     */
    TEntity findOneAndUpdate(Bson filter, TEntity entity);

    /**
     * @param filter   filter Bson
     * @param entity   TEntity
     * @param isUpsert default false
     * @param sort     sort Bson
     * @return
     */
    TEntity findOneAndUpdate(Bson filter, TEntity entity, Boolean isUpsert, Bson sort);

    /**
     * @param filter   filter Bson
     * @param entity   TEntity
     * @param isUpsert default false
     * @param sort     sort Bson
     * @param hint     hint Bson
     * @return
     */
    TEntity findOneAndUpdate(Bson filter, TEntity entity, Boolean isUpsert, Bson sort, Bson hint);

    /**
     * @param filter filter Bson
     * @return
     */
    TEntity findOneAndDelete(Bson filter);

    /**
     * @param filter filter Bson
     * @return
     */
    TEntity findOneAndDelete(Bson filter, Bson sort);

    /**
     * @param filter filter Bson
     * @param hint   hint Bson
     * @return
     */
    TEntity findOneAndDelete(Bson filter, Bson sort, Bson hint);

    /**
     * @param id TKey
     * @return
     */
    DeleteResult deleteOne(TKey id);

    /**
     * @param id           TKey
     * @param writeConcern WriteConcern
     * @return
     */
    DeleteResult deleteOne(TKey id, WriteConcern writeConcern);

    /**
     * @param filter filter Bson
     * @return
     */
    DeleteResult deleteOne(Bson filter);

    /**
     * @param filter       filter Bson
     * @param writeConcern WriteConcern
     * @return
     */
    DeleteResult deleteOne(Bson filter, WriteConcern writeConcern);

    /**
     * @param filter       filter Bson
     * @param hint         hint Bson
     * @param writeConcern WriteConcern
     * @return
     */
    DeleteResult deleteOne(Bson filter, final Bson hint, WriteConcern writeConcern);

    /**
     * @param filter filter Bson
     * @return
     */
    DeleteResult deleteMany(Bson filter);

    /**
     * @param filter       filter Bson
     * @param writeConcern WriteConcern
     * @return
     */
    DeleteResult deleteMany(Bson filter, WriteConcern writeConcern);


    /**
     * @param filter       filter Bson
     * @param hint         hint Bson
     * @param writeConcern WriteConcern
     * @return
     */
    DeleteResult deleteMany(final Bson filter, final Bson hint, final WriteConcern writeConcern);

}
