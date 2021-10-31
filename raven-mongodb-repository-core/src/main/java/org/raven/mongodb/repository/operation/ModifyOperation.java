package org.raven.mongodb.repository.operation;

import com.mongodb.Function;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.NonNull;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.*;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.query.FilterBuilder;
import org.raven.mongodb.repository.query.HintBuilder;
import org.raven.mongodb.repository.query.UpdateBuilder;
import org.raven.mongodb.repository.spi.IdGenerator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author by yanfeng
 * date 2021/10/30 21:39
 */
public interface ModifyOperation<TEntity extends Entity<TKey>, TKey, TInsertOneResult, TInsertManyResult, TUpdateResult, TDeleteResult> {

    //#region insert

    /**
     * @param entity
     */
    default TInsertOneResult insert(final TEntity entity) {
        return this.insert(entity, null);
    }

    /**
     * @param entity
     * @param writeConcern
     */
    default TInsertOneResult insert(final TEntity entity, final WriteConcern writeConcern) {
        if (entity.getId() == null) {
            TKey id = modifyProxy().getIdGenerator().generateId();
            entity.setId(id);
        }
        return modifyProxy().doInsert(entity, writeConcern);
    }

    /**
     * @param entities
     */
    default TInsertManyResult insertBatch(final List<TEntity> entities) {
        return this.insertBatch(entities, null);
    }

