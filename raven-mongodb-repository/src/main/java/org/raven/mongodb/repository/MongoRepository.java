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
     * @param entity
     */
    InsertOneResult insert(TEntity entity);

    /**
     * @param entity
     * @param writeConcern
     */
    InsertOneResult insert(TEntity entity, WriteConcern writeConcern);

    /**
     * @param entitys
     */
    InsertManyResult insertBatch(List<TEntity> entitys);

    /**
     * @param entitys
     * @param writeConcern
     */
    InsertManyResult insertBatch(List<TEntity> entitys, WriteConcern writeConcern);


    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @return
     */
    UpdateResult updateOne(Bson filter, TEntity updateEntity);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @param isUpsert
     * @return
     */
    UpdateResult updateOne(Bson filter, TEntity updateEntity, Boolean isUpsert);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @param isUpsert
     * @param writeConcern
     * @return
     */
    UpdateResult updateOne(Bson filter, TEntity updateEntity, Boolean isUpsert, WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @return
     */
    UpdateResult updateOne(Bson filter, Bson update);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @param isUpsert
     * @return
     */
    UpdateResult updateOne(Bson filter, Bson update, Boolean isUpsert);

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @param isUpsert
     * @param writeConcern
     * @return
     */
    UpdateResult updateOne(Bson filter, Bson update, Boolean isUpsert, WriteConcern writeConcern);

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @return
     */
    UpdateResult updateMany(Bson filter, Bson update);

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @param writeConcern
     * @return
     */
    UpdateResult updateMany(Bson filter, Bson update, WriteConcern writeConcern);

    /**
     * @param filter
     * @param update
     * @return
     */
    TEntity findOneAndUpdate(Bson filter, Bson update);

    /**
     * @param filter
     * @param update
     * @param isUpsert default false
     * @param sort
     * @return
     */
    TEntity findOneAndUpdate(Bson filter, Bson update, Boolean isUpsert, Bson sort);

    /**
     * @param filter
     * @param entity
     * @return
     */
    TEntity findOneAndUpdate(Bson filter, TEntity entity);

    /**
     * @param filter
     * @param entity
     * @param isUpsert default false
     * @param sort
     * @return
     */
    TEntity findOneAndUpdate(Bson filter, TEntity entity, Boolean isUpsert, Bson sort);

    /**
     * @param filter
     * @return
     */
    TEntity findOneAndDelete(Bson filter);

    /**
     * @param filter
     * @param sort
     * @return
     */
    TEntity findOneAndDelete(Bson filter, Bson sort);

    /**
     * @param id
     * @return
     */
    DeleteResult deleteOne(TKey id);

    /**
     * @param id
     * @param writeConcern
     * @return
     */
    DeleteResult deleteOne(TKey id, WriteConcern writeConcern);

    /**
     * @param filter
     * @return
     */
    DeleteResult deleteOne(Bson filter);

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    DeleteResult deleteOne(Bson filter, WriteConcern writeConcern);

    /**
     * @param filter
     * @return
     */
    DeleteResult deleteMany(Bson filter);

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    DeleteResult deleteMany(Bson filter, WriteConcern writeConcern);

}
