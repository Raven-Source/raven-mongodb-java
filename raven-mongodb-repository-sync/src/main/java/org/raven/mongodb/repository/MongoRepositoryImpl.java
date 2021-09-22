package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.annotations.PostUpdate;
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
     * @param entities
     */
    @Override
    public InsertManyResult insertBatch(final List<TEntity> entities) {
        return this.insertBatch(entities, null);
    }

    /**
     * @param entities
     * @param writeConcern
     */
    @Override
    public InsertManyResult insertBatch(final List<TEntity> entities, final WriteConcern writeConcern) {

        List<TEntity> entityStream = entities.stream().filter(x -> x.getId() == null).collect(Collectors.toList());
        long count = entityStream.size();

        if (count > 0) {
            List<TKey> ids = idGenerator.generateIdBatch(count);

            for (int i = 0; i < count; i++) {
                entityStream.get(i).setId(ids.get(i));
            }
        }

        return super.getCollection(writeConcern).insertMany(entities);
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
     * @param updateEntity
     * @param isUpsert
     * @param hint
     * @param writeConcern
     * @return
     */
    @Override
    public UpdateResult updateOne(final Bson filter, final TEntity updateEntity, final Boolean isUpsert, final Bson hint, final WriteConcern writeConcern) {

        Bson update = createUpdateBson(updateEntity, isUpsert);

        return this.updateOne(filter, update, isUpsert, hint, writeConcern);
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
        return this.updateOne(filter, update, false, (WriteConcern) null);
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
        return this.updateOne(filter, update, isUpsert, (WriteConcern) null);
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
        return this.updateOne(filter, update, isUpsert, (Bson) null, writeConcern);
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
    public UpdateResult updateOne(final Bson filter, final Bson update, final Boolean isUpsert, Bson hint, final WriteConcern writeConcern) {

        return this.updateOne(filter, update, isUpsert, hint, writeConcern);
    }

    /**
     * 修改单条数据
     *
     * @param options UpdateOptions
     * @return
     */
    @Override
    public UpdateResult updateOne(final UpdateOptions options) {

        return this.doUpdate(options, UpdateType.ONE);
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
        return this.updateMany(filter, update, (WriteConcern) null);
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
        return this.updateMany(filter, update, (Bson) null, writeConcern);
    }

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @param hint
     * @param writeConcern
     * @return
     */
    @Override
    public UpdateResult updateMany(final Bson filter, final Bson update, Bson hint, final WriteConcern writeConcern) {

        return this.updateMany(filter, update, hint, writeConcern);
    }

    /**
     * 修改多条数据
     *
     * @param options
     * @return
     */
    @Override
    public UpdateResult updateMany(final UpdateOptions options) {

        options.upsert(false);
        return this.doUpdate(options, UpdateType.MANY);
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

        return this.findOneAndUpdate(filter, update, isUpsert, sort, null);
    }

    /**
     * 找到并更新
     *
     * @param filter
     * @param update
     * @param isUpsert default false
     * @param sort
     * @param hint
     * @return
     */
    @Override
    public TEntity findOneAndUpdate(final Bson filter, final Bson update, final Boolean isUpsert, final Bson sort, final Bson hint) {

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.filter(filter);
        options.update(update);
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(isUpsert);

        options.sort(sort);
        options.hint(hint);

        return this.findOneAndUpdate(options);
    }

    /**
     * 找到并更新
     *
     * @param options FindOneAndUpdateOptions
     * @return
     */
    @Override
    public TEntity findOneAndUpdate(final FindOneAndUpdateOptions options) {
        options.returnDocument(ReturnDocument.AFTER);

        return this.doFindOneAndUpdate(options.filter(), options.update(), options);
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
        return this.findOneAndUpdate(filter, entity, isUpsert, sort, null);
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
    public TEntity findOneAndUpdate(final Bson filter, final TEntity entity, final Boolean isUpsert, final Bson sort, final Bson hint) {

        Bson update = createUpdateBson(entity, isUpsert);

        return this.findOneAndUpdate(filter, update, isUpsert, sort, hint);
    }

    /**
     * 找到并删除
     *
     * @param filter
     * @return
     */
    @Override
    public TEntity findOneAndDelete(final Bson filter) {
        return this.findOneAndDelete(filter, (Bson) null);
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
        return this.findOneAndDelete(filter, sort, (Bson) null);
    }

    /**
     * 找到并删除
     *
     * @param filter
     * @param sort
     * @param hint
     * @return
     */
    @Override
    public TEntity findOneAndDelete(final Bson filter, final Bson sort, final Bson hint) {

        FindOneAndDeleteOptions option = new FindOneAndDeleteOptions();
        option.filter(filter);
        option.sort(sort);
        option.hint(hint);

        return this.findOneAndDelete(option);
    }

    /**
     * 找到并删除
     *
     * @param option FindOneAndDeleteOptions
     * @return
     */
    @Override
    public TEntity findOneAndDelete(final FindOneAndDeleteOptions option) {

        return doFindOneAndDelete(option.filter(), option);
    }

    //#endregion

    //#region delete

    /**
     * @param id 主键
     * @return
     */
    @Override
    public DeleteResult deleteOne(final TKey id) {
        return this.deleteOne(id, (WriteConcern) null);
    }

    /**
     * @param id           主键
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public DeleteResult deleteOne(final TKey id, final WriteConcern writeConcern) {
        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        return this.deleteOne(filter, writeConcern);
    }

    /**
     * @param filter
     * @return
     */
    @Override
    public DeleteResult deleteOne(final Bson filter) {
        return this.deleteOne(filter, (WriteConcern) null);
    }

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public DeleteResult deleteOne(final Bson filter, final WriteConcern writeConcern) {
        return this.deleteOne(filter, (Bson) null, writeConcern);
    }

    /**
     * @param filter
     * @param hint
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public DeleteResult deleteOne(final Bson filter, final Bson hint, final WriteConcern writeConcern) {

        DeleteOptions options = new DeleteOptions();
        options.hint(hint);
        options.filter(filter);
        options.writeConcern(writeConcern);

        return this.deleteOne(options);
    }

    /**
     * @param options DeleteOptions
     * @return
     */
    @Override
    public DeleteResult deleteOne(final DeleteOptions options) {
        return super.getCollection(options.writeConcern()).deleteOne(options.filter(),
            new com.mongodb.client.model.DeleteOptions()
                .hint(options.hint())
        );
    }

    /**
     * @param filter
     * @return
     */
    @Override
    public DeleteResult deleteMany(final Bson filter) {
        return this.deleteMany(filter, null);
    }

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public DeleteResult deleteMany(final Bson filter, final WriteConcern writeConcern) {
        return this.deleteMany(filter, (Bson) null, writeConcern);
    }

    /**
     * @param filter
     * @param hint
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public DeleteResult deleteMany(final Bson filter, final Bson hint, final WriteConcern writeConcern) {

        DeleteOptions options = new DeleteOptions();
        options.hint(hint);
        options.filter(filter);
        options.writeConcern(writeConcern);

        return this.deleteMany(options);
    }

    /**
     * @param options DeleteOptions
     * @return
     */
    @Override
    public DeleteResult deleteMany(final DeleteOptions options) {
        return super.getCollection(options.writeConcern()).deleteMany(options.filter(),
            new com.mongodb.client.model.DeleteOptions()
                .hint(options.hint())
        );
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
     *
     */
    protected UpdateResult doUpdate(final UpdateOptions options,
                                    final UpdateType updateType) {

        if (options.filter() == null) {
            options.filter(new BsonDocument());
        }

        callGlobalInterceptors(PostUpdate.class, null, options);

        if (updateType == UpdateType.ONE) {
            return super.getCollection(options.writeConcern()).updateOne(options.filter(), options.update(),
                new com.mongodb.client.model.UpdateOptions()
                    .hint(options.hint())
                    .upsert(options.upsert())

            );
        } else {
            return super.getCollection(options.writeConcern()).updateMany(options.filter(), options.update(),
                new com.mongodb.client.model.UpdateOptions()
                    .hint(options.hint())
                    .upsert(options.upsert())
            );
        }
    }

    /**
     *
     */
    protected TEntity doFindOneAndUpdate(final Bson filter, final Bson update, final FindOneAndUpdateOptions options) {

        if (options.filter() == null) {
            options.filter(new BsonDocument());
        }

        callGlobalInterceptors(PostUpdate.class, null, options);

        return super.getCollection().findOneAndUpdate(filter, update,
            new com.mongodb.client.model.FindOneAndUpdateOptions()
                .returnDocument(options.returnDocument())
                .upsert(options.upsert())
                .hint(options.hint())
                .sort(options.sort())
        );
    }

    /**
     *
     */
    protected TEntity doFindOneAndDelete(final Bson filter, final FindOneAndDeleteOptions options) {

        if (options.filter() == null) {
            options.filter(new BsonDocument());
        }

        return super.getCollection().findOneAndDelete(filter,
            new com.mongodb.client.model.FindOneAndDeleteOptions()
                .hint(options.hint())
                .sort(options.sort())
        );
    }

    //endregion

}