    /**
     * @param entities
     * @param writeConcern
     */
    default TInsertManyResult insertBatch(final List<TEntity> entities, final WriteConcern writeConcern) {

        List<TEntity> entityStream = entities.stream().filter(x -> x.getId() == null).collect(Collectors.toList());
        long count = entityStream.size();

        if (count > 0) {
            List<TKey> ids = modifyProxy().getIdGenerator().generateIdBatch(count);

            for (int i = 0; i < count; i++) {
                entityStream.get(i).setId(ids.get(i));
            }
        }

        return modifyProxy().doInsertBatch(entities, writeConcern);
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
    default TUpdateResult updateOne(final Bson filter, final TEntity updateEntity) {
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
    default TUpdateResult updateOne(final Bson filter, final TEntity updateEntity, final boolean isUpsert) {

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
    default TUpdateResult updateOne(final Bson filter, final TEntity updateEntity, final boolean isUpsert, final WriteConcern writeConcern) {

        return this.updateOne(filter, updateEntity, isUpsert, (Bson) null, writeConcern);
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
    TUpdateResult updateOne(final Bson filter, final TEntity updateEntity, final boolean isUpsert, final Bson hint, final WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param id     TKey
     * @param update
     * @return
     */
    default TUpdateResult updateOne(final TKey id, final Bson update) {

        final Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        return this.updateOne(filter, update, false, (WriteConcern) null);
    }

    /**
     * 修改单条数据
     *
     * @param filter
     * @param update
     * @return
     */
    default TUpdateResult updateOne(final Bson filter, final Bson update) {
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
    default TUpdateResult updateOne(final Bson filter, final Bson update, final boolean isUpsert) {
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
    default TUpdateResult updateOne(final Bson filter, final Bson update, final boolean isUpsert, final WriteConcern writeConcern) {
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
    default TUpdateResult updateOne(final Bson filter, final Bson update, final boolean isUpsert, Bson hint, final WriteConcern writeConcern) {

        UpdateOptions options = new UpdateOptions();
        options.filter(filter);
        options.update(update);
        options.upsert(isUpsert);
        options.hint(hint);
        options.writeConcern(writeConcern);

        return this.updateOne(options);
    }

    default TUpdateResult updateOne(TKey id,
                                    final Function<UpdateBuilder<TEntity>, Bson> updateBuilder) {
        final Function<FilterBuilder<TEntity>, Bson> filterBuilder = f -> f.eq(BsonConstant.PRIMARY_KEY_NAME, id).build();
        return this.updateOne(filterBuilder, updateBuilder, false);
    }

    default TUpdateResult updateOne(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                    final Function<UpdateBuilder<TEntity>, Bson> updateBuilder) {
        return this.updateOne(filterBuilder, updateBuilder, false);
    }

    default TUpdateResult updateOne(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                    final Function<UpdateBuilder<TEntity>, Bson> updateBuilder,
                                    final boolean isUpsert) {
        return this.updateOne(filterBuilder, updateBuilder, isUpsert, null);
    }

    default TUpdateResult updateOne(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                    final Function<UpdateBuilder<TEntity>, Bson> updateBuilder,
                                    final boolean isUpsert,
                                    final Function<HintBuilder<TEntity>, Bson> hintBuilder) {
        return this.updateOne(filterBuilder, updateBuilder, isUpsert, hintBuilder, null);
    }

    default TUpdateResult updateOne(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                    final Function<UpdateBuilder<TEntity>, Bson> updateBuilder,
                                    final boolean isUpsert,
                                    final Function<HintBuilder<TEntity>, Bson> hintBuilder,
                                    final WriteConcern writeConcern) {

        final UpdateOptions options = new UpdateOptions();
        if (!Objects.isNull(filterBuilder)) {
            options.filter(
                    filterBuilder.apply(FilterBuilder.empty(modifyProxy().getEntityInformation().getEntityType()))
            );
        }
        if (!Objects.isNull(updateBuilder)) {
            options.update(
                    updateBuilder.apply(UpdateBuilder.empty(modifyProxy().getEntityInformation().getEntityType()))
            );
        }
        if (!Objects.isNull(hintBuilder)) {
            options.hint(
                    hintBuilder.apply(HintBuilder.empty(modifyProxy().getEntityInformation().getEntityType()))
            );
        }
        options.upsert(isUpsert);
        options.writeConcern(writeConcern);

        return this.updateOne(options);
    }


    /**
     * 修改单条数据
     *
     * @param options UpdateOptions
     * @return
     */
    default TUpdateResult updateOne(final UpdateOptions options) {

        return modifyProxy().doUpdate(options, UpdateType.ONE);
    }

    /**
     * 修改多条数据
     *
     * @param filter
     * @param update
     * @return
     */
    default TUpdateResult updateMany(final Bson filter, final Bson update) {
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
    default TUpdateResult updateMany(final Bson filter, final Bson update, final WriteConcern writeConcern) {
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
    default TUpdateResult updateMany(final Bson filter, final Bson update, Bson hint, final WriteConcern writeConcern) {

        UpdateOptions options = new UpdateOptions();
        options.filter(filter);
        options.update(update);
        options.hint(hint);
        options.writeConcern(writeConcern);

        return this.updateMany(options);
    }

    default TUpdateResult updateMany(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                     final Function<UpdateBuilder<TEntity>, Bson> updateBuilder) {
        return this.updateMany(filterBuilder, updateBuilder, null);
    }

    default TUpdateResult updateMany(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                     final Function<UpdateBuilder<TEntity>, Bson> updateBuilder,
                                     final Function<HintBuilder<TEntity>, Bson> hintBuilder) {
        return this.updateMany(filterBuilder, updateBuilder, hintBuilder, null);
    }

    default TUpdateResult updateMany(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                     final Function<UpdateBuilder<TEntity>, Bson> updateBuilder,
                                     final Function<HintBuilder<TEntity>, Bson> hintBuilder,
                                     final WriteConcern writeConcern) {

        final UpdateOptions options = new UpdateOptions();

        if (!Objects.isNull(filterBuilder)) {
            options.filter(
                    filterBuilder.apply(FilterBuilder.empty(modifyProxy().getEntityInformation().getEntityType()))
            );
        }
        if (!Objects.isNull(updateBuilder)) {
            options.update(
                    updateBuilder.apply(UpdateBuilder.empty(modifyProxy().getEntityInformation().getEntityType()))
            );
        }
        if (!Objects.isNull(hintBuilder)) {
            options.hint(
                    hintBuilder.apply(HintBuilder.empty(modifyProxy().getEntityInformation().getEntityType()))
            );
        }
        options.writeConcern(writeConcern);

        return this.updateMany(options);

    }

    /**
     * 修改多条数据
     *
     * @param options
     * @return
     */
    default TUpdateResult updateMany(final UpdateOptions options) {

        options.upsert(false);
        return modifyProxy().doUpdate(options, UpdateType.MANY);
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
    default TEntity findOneAndUpdate(final Bson filter, final Bson update) {
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
    default TEntity findOneAndUpdate(final Bson filter, final Bson update, final boolean isUpsert, final Bson sort) {

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
    default TEntity findOneAndUpdate(final Bson filter, final Bson update, final boolean isUpsert, final Bson sort, final Bson hint) {

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
    default TEntity findOneAndUpdate(final FindOneAndUpdateOptions options) {
        options.returnDocument(ReturnDocument.AFTER);

        return modifyProxy().doFindOneAndUpdate(options);
    }

    /**
     * 找到并更新
     *
     * @param filter
     * @param entity
     * @return
     */
    default TEntity findOneAndUpdate(final Bson filter, final TEntity entity) {
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
    default TEntity findOneAndUpdate(final Bson filter, final TEntity entity, final boolean isUpsert, final Bson sort) {
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
    TEntity findOneAndUpdate(final Bson filter, final TEntity entity, final boolean isUpsert, final Bson sort, final Bson hint);

    /**
     * 找到并删除
     *
     * @param filter
     * @return
     */
    default TEntity findOneAndDelete(final Bson filter) {
        return this.findOneAndDelete(filter, (Bson) null);
    }

    /**
     * 找到并删除
     *
     * @param filter
     * @param sort
     * @return
     */
    default TEntity findOneAndDelete(final Bson filter, final Bson sort) {
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
    default TEntity findOneAndDelete(final Bson filter, final Bson sort, final Bson hint) {

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
    default TEntity findOneAndDelete(final FindOneAndDeleteOptions option) {

        return modifyProxy().doFindOneAndDelete(option);
    }

    //#endregion

    //#region delete

    /**
     * @param id 主键
     * @return
     */
    default TDeleteResult deleteOne(final TKey id) {
        return this.deleteOne(id, (WriteConcern) null);
    }

    /**
     * @param id           主键
     * @param writeConcern WriteConcern
     * @return
     */
    default TDeleteResult deleteOne(final TKey id, final WriteConcern writeConcern) {
        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        return this.deleteOne(filter, writeConcern);
    }

    /**
     * @param filter
     * @return
     */
    default TDeleteResult deleteOne(final Bson filter) {
        return this.deleteOne(filter, (WriteConcern) null);
    }

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    default TDeleteResult deleteOne(final Bson filter, final WriteConcern writeConcern) {
        return this.deleteOne(filter, (Bson) null, writeConcern);
    }

    /**
     * @param filter
     * @param hint
     * @param writeConcern WriteConcern
     * @return
     */
    default TDeleteResult deleteOne(final Bson filter, final Bson hint, final WriteConcern writeConcern) {

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
    default TDeleteResult deleteOne(final DeleteOptions options) {
        return modifyProxy().doDeleteOne(options);
    }

    /**
     * @param filter
     * @return
     */
    default TDeleteResult deleteMany(final Bson filter) {
        return this.deleteMany(filter, null);
    }

    /**
     * @param filter
     * @param writeConcern WriteConcern
     * @return
     */
    default TDeleteResult deleteMany(final Bson filter, final WriteConcern writeConcern) {
        return this.deleteMany(filter, (Bson) null, writeConcern);
    }

    /**
     * @param filter
     * @param hint
     * @param writeConcern WriteConcern
     * @return
     */
    default TDeleteResult deleteMany(final Bson filter, final Bson hint, final WriteConcern writeConcern) {

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
    default TDeleteResult deleteMany(final DeleteOptions options) {
        return modifyProxy().doDeleteMany(options);
    }


    //#endregion

    ModifyProxy<TEntity, TKey, TInsertOneResult, TInsertManyResult, TUpdateResult, TDeleteResult> modifyProxy();

    abstract class ModifyProxy<TEntity extends Entity<TKey>, TKey, TInsertOneResult, TInsertManyResult, TUpdateResult, TDeleteResult> {

        protected abstract EntityInformation<TEntity, TKey> getEntityInformation();

        protected abstract IdGenerator<TKey> getIdGenerator();

        protected abstract TInsertOneResult doInsert(final TEntity entity, final WriteConcern writeConcern);

        protected abstract TInsertManyResult doInsertBatch(final List<TEntity> entities, final WriteConcern writeConcern);

        protected abstract TUpdateResult doUpdate(@NonNull final UpdateOptions options, final UpdateType updateType);

        protected abstract TEntity doFindOneAndUpdate(final FindOneAndUpdateOptions options);

        protected abstract TEntity doFindOneAndDelete(@NonNull final FindOneAndDeleteOptions options);

        protected abstract TDeleteResult doDeleteOne(final DeleteOptions options);

        protected abstract TDeleteResult doDeleteMany(final DeleteOptions options);

    }
}
