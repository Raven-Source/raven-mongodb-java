package org.raven.mongodb.repository;

import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.contants.BsonConstant;

import java.util.List;

/**
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 * @since JDK1.8
 */
public class MongoRepositoryImpl<TEntity extends Entity<TKey>, TKey>
    extends MongoReaderRepositoryImpl<TEntity, TKey>
    implements MongoRepository<TEntity, TKey> {


    //#region constructor

    /**
     * constructor
     *
     * @param uri            数据库连接节点
     * @param dbName         数据库名称
     * @param collectionName 集合名称
     * @param writeConcern   WriteConcern
     * @param readPreference ReadPreference
     * @param sequence       Mongo自增长ID数据序列对象
     */
    public MongoRepositoryImpl(final String uri, final String dbName, final String collectionName, final WriteConcern writeConcern, final ReadPreference readPreference, final MongoSequence sequence) {
        super(uri, dbName, collectionName, writeConcern, readPreference, sequence);
    }

    /**
     * constructor
     *
     * @param uri    数据库连接节点
     * @param dbName 数据库名称
     */
    public MongoRepositoryImpl(final String uri, final String dbName) {
        super(uri, dbName);
    }

    /**
     * constructor
     *
     * @param options
     * @see MongoRepositoryOptions
     */
    public MongoRepositoryImpl(final MongoRepositoryOptions options) {
        super(options);
    }

    //#endregion

    /**
     * 创建自增ID
     *
     * @param entity
     */
    @Override
    public void createIncID(final TEntity entity) {
        long _id = 0;
        _id = this.createIncID();
        assignmentEntityID(entity, _id);
    }

    /**
     * 创建ObjectId
     *
     * @param entity
     */
    @Override
    public void createObjectID(final TEntity entity) {
        ObjectId _id = new ObjectId();
        assignmentEntityID(entity, _id);
    }


    /**
     * @return
     */
    @Override
    public long createIncID() {
        return createIncID(1);
    }

    /**
     * @param inc
     * @return
     */
    @Override
    public long createIncID(final long inc) {
        return createIncID(inc, 0);
    }

    /**
     * @param inc
     * @param iteration
     * @return
     */
    @Override
    public long createIncID(final long inc, final int iteration) {
        long id = 1;
        MongoCollection<BsonDocument> collection = getDatabase().getCollection(super._sequence.getSequenceName(), BsonDocument.class);
        String typeName = getCollectionName();

        Bson filter = Filters.eq(super._sequence.getCollectionName(), typeName);
        Bson updater = Updates.inc(super._sequence.getIncrementID(), inc);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options = options.upsert(true).returnDocument(ReturnDocument.AFTER);

        BsonDocument result = collection.findOneAndUpdate(filter, updater, options);
        if (result != null) {
            id = result.getInt64(super._sequence.getIncrementID()).longValue();
            //id = result[super._sequence.getIncrementID()].AsInt64;
            return id;
        } else if (iteration <= 1) {
            return createIncID(inc, (iteration + 1));
        } else {
            throw new MongoException("Failed to get on the IncID");
        }
    }

    //#region insert

    /**
     * @param entity
     */
    @Override
    public void insert(final TEntity entity) {
        this.insert(entity, null);
    }

    /**
     * @param entity
     * @param writeConcern
     */
    @Override
    public void insert(final TEntity entity, final WriteConcern writeConcern) {
        if (isAutoIncrClass) {
            createIncID(entity);
        } else if (keyClazz.equals(BsonConstant.OBJECT_ID_CLASS) && ((Entity<ObjectId>) entity).getId() == null) {
            createObjectID(entity);
        }
        super.getCollection(writeConcern).insertOne(entity);
    }

    /**
     * @param entitys
     */
    @Override
    public void insertBatch(final List<TEntity> entitys) {
        this.insertBatch(entitys, null);
    }

    /**
     * @param entitys
     * @param writeConcern
     */
    @Override
    public void insertBatch(final List<TEntity> entitys, final WriteConcern writeConcern) {
        //需要自增的实体
        if (isAutoIncrClass) {
            int count = entitys.size();
            //自增ID值
            long id = createIncID(count);
            id = id - count;

            for (TEntity entity : entitys) {
                assignmentEntityID(entity, ++id);
            }
        } else if (keyClazz.equals(BsonConstant.OBJECT_ID_CLASS)) {
            for (TEntity entity : entitys) {
                if (((Entity<ObjectId>) entity).getId() == null) {
                    createObjectID(entity);
                }
            }
        }

        super.getCollection(writeConcern).insertMany(entitys);
    }

    //#endregion

    /**
     * @param updateEntity
     * @param isUpsert
     * @return
     */
    protected Bson createUpdateBson(final TEntity updateEntity, final Boolean isUpsert) {
        long id = 0;
        BsonDocument bsDoc = super.toBsonDocument(updateEntity);
        bsDoc.remove(BsonConstant.PRIMARY_KEY_NAME);

        Bson update = new BsonDocument("$set", bsDoc);
        if (isUpsert && isAutoIncrClass) {
            id = createIncID();
            update = Updates.combine(update, Updates.setOnInsert(BsonConstant.PRIMARY_KEY_NAME, id));
        }

        return update;

    }

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

        UpdateOptions options = new UpdateOptions();
        options.upsert(isUpsert);

        Bson update = createUpdateBson(updateEntity, isUpsert);

        return super.getCollection(writeConcern).updateOne(filter, update, options);
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
        return super.getCollection(writeConcern).updateOne(filter, update, options);
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
        return super.getCollection(writeConcern).updateMany(filter, update);
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


}
