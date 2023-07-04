package org.raven.mongodb.repository.reactive;

import com.mongodb.WriteConcern;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.ClientSession;
import lombok.NonNull;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.*;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReactiveModifyOperationImpl<TEntity extends Entity<TKey>, TKey>
        implements ReactiveModifyOperation<TEntity, TKey> {

    private final AbstractAsyncMongoBaseRepository<TEntity, TKey> baseRepository;

    private final @Nullable ClientSession clientSession;


    public ReactiveModifyOperationImpl(AbstractAsyncMongoBaseRepository<TEntity, TKey> baseRepository,
                                       @Nullable ClientSession clientSession) {
        this.baseRepository = baseRepository;
        this.clientSession = clientSession;
    }

    protected ReactiveModifyOperationImpl<TEntity, TKey> clone(ClientSession clientSession) {
        return new ReactiveModifyOperationImpl<>(this.baseRepository, clientSession);
    }


    /**
     * 修改单条数据
     *
     * @param filter       filter
     * @param updateEntity entity
     * @param isUpsert     default false
     * @param hint         hint
     * @param writeConcern {{@link WriteConcern}}
     * @return UpdateResult
     */
    @Override
    public Mono<Long> updateOne(final Bson filter, final TEntity updateEntity, final boolean isUpsert, final Bson hint, final WriteConcern writeConcern) {

        return baseRepository.createUpdateBson(updateEntity, isUpsert).flatMap(update ->
                this.updateOne(filter, update, isUpsert, hint, writeConcern)
        );

    }
    /**
     * 找到并更新
     *
     * @param filter   filter
     * @param entity   entity
     * @param isUpsert default false
     * @param sort     sort
     * @return Entity
     */
    @Override
    public Mono<TEntity> findOneAndUpdate(final Bson filter, final TEntity entity, final boolean isUpsert, final Bson sort, final Bson hint) {

        return baseRepository.createUpdateBson(entity, isUpsert).flatMap(update ->
                this.findOneAndUpdate(filter, update, isUpsert, sort, hint)
        );

    }

    protected Mono<InsertOneResult> doInsert(final TEntity entity, final WriteConcern writeConcern) {
        return baseRepository.doInsert(this.clientSession, entity, writeConcern);
    }

    protected Mono<InsertManyResult> doInsertBatch(final List<TEntity> entities, final WriteConcern writeConcern) {
        return baseRepository.doInsertBatch(this.clientSession, entities, writeConcern);
    }

    protected Mono<UpdateResult> doUpdate(@NonNull final UpdateOptions options,
                                    final UpdateType updateType) {
        return baseRepository.doUpdate(this.clientSession, options, updateType);
    }

    protected Mono<TEntity> doFindOneAndUpdate(final FindOneAndUpdateOptions options) {
        return baseRepository.doFindOneAndUpdate(this.clientSession, options);
    }

    protected Mono<TEntity> doFindOneAndDelete(@NonNull final FindOneAndDeleteOptions options) {
        return baseRepository.doFindOneAndDelete(this.clientSession, options);
    }

    protected Mono<DeleteResult> doDeleteOne(final DeleteOptions options) {
        return baseRepository.doDeleteOne(this.clientSession, options);
    }

    protected Mono<DeleteResult> doDeleteMany(final DeleteOptions options) {
        return baseRepository.doDeleteMany(this.clientSession, options);
    }


    @Override
    public ModifyProxy<TEntity, TKey, Mono<Optional<TKey>>, Mono<Map<Integer, TKey>>, Mono<Long>, Mono<TEntity>, Mono<Long>> modifyProxy() {
        return proxy;
    }

    private final ModifyProxy<TEntity, TKey, Mono<Optional<TKey>>, Mono<Map<Integer, TKey>>, Mono<Long>, Mono<TEntity>, Mono<Long>> proxy =
            new ModifyProxy<TEntity, TKey, Mono<Optional<TKey>>, Mono<Map<Integer, TKey>>, Mono<Long>, Mono<TEntity>, Mono<Long>>() {

                @Override
                public EntityInformation<TEntity, TKey> getEntityInformation() {
                    return baseRepository.getEntityInformation();
                }

                @Override
                public Mono<Optional<TKey>> doInsert(TEntity entity, WriteConcern writeConcern) {
                    return ReactiveModifyOperationImpl.this.doInsert(entity, writeConcern).map(x ->
                            Optional.ofNullable(
                                    x.wasAcknowledged()
                                            ? BsonUtils.convert(baseRepository.getEntityInformation().getIdType(), x.getInsertedId())
                                            : null
                            )
                    );
                }

                @Override
                public Mono<Map<Integer, TKey>> doInsertBatch(List<TEntity> entities, WriteConcern writeConcern) {

                    return ReactiveModifyOperationImpl.this.doInsertBatch(entities, writeConcern).map(x -> {

                        Map<Integer, TKey> integerTKeyMap = new HashMap<>();
                        if (x.wasAcknowledged()) {
                            for (Map.Entry<Integer, BsonValue> e : x.getInsertedIds().entrySet()) {
                                integerTKeyMap.put(
                                        e.getKey(),
                                        BsonUtils.convert(baseRepository.getEntityInformation().getIdType(), e.getValue()))
                                ;
                            }
                        }

                        return integerTKeyMap;
                    });
                }

                @Override
                public Mono<Long> doUpdate(@NonNull UpdateOptions options, UpdateType updateType) {
                    return ReactiveModifyOperationImpl.this.doUpdate(options, updateType).map(x ->
                            x.wasAcknowledged() ? x.getModifiedCount() : 0
                    );
                }

                @Override
                public Mono<TEntity> doFindOneAndUpdate(FindOneAndUpdateOptions options) {
                    return ReactiveModifyOperationImpl.this.doFindOneAndUpdate(options);
                }

                @Override
                public Mono<TEntity> doFindOneAndDelete(@NonNull FindOneAndDeleteOptions options) {
                    return ReactiveModifyOperationImpl.this.doFindOneAndDelete(options);
                }

                @Override
                public Mono<Long> doDeleteOne(DeleteOptions options) {
                    return ReactiveModifyOperationImpl.this.doDeleteOne(options).map(x ->
                            x.wasAcknowledged() ? x.getDeletedCount() : 0
                    );
                }

                @Override
                public Mono<Long> doDeleteMany(DeleteOptions options) {
                    return ReactiveModifyOperationImpl.this.doDeleteMany(options).map(x ->
                            x.wasAcknowledged() ? x.getDeletedCount() : 0
                    );
                }
            };

}
