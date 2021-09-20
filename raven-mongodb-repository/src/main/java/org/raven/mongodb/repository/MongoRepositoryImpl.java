package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.spi.IdGenerationType;
import org.raven.mongodb.repository.spi.IdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 * @since JDK11
 */
public class MongoRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends MongoReaderRepositoryImpl<TEntity, TKey>
        implements MongoRepository<TEntity, TKey> {


    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession        mongoSession
     * @param collectionName      collectionName
     * @param idGeneratorProvider idGeneratorProvider
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<IdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {
        super(mongoSession, collectionName, sequence, idGeneratorProvider);
    }

    /**
     * constructor
     *
     * @param mongoSession mongoSession
     */
    public MongoRepositoryImpl(final MongoSession mongoSession) {
        super(mongoSession);
    }

    /**
     * constructor
     *
     * @param mongoOptions mongoOptions
     */
    public MongoRepositoryImpl(final MongoOptions mongoOptions) {
        super(mongoOptions, null);
    }

    /**
     * constructor
     *
     * @param mongoOptions
     * @param collectionName
     */
    public MongoRepositoryImpl(final MongoOptions mongoOptions, final String collectionName) {
        super(mongoOptions, collectionName);
    }

    //#endregion

//    /**
//     * 创建自增ID
//     *
//     * @param entity
//     */
//    @Override
//    public void createIncId(final TEntity entity) {
//        long _id = 0;
//        _id = this.createIncId();
//        assignmentEntityID(entity, _id);
//    }
//
//    /**
//     * 创建ObjectId
//     *
//     * @param entity
//     */
//    @Override
//    public void createObjectId(final TEntity entity) {
//        ObjectId _id = new ObjectId();
//        assignmentEntityID(entity, _id);
//    }
//
//
//    /**
//     * @return
//     */
//    @Override
//    public long createIncId() {
//        return createIncId(1);
//    }
//
//    /**
//     * @param inc
//     * @return
//     */
//    @Override
//    public long createIncId(final long inc) {
//        return createIncId(inc, 0);
//    }
//
//    /**
//     * @param inc
//     * @param iteration
//     * @return
//     */
//    @Override
//    public long createIncId(final long inc, final int iteration) {
//        long id = 1;
//        MongoCollection<BsonDocument> collection = getDatabase().getCollection(super.sequence.getSequenceName(), BsonDocument.class);
//        String typeName = getCollectionName();
//
//        Bson filter = Filters.eq(super.sequence.getCollectionName(), typeName);
//        Bson updater = Updates.inc(super.sequence.getIncrementID(), inc);
//        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
//        options = options.upsert(true).returnDocument(ReturnDocument.AFTER);
//
//        BsonDocument result = collection.findOneAndUpdate(filter, updater, options);
//        if (result != null) {
//            id = result.getInt64(super.sequence.getIncrementID()).longValue();
//            //id = result[super._sequence.getIncrementID()].AsInt64;
//            return id;
//        } else if (iteration <= 1) {
//            return createIncId(inc, (iteration + 1));
//        } else {
//            throw new MongoException("Failed to get on the IncID");
//        }
//    }

    //#region insert

    /**
     * @param entity
     */
    @Override
    public InsertOneResult insert(final TEntity entity) {
        return this.insert(entity, null);
    }

    /**
     * @param entity
     * @param writeConcern
     */
    @Override
    public InsertOneResult insert(final TEntity entity, final WriteConcern writeConcern) {
        if (entity.getId() == null) {
            TKey id = idGenerator.generateId();
            entity.setId(id);
        }
        return super.getCollection(writeConcern).insertOne(entity);
    }

    /**
     * @param entitys
     */
    @Override
    public InsertManyResult insertBatch(final List<TEntity> entitys) {
        return this.insertBatch(entitys, null);
    }

    /**
     * @param entitys
     * @param writeConcern
     */
    @Override
    public InsertManyResult insertBatch(final List<TEntity> entitys, final WriteConcern writeConcern) {

        List<TEntity> entityStream = entitys.stream().filter(x -> x.getId() == null).collect(Collectors.toList());
        long count = entityStream.size();

        if (count > 0) {
            List<TKey> ids = idGenerator.generateIdBatch(count);

            for (int i = 0; i < count; i++) {
                entityStream.get(i).setId(ids.get(i));
            }
        }

        return super.getCollection(writeConcern).insertMany(entitys);
    }

    //#endregion

    //#region update

    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @return
     */
    @Override
    public UpdateResult updateOne(final Bson filter, final TEntity updateEntity) {
        return this.updateOne(filter, updateEntity, false, null);
    }

    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @param isUpsert
     * @return
     */
    @Override
    public UpdateResult updateOne(final Bson filter, final TEntity updateEntity, final Boolean isUpsert) {

        return this.updateOne(filter, updateEntity, isUpsert, null);
    }

    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @param isUpsert
     * @param writeConcern
     * @return
     */
    @Override
    public UpdateResult updateOne(final Bson filter, final TEntity updateEntity, final Boolean isUpsert, final WriteConcern writeConcern) {

        Bson update = createUpdateBson(updateEntity, isUpsert);

        return this.updateOne(filter, update, isUpsert, writeConcern);
    }

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @return
     */
    @Override
    public UpdateResult updateOne(final Bson filter, final Bson update) {
        return this.updateOne(filter, update, false, null);
    }

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @param isUpsert
     * @return
     */
    @Override
    public UpdateResult updateOne(final Bson filter, final Bson update, final Boolean isUpsert) {
        return this.updateOne(filter, update, isUpsert, null);
    }

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @param isUpsert
     * @param writeConcern
     * @return
     */
    @Override
    public UpdateResult updateOne(final Bson filter, final Bson update, final Boolean isUpsert, final WriteConcern writeConcern) {

        UpdateOptions options = new UpdateOptions();
        options.upsert(isUpsert);
        return doUpdate(filter, update, options, writeConcern, UpdateType.ONE);
    }

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @return
     */
    @Override
    public UpdateResult updateMany(final Bson filter, final Bson update) {
        return super.getCollection().updateMany(filter, update);
    }

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @param writeConcern
     * @return
     */
    @Override
    public UpdateResult updateMany(final Bson filter, final Bson update, final WriteConcern writeConcern) {

        UpdateOptions options = new UpdateOptions();
        options.upsert(false);
        return doUpdate(filter, update, options, writeConcern, UpdateType.MANY);
    }

    //#endregion

    //#region findAndModify

    /**
     * 找到并更新
     *
     * @param filter
     * @param update
     * @return
     */
    @Override
    public TEntity findOneAndUpdate(final Bson filter, final Bson update) {
        return this.findOneAndUpdate(filter, update, false, null);
    }

    /**
     * 找到并更新
     *
     * @param filter
     * @param update
     * @param isUpsert default false
     * @param sort
     * @return
     */
    @Override
    public TEntity findOneAndUpdate(final Bson filter, final Bson update, final Boolean isUpsert, final Bson sort) {

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(isUpsert);
        if (sort != null) {
            options.sort(sort);
        }

        return super.getCollection().findOneAndUpdate(filter, update, options);
    }

    /**
     * 找到并更新
     *
     * @param filter
     * @param entity
     * @return
     */
    @Override
    public TEntity findOneAndUpdate(final Bson filter, final TEntity entity) {
        return this.findOneAndUpdate(filter, entity, false, null);
    }

    /**
     * 找到并更新
     *
     * @param filter
     * @param entity
     * @param isUpsert default false
     * @param sort
     * @return
     */
    @Override
    public TEntity findOneAndUpdate(final Bson filter, final TEntity entity, final Boolean isUpsert, final Bson sort) {

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(isUpsert);
        if (sort != null) {
            options.sort(sort);
        }

        Bson update = createUpdateBson(entity, isUpsert);

        return super.getCollection().findOneAndUpdate(filter, update, options);
    }

    /**
     * 找到并删除
     *
     * @param filter
     * @return
     */
    @Override
    public TEntity findOneAndDelete(final Bson filter) {
        return super.getCollection().findOneAndDelete(filter);
    }

    /**
     * 找到并删除
     *
     * @param filter
     * @param sort
     * @return
     */
    @Override
    public TEntity findOneAndDelete(final Bson filter, final Bson sort) {

        FindOneAndDeleteOptions option = new FindOneAndDeleteOptions();
        if (sort != null) {
            option.sort(sort);
        }

        return super.getCollection().findOneAndDelete(filter, option);
    }

    //#endregion

    //#region delete

    /**
     * @param id 主键
     * @return
     */
    @Override
    public DeleteResult deleteOne(final TKey id) {
        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        return super.getCollection().deleteOne(filter);
    }

    /**
     * @param id           主键
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public DeleteResult deleteOne(final TKey id, final WriteConcern writeConcern) {
        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        return super.getCollection(writeConcern).deleteOne(filter);
    }

    /**
     * @param filter
     * @return
     */
    @Override
    public DeleteResult deleteOne(final Bson filter) {
        return super.getCollection().deleteOne(filter);
    }

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public DeleteResult deleteOne(final Bson filter, final WriteConcern writeConcern) {
        return super.getCollection(writeConcern).deleteOne(filter);
    }

    /**
     * @param filter
     * @return
     */
    @Override
    public DeleteResult deleteMany(final Bson filter) {
        return super.getCollection().deleteMany(filter);
    }

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public DeleteResult deleteMany(final Bson filter, final WriteConcern writeConcern) {
        return super.getCollection(writeConcern).deleteMany(filter);
    }


    //#endregion

    //region protected

    /**
     * @param updateEntity
     * @param isUpsert
     * @return
     */
    protected Bson createUpdateBson(final TEntity updateEntity, final Boolean isUpsert) {

        BsonDocument bsDoc = entityInformation.toBsonDocument(updateEntity);
        bsDoc.remove(BsonConstant.PRIMARY_KEY_NAME);

        Bson update = new BsonDocument("$set", bsDoc);
        if (isUpsert && entityInformation.getIdGenerationType() == IdGenerationType.AUTO_INCR) {
            TKey id = idGenerator.generateId();
            update = Updates.combine(update, Updates.setOnInsert(BsonConstant.PRIMARY_KEY_NAME, id));
        }

        return update;

    }

    /**
     * @param filter
     * @param update
     * @param options
     * @param writeConcern
     * @return
     */
    protected UpdateResult doUpdate(final Bson filter,
                                    final Bson update,
                                    final UpdateOptions options,
                                    final WriteConcern writeConcern,
                                    final UpdateType updateType) {
        if (updateType == UpdateType.ONE) {
            return super.getCollection(writeConcern).updateOne(filter, update, options);
        } else {
            return super.getCollection(writeConcern).updateMany(filter, update, options);
        }
    }

    //endregion

}
