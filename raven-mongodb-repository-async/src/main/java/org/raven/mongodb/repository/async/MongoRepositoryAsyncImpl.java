package org.raven.mongodb.repository.async;

import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.MongoRepositoryOptions;
import org.raven.mongodb.repository.MongoSequence;
import org.raven.mongodb.repository.contants.BsonConstant;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public class MongoRepositoryAsyncImpl<TEntity extends Entity<TKey>, TKey>
    extends MongoReaderRepositoryAsyncImpl<TEntity, TKey>
    implements MongoRepositoryAsync<TEntity, TKey> {

    //#region 构造函数

    /**
     * 构造函数
     *
     * @param uri            数据库连接uri
     * @param dbName         数据库名称
     * @param collectionName 集合名称
     * @param writeConcern   WriteConcern
     * @param readPreference ReadPreference
     * @param sequence       Mongo自增长ID数据序列对象
     */
    public MongoRepositoryAsyncImpl(final String uri, final String dbName, final String collectionName, final WriteConcern writeConcern, final ReadPreference readPreference, final MongoSequence sequence) {
        super(uri, dbName, collectionName, writeConcern, readPreference, sequence);
    }

    /**
     * 构造函数
     *
     * @param uri    数据库连接uri
     * @param dbName 数据库名称
     */
    public MongoRepositoryAsyncImpl(final String uri, final String dbName) {
        super(uri, dbName);
    }

    /**
     * 构造函数
     *
     * @param options
     * @see MongoRepositoryOptions
     */
    public MongoRepositoryAsyncImpl(final MongoRepositoryOptions options) {
        super(options);
    }

    //#endregion

    /**
     * 创建自增ID
     *
     * @param entity
     */
    @Override
    public CompletableFuture createIncIDAsync(final TEntity entity) {
        long _id = 0;
        CompletableFuture<Long> future = this.createIncIDAsync();
        return future.thenAccept((id) -> assignmentEntityID(entity, id));
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
    public CompletableFuture<Long> createIncIDAsync() {
        return createIncIDAsync(1);
    }

    /**
     * @param inc
     * @return
     */
    @Override
    public CompletableFuture<Long> createIncIDAsync(final long inc) {

        MongoCollection<BsonDocument> collection = getDatabase().getCollection(super._sequence.getSequenceName(), BsonDocument.class);
        String typeName = getCollectionName();

        Bson filter = Filters.eq(super._sequence.getCollectionName(), typeName);
        Bson updater = Updates.inc(super._sequence.getIncrementID(), inc);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options = options.upsert(true).returnDocument(ReturnDocument.AFTER);

        CompletableFuture<Long> future = new CompletableFuture<>();

        collection.findOneAndUpdate(filter, updater, options, new SingleResultCallback<BsonDocument>() {
            @Override
            public void onResult(BsonDocument result, Throwable throwable) {

                long id = 1;
                if (throwable == null) {
                    id = result.getInt64(_sequence.getIncrementID()).longValue();
                    future.complete(id);
                } else {
                    future.completeExceptionally(new MongoException("Failed to get on the IncID"));
                }
            }
        });

        return future;
    }

    //#region insert

    /**
     * @param entity
     * @return
     */
    @Override
    public CompletableFuture insertAsync(final TEntity entity) {
        return this.insertAsync(entity, null);
    }

    /**
     * @param entity
     * @param writeConcern
     * @return
     */
    @Override
    public CompletableFuture insertAsync(final TEntity entity, final WriteConcern writeConcern) {

        final CompletableFuture<Void> resFuture = new CompletableFuture<>();
        final MongoCollection<TEntity> collection = super.getCollection(writeConcern);
        final Runnable runnable = () -> {
            collection.insertOne(entity, new SingleResultCallback<Void>() {
                @Override
                public void onResult(Void result, Throwable throwable) {
                    if (throwable == null) {
                        resFuture.complete(result);
                    } else {
                        resFuture.completeExceptionally(throwable);
                    }
                }
            });
        };

        if (isAutoIncrClass) {
            createIncIDAsync(entity).thenRun(runnable);
        } else {
            if (keyClazz.equals(BsonConstant.OBJECT_ID_CLASS) && ((Entity<ObjectId>) entity).getId() == null) {
                createObjectID(entity);
            }
            runnable.run();
        }

        return resFuture;
    }


    /**
     * @param entitys
     */
    @Override
    public CompletableFuture insertBatchAsync(final List<TEntity> entitys) {
        return this.insertBatchAsync(entitys, null);
    }

    /**
     * @param entitys
     * @param writeConcern
     * @return
     */
    @Override
    public CompletableFuture insertBatchAsync(final List<TEntity> entitys, final WriteConcern writeConcern) {

        final CompletableFuture<Void> resFuture = new CompletableFuture<>();

        final MongoCollection<TEntity> collection = super.getCollection(writeConcern);
        final Runnable runnable = () -> {
            collection.insertMany(entitys, new SingleResultCallback<Void>() {
                @Override
                public void onResult(Void result, Throwable throwable) {
                    if (throwable == null) {
                        resFuture.complete(result);
                    } else {
                        resFuture.completeExceptionally(new MongoException("Failed to get on the IncID"));
                    }
                }
            });
        };

        //需要自增的实体
        if (isAutoIncrClass) {
            int count = entitys.size();

            createIncIDAsync(count).thenAccept((id) -> {
                //自增ID值
                id = id - count;
                for (TEntity entity : entitys) {
                    assignmentEntityID(entity, ++id);
                }

                runnable.run();
            });

        } else {
            if (keyClazz.equals(BsonConstant.OBJECT_ID_CLASS)) {
                for (TEntity entity : entitys) {
                    if (((Entity<ObjectId>) entity).getId() == null) {
                        createObjectID(entity);
                    }
                }
            }
            runnable.run();
        }

        return resFuture;
    }

    //#endregion

    /**
     * @param updateEntity
     * @param isUpsert
     * @return
     * @throws MongoException
     */
    protected CompletableFuture<Bson> createUpdateBson(final TEntity updateEntity, final Boolean isUpsert)
        throws MongoException {

        CompletableFuture<Bson> future = new CompletableFuture<>();

        BsonDocument bsDoc = super.toBsonDocument(updateEntity);
        bsDoc.remove(BsonConstant.PRIMARY_KEY_NAME);

        Bson update = new BsonDocument("$set", bsDoc);
        if (isUpsert && isAutoIncrClass) {
            createIncIDAsync().thenAccept((id) -> {
                future.complete(Updates.combine(update, Updates.setOnInsert(BsonConstant.PRIMARY_KEY_NAME, id)));
            });
        } else {
            future.complete(update);
        }

        return future;
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
    public CompletableFuture<UpdateResult> updateOneAsync(final Bson filter, final TEntity updateEntity)
        throws MongoException {
        return this.updateOneAsync(filter, updateEntity, false, null);
    }

    /**
     * 修改单条数据
     *
     * @param filter
     * @param updateEntity
     * @return
     */
    @Override
    public CompletableFuture<UpdateResult> updateOneAsync(final Bson filter, final TEntity updateEntity, final Boolean isUpsert)
        throws MongoException {
        return this.updateOneAsync(filter, updateEntity, isUpsert, null);
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
    public CompletableFuture<UpdateResult> updateOneAsync(final Bson filter, final TEntity updateEntity, final Boolean isUpsert, final WriteConcern writeConcern)
        throws MongoException {

        CompletableFuture<UpdateResult> future = new CompletableFuture<>();

        final UpdateOptions options = new UpdateOptions();
        options.upsert(isUpsert);

        MongoCollection<TEntity> collection = super.getCollection(writeConcern);

        createUpdateBson(updateEntity, isUpsert).thenAccept((update) -> {

            collection.updateOne(filter, update, options, (result, throwable) -> {

                if (throwable == null) {
                    future.complete(result);
                } else {
                    future.completeExceptionally(throwable);
                }
            });
        });

        return future;
    }

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @return
     */
    @Override
    public CompletableFuture<UpdateResult> updateOneAsync(final Bson filter, final Bson update) {
        return this.updateOneAsync(filter, update, false, null);
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
    public CompletableFuture<UpdateResult> updateOneAsync(final Bson filter, final Bson update, final Boolean isUpsert) {
        return this.updateOneAsync(filter, update, isUpsert, null);
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
    public CompletableFuture<UpdateResult> updateOneAsync(final Bson filter, final Bson update, final Boolean isUpsert, final WriteConcern writeConcern) {

        CompletableFuture<UpdateResult> future = new CompletableFuture<>();

        UpdateOptions options = new UpdateOptions();
        options.upsert(isUpsert);

        super.getCollection(writeConcern).updateOne(filter, update, options, (result, throwable) -> {

            if (throwable == null) {
                future.complete(result);
            } else {
                future.completeExceptionally(throwable);
            }

        });

        return future;
    }

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @return
     */
    @Override
    public CompletableFuture<UpdateResult> updateManyAsync(final Bson filter, final Bson update) {
        return this.updateOneAsync(filter, update, null);
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
    public CompletableFuture<UpdateResult> updateManyAsync(final Bson filter, final Bson update, final WriteConcern writeConcern) {

        CompletableFuture<UpdateResult> future = new CompletableFuture<>();

        super.getCollection(writeConcern).updateMany(filter, update, (result, throwable) -> {

            if (throwable == null) {
                future.complete(result);
            } else {
                future.completeExceptionally(throwable);
            }
        });

        return future;
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
    public CompletableFuture<TEntity> findOneAndUpdateAsync(final Bson filter, final Bson update) {
        return this.findOneAndUpdateAsync(filter, update, false, null);
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
    public CompletableFuture<TEntity> findOneAndUpdateAsync(final Bson filter, final Bson update, final Boolean isUpsert, final Bson sort) {

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(isUpsert);
        if (sort != null) {
            options.sort(sort);
        }

        CompletableFuture<TEntity> future = new CompletableFuture<>();

        super.getCollection().findOneAndUpdate(filter, update, options, (result, throwable) -> {

            if (throwable == null) {
                future.complete(result);
            } else {
                future.completeExceptionally(throwable);
            }
        });

        return future;
    }

    /**
     * 找到并更新
     *
     * @param filter
     * @param entity
     * @return
     * @throws MongoException
     */
    @Override
    public CompletableFuture<TEntity> findOneAndUpdateAsync(final Bson filter, final TEntity entity)
        throws MongoException {
        return this.findOneAndUpdateAsync(filter, entity, false, null);
    }

    /**
     * 找到并更新
     *
     * @param filter
     * @param entity
     * @param isUpsert default false
     * @param sort
     * @return
     * @throws MongoException
     */
    @Override
    public CompletableFuture<TEntity> findOneAndUpdateAsync(final Bson filter, final TEntity entity, final Boolean isUpsert, final Bson sort)
        throws MongoException {

        CompletableFuture<TEntity> future = new CompletableFuture<>();

        final FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(isUpsert);
        if (sort != null) {
            options.sort(sort);
        }
        final MongoCollection<TEntity> collection = super.getCollection();

        createUpdateBson(entity, isUpsert).thenAccept((update) -> {
            collection.findOneAndUpdate(filter, update, options, (result, throwable) -> {

                if (throwable == null) {
                    future.complete(result);
                } else {
                    future.completeExceptionally(throwable);
                }

            });
        });

        return future;
    }

    /**
     * 找到并删除
     *
     * @param filter
     * @return
     */
    @Override
    public CompletableFuture<TEntity> findOneAndDeleteAsync(final Bson filter) {
        return this.findOneAndDeleteAsync(filter);
    }

    /**
     * 找到并删除
     *
     * @param filter
     * @param sort
     * @return
     */
    @Override
    public CompletableFuture<TEntity> findOneAndDeleteAsync(final Bson filter, final Bson sort) {

        CompletableFuture<TEntity> future = new CompletableFuture<>();

        FindOneAndDeleteOptions option = new FindOneAndDeleteOptions();
        if (sort != null) {
            option.sort(sort);
        }

        super.getCollection().findOneAndDelete(filter, option, (result, throwable) -> {

            if (throwable == null) {
                future.complete(result);
            } else {
                future.completeExceptionally(throwable);
            }

        });

        return future;
    }

    //#endregion

    //#region delete

    /**
     * @param id 主键
     * @return
     */
    @Override
    public CompletableFuture<DeleteResult> deleteOneAsync(final TKey id) {
        return this.deleteOneAsync(id);
    }

    /**
     * @param id           主键
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public CompletableFuture<DeleteResult> deleteOneAsync(final TKey id, final WriteConcern writeConcern) {
        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        return this.deleteOneAsync(filter);
    }

    /**
     * @param filter
     * @return
     */
    @Override
    public CompletableFuture<DeleteResult> deleteOneAsync(final Bson filter) {
        return this.deleteOneAsync(filter, null);
    }

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public CompletableFuture<DeleteResult> deleteOneAsync(final Bson filter, final WriteConcern writeConcern) {

        CompletableFuture<DeleteResult> future = new CompletableFuture<>();

        super.getCollection(writeConcern).deleteOne(filter, (result, throwable) -> {

            if (throwable == null) {
                future.complete(result);
            } else {
                future.completeExceptionally(throwable);
            }
        });

        return future;
    }

    /**
     * @param filter
     * @return
     */
    @Override
    public CompletableFuture<DeleteResult> deleteManyAsync(final Bson filter) {
        return this.deleteManyAsync(filter, null);
    }

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public CompletableFuture<DeleteResult> deleteManyAsync(final Bson filter, final WriteConcern writeConcern) {
        CompletableFuture<DeleteResult> future = new CompletableFuture<>();

        super.getCollection(writeConcern).deleteMany(filter, (result, throwable) -> {

            if (throwable == null) {
                future.complete(result);
            } else {
                future.completeExceptionally(throwable);
            }
        });

        return future;
    }


    //#endregion

}
