package org.raven.mongodb.repository.reactive;

import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.MongoOptions;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.spi.ReactiveIdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 * @since JDK11
 */
public class ReactiveMongoRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends ReactiveMongoReaderRepositoryImpl<TEntity, TKey>
        implements ReactiveMongoRepository<TEntity, TKey> {


    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession
     * @param collectionName
     * @param idGeneratorProvider
     */
    public ReactiveMongoRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName
            , final IdGeneratorProvider<ReactiveIdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {

        super(mongoSession, collectionName, idGeneratorProvider);
    }

    /**
     * constructor
     *
     * @param mongoSession
     */
    public ReactiveMongoRepositoryImpl(final ReactiveMongoSession mongoSession) {
        super(mongoSession);
    }

    /**
     * constructor
     *
     * @param mongoOptions mongoOptions
     */
    public ReactiveMongoRepositoryImpl(final MongoOptions mongoOptions) {
        super(mongoOptions);
    }

    //#endregion

    //#region insert

    /**
     * @param entity
     */
    @Override
    public Mono<InsertOneResult> insert(final TEntity entity) {
        return this.insert(entity, null);
    }

    /**
     * @param entity
     * @param writeConcern
     */
    @Override
    public Mono<InsertOneResult> insert(final TEntity entity, final WriteConcern writeConcern) {

        Mono<TEntity> mono = Mono.just(entity);
        if (entity.getId() == null) {

            mono = idGenerator.generateId().map(x -> {
                entity.setId(x);
                return entity;

            });

        }
        return mono.flatMap(e ->
                Mono.from(super.getCollection(writeConcern).insertOne(e))
        );
    }

    /**
     * @param entitys
     */
    @Override
    public Mono<InsertManyResult> insertBatch(final List<TEntity> entitys) {
        return this.insertBatch(entitys, null);
    }

    /**
     * @param entitys
     * @param writeConcern
     */
    @Override
    public Mono<InsertManyResult> insertBatch(final List<TEntity> entitys, final WriteConcern writeConcern) {
        //

        Mono<List<TEntity>> mono = Mono.just(entitys);

        List<TEntity> entityStream = entitys.stream().filter(x -> x.getId() == null).collect(Collectors.toList());
        long count = entityStream.size();

        if (count > 0) {

            mono = idGenerator.generateIdBatch(count).map(ids -> {

                for (int i = 0; i < count; i++) {
                    entityStream.get(i).setId(ids.get(i));
                }

                return entitys;
            });

        }
        return mono.flatMap(es ->
                Mono.from(super.getCollection(writeConcern).insertMany(es))
        );
    }

    //#endregion

    /**
     * @param updateEntity
     * @param isUpsert
     * @return
     */
    protected Mono<Bson> createUpdateBson(final TEntity updateEntity, final Boolean isUpsert) {

        BsonDocument bsDoc = super.toBsonDocument(updateEntity);
        bsDoc.remove(BsonConstant.PRIMARY_KEY_NAME);

        Bson update = new BsonDocument("$set", bsDoc);
        if (isUpsert) {
            return idGenerator.generateId().map(id ->
                    Updates.combine(update, Updates.setOnInsert(BsonConstant.PRIMARY_KEY_NAME, id))
            );
        } else {
            return Mono.just(update);
        }

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
    public Mono<UpdateResult> updateOne(final Bson filter, final TEntity updateEntity) {
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
    public Mono<UpdateResult> updateOne(final Bson filter, final TEntity updateEntity, final Boolean isUpsert) {

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
    public Mono<UpdateResult> updateOne(final Bson filter, final TEntity updateEntity, final Boolean isUpsert, final WriteConcern writeConcern) {

        UpdateOptions options = new UpdateOptions();
        options.upsert(isUpsert);

        return createUpdateBson(updateEntity, isUpsert).flatMap(update -> Mono.from(
                super.getCollection(writeConcern).updateOne(filter, update, options)
        ));

    }

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @return
     */
    @Override
    public Mono<UpdateResult> updateOne(final Bson filter, final Bson update) {
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
    public Mono<UpdateResult> updateOne(final Bson filter, final Bson update, final Boolean isUpsert) {
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
    public Mono<UpdateResult> updateOne(final Bson filter, final Bson update, final Boolean isUpsert, final WriteConcern writeConcern) {

        UpdateOptions options = new UpdateOptions();
        options.upsert(isUpsert);

        return Mono.from(
                super.getCollection(writeConcern).updateOne(filter, update, options)
        );
    }

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @return
     */
    @Override
    public Mono<UpdateResult> updateMany(final Bson filter, final Bson update) {
        return Mono.from(
                super.getCollection().updateMany(filter, update)
        );
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
    public Mono<UpdateResult> updateMany(final Bson filter, final Bson update, final WriteConcern writeConcern) {
        return Mono.from(
                super.getCollection(writeConcern).updateMany(filter, update)
        );
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
    public Mono<TEntity> findOneAndUpdate(final Bson filter, final Bson update) {
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
    public Mono<TEntity> findOneAndUpdate(final Bson filter, final Bson update, final Boolean isUpsert, final Bson sort) {

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(isUpsert);
        if (sort != null) {
            options.sort(sort);
        }

        return Mono.from(super.getCollection().findOneAndUpdate(filter, update, options));
    }

    /**
     * 找到并更新
     *
     * @param filter
     * @param entity
     * @return
     */
    @Override
    public Mono<TEntity> findOneAndUpdate(final Bson filter, final TEntity entity) {
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
    public Mono<TEntity> findOneAndUpdate(final Bson filter, final TEntity entity, final Boolean isUpsert, final Bson sort) {

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(isUpsert);
        if (sort != null) {
            options.sort(sort);
        }

        return createUpdateBson(entity, isUpsert).flatMap(update -> Mono.from(
                super.getCollection().findOneAndUpdate(filter, update, options))
        );

    }

    /**
     * 找到并删除
     *
     * @param filter
     * @return
     */
    @Override
    public Mono<TEntity> findOneAndDelete(final Bson filter) {
        return Mono.from(super.getCollection().findOneAndDelete(filter));
    }

    /**
     * 找到并删除
     *
     * @param filter
     * @param sort
     * @return
     */
    @Override
    public Mono<TEntity> findOneAndDelete(final Bson filter, final Bson sort) {

        FindOneAndDeleteOptions option = new FindOneAndDeleteOptions();
        if (sort != null) {
            option.sort(sort);
        }

        return Mono.from(super.getCollection().findOneAndDelete(filter, option));
    }

    //#endregion

    //#region delete

    /**
     * @param id 主键
     * @return
     */
    @Override
    public Mono<DeleteResult> deleteOne(final TKey id) {
        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        return Mono.from(super.getCollection().deleteOne(filter));
    }

    /**
     * @param id           主键
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public Mono<DeleteResult> deleteOne(final TKey id, final WriteConcern writeConcern) {
        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        return Mono.from(super.getCollection(writeConcern).deleteOne(filter));
    }

    /**
     * @param filter
     * @return
     */
    @Override
    public Mono<DeleteResult> deleteOne(final Bson filter) {
        return Mono.from(super.getCollection().deleteOne(filter));
    }

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public Mono<DeleteResult> deleteOne(final Bson filter, final WriteConcern writeConcern) {
        return Mono.from(super.getCollection(writeConcern).deleteOne(filter));
    }

    /**
     * @param filter
     * @return
     */
    @Override
    public Mono<DeleteResult> deleteMany(final Bson filter) {
        return Mono.from(super.getCollection().deleteMany(filter));
    }

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    @Override
    public Mono<DeleteResult> deleteMany(final Bson filter, final WriteConcern writeConcern) {
        return Mono.from(super.getCollection(writeConcern).deleteMany(filter));
    }


    //#endregion


}
