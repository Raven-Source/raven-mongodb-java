package org.raven.mongodb.operation;

import com.mongodb.WriteConcern;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReturnDocument;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.*;
import org.raven.mongodb.contants.BsonConstant;
import org.raven.mongodb.criteria.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author by yanfeng
 * date 2021/10/30 21:39
 */
public interface WriteOperation<TEntity extends Entity<TKey>, TKey, TInsertOneResult, TInsertManyResult, TUpdateResult, TFindOneAndModifyResult, TDeleteResult> {

    //#region insert

    /**
     * @param entity TEntity
     * @return InsertOneResult
     */
    default TInsertOneResult insert(final TEntity entity) {
        return this.insert(entity, null);
    }

    /**
     * @param entity       TEntity
     * @param writeConcern {{@link WriteConcern}}
     * @return InsertOneResult
     */
    default TInsertOneResult insert(final TEntity entity, final WriteConcern writeConcern) {
        return modifyExecutor().doInsert(entity, writeConcern);
    }

    /**
     * @param entities entities
     * @return InsertManyResult
     */
    default TInsertManyResult insertBatch(final List<TEntity> entities) {
        return this.insertBatch(entities, null);
    }

    /**
     * @param entities     entities
     * @param writeConcern {{@link WriteConcern}}
     * @return InsertManyResult
     */
    default TInsertManyResult insertBatch(final List<TEntity> entities, final WriteConcern writeConcern) {

        return modifyExecutor().doInsertBatch(entities, writeConcern);
    }

    //#endregion

    //#region update

    /**
     * 修改单条数据
     *
     * @param filter       conditions
     * @param updateEntity TEntity
     * @return UpdateResult
     */
    default TUpdateResult updateOne(final Bson filter, final TEntity updateEntity) {
        return this.updateOne(filter, updateEntity, false, null);
    }

    /**
     * 修改单条数据
     *
     * @param filter       conditions
     * @param updateEntity updateEntity
     * @param isUpsert     default false
     * @return UpdateResult
     */
    default TUpdateResult updateOne(final Bson filter, final TEntity updateEntity, final boolean isUpsert) {

        return this.updateOne(filter, updateEntity, isUpsert, null);
    }

    /**
     * 修改单条数据
     *
     * @param filter       conditions
     * @param updateEntity updateEntity
     * @param isUpsert     default false
     * @param writeConcern {{@link WriteConcern}}
     * @return UpdateResult
     */
    default TUpdateResult updateOne(final Bson filter, final TEntity updateEntity, final boolean isUpsert, final WriteConcern writeConcern) {

        return this.updateOne(filter, updateEntity, isUpsert, (Bson) null, writeConcern);
    }

    /**
     * 修改单条数据
     *
     * @param filter       conditions
     * @param updateEntity updateEntity
     * @param isUpsert     default false
     * @param hint         hint
     * @param writeConcern {{@link WriteConcern}}
     * @return UpdateResult
     */
    TUpdateResult updateOne(final Bson filter, final TEntity updateEntity, final boolean isUpsert, final Bson hint, final WriteConcern writeConcern);

    /**
     * 修改单条数据
     *
     * @param id     TKey
     * @param update update
     * @return UpdateResult
     */
    default TUpdateResult updateOne(final TKey id, final Bson update) {

        final Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        return this.updateOne(filter, update, false, (WriteConcern) null);
    }

    /**
     * 修改单条数据
     *
     * @param filter conditions
     * @param update update
     * @return UpdateResult
     */
    default TUpdateResult updateOne(final Bson filter, final Bson update) {
        return this.updateOne(filter, update, false, (WriteConcern) null);
    }

    /**
     * 修改单条数据
     *
     * @param filter   conditions
     * @param update   update
     * @param isUpsert default false
     * @return UpdateResult
     */
    default TUpdateResult updateOne(final Bson filter, final Bson update, final boolean isUpsert) {
        return this.updateOne(filter, update, isUpsert, (WriteConcern) null);
    }

    /**
     * 修改单条数据
     *
     * @param filter       conditions
     * @param update       update
     * @param isUpsert     default false
     * @param writeConcern {{@link WriteConcern}}
     * @return UpdateResult
     */
    default TUpdateResult updateOne(final Bson filter, final Bson update, final boolean isUpsert, final WriteConcern writeConcern) {
        return this.updateOne(filter, update, isUpsert, (Bson) null, writeConcern);
    }

    /**
     * 修改单条数据
     *
     * @param filter       conditions
     * @param update       update
     * @param isUpsert     default false
     * @param hint         hint
     * @param writeConcern {{@link WriteConcern}}
     * @return UpdateResult
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
                                    final UpdateExpression<TEntity> updateExpression) {
        final FilterExpression<TEntity> filterExpression = f -> f.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        return this.updateOne(filterExpression, updateExpression, false);
    }

    default TUpdateResult updateOne(final FilterExpression<TEntity> filterExpression,
                                    final UpdateExpression<TEntity> updateExpression) {
        return this.updateOne(filterExpression, updateExpression, false);
    }

    default TUpdateResult updateOne(final FilterExpression<TEntity> filterExpression,
                                    final UpdateExpression<TEntity> updateExpression,
                                    final boolean isUpsert) {
        return this.updateOne(filterExpression, updateExpression, isUpsert, null);
    }

    default TUpdateResult updateOne(final FilterExpression<TEntity> filterExpression,
                                    final UpdateExpression<TEntity> updateExpression,
                                    final boolean isUpsert,
                                    final HintExpression<TEntity> hintExpression) {
        return this.updateOne(filterExpression, updateExpression, isUpsert, hintExpression, null);
    }

    default TUpdateResult updateOne(final FilterExpression<TEntity> filterExpression,
                                    final UpdateExpression<TEntity> updateExpression,
                                    final boolean isUpsert,
                                    final HintExpression<TEntity> hintExpression,
                                    final WriteConcern writeConcern) {

        final UpdateOptions options = new UpdateOptions();
        if (!Objects.isNull(filterExpression)) {
            options.filter(
                    filterExpression.toBson(modifyExecutor().getEntityType())
            );
        }
        if (!Objects.isNull(updateExpression)) {
            options.update(
                    updateExpression.toBson(modifyExecutor().getEntityType())
            );
        }
        if (!Objects.isNull(hintExpression)) {
            options.hint(
                    hintExpression.toBson(modifyExecutor().getEntityType())
            );
        }
        options.upsert(isUpsert);

        if (writeConcern != null) {
            options.writeConcern(writeConcern);
        }

        return this.updateOne(options);
    }


    /**
     * 修改单条数据
     *
     * @param options UpdateOptions
     * @return UpdateResult
     */
    default TUpdateResult updateOne(final UpdateOptions options) {

        return modifyExecutor().doUpdate(options, UpdateType.ONE);
    }

    /**
     * 修改多条数据
     *
     * @param filter conditions
     * @param update update
     * @return UpdateResult
     */
    default TUpdateResult updateMany(final Bson filter, final Bson update) {
        return this.updateMany(filter, update, (WriteConcern) null);
    }

    /**
     * 修改多条数据
     *
     * @param filter       conditions
     * @param update       update
     * @param writeConcern writeConcern
     * @return UpdateResult
     */
    default TUpdateResult updateMany(final Bson filter, final Bson update, final WriteConcern writeConcern) {
        return this.updateMany(filter, update, (Bson) null, writeConcern);
    }

    /**
     * 修改多条数据
     *
     * @param filter       conditions
     * @param update       update
     * @param hint         hint
     * @param writeConcern writeConcern
     * @return UpdateResult
     */
    default TUpdateResult updateMany(final Bson filter, final Bson update, Bson hint, final @Nullable WriteConcern writeConcern) {

        UpdateOptions options = new UpdateOptions();
        options.filter(filter);
        options.update(update);
        options.hint(hint);
        if (writeConcern != null) {
            options.writeConcern(writeConcern);
        }

        return this.updateMany(options);
    }

    default TUpdateResult updateMany(final FilterExpression<TEntity> filterExpression,
                                     final UpdateExpression<TEntity> updateExpression) {
        return this.updateMany(filterExpression, updateExpression, null);
    }

    default TUpdateResult updateMany(final FilterExpression<TEntity> filterExpression,
                                     final UpdateExpression<TEntity> updateExpression,
                                     final HintExpression<TEntity> hintExpression) {
        return this.updateMany(filterExpression, updateExpression, hintExpression, null);
    }

    default TUpdateResult updateMany(final FilterExpression<TEntity> filterExpression,
                                     final UpdateExpression<TEntity> updateExpression,
                                     final HintExpression<TEntity> hintExpression,
                                     final WriteConcern writeConcern) {

        final UpdateOptions options = new UpdateOptions();

        if (!Objects.isNull(filterExpression)) {
            options.filter(
                    filterExpression.toBson(modifyExecutor().getEntityType())
            );
        }
        if (!Objects.isNull(updateExpression)) {
            options.update(
                    updateExpression.toBson(modifyExecutor().getEntityType())
            );
        }
        if (!Objects.isNull(hintExpression)) {
            options.hint(
                    hintExpression.toBson(modifyExecutor().getEntityType())
            );
        }
        options.writeConcern(writeConcern);

        return this.updateMany(options);

    }

    /**
     * 修改多条数据
     *
     * @param options options
     * @return UpdateResult
     */
    default TUpdateResult updateMany(final UpdateOptions options) {

        options.upsert(false);
        return modifyExecutor().doUpdate(options, UpdateType.MANY);
    }

    //#endregion

    //#region findAndModify

    /**
     * 找到并更新
     *
     * @param filter conditions
     * @param update update
     * @return FindOneAndModifyResult
     */
    default TFindOneAndModifyResult findOneAndUpdate(final Bson filter, final Bson update) {
        return this.findOneAndUpdate(filter, update, false, null);
    }

    /**
     * 找到并更新
     *
     * @param filter   conditions
     * @param update   update
     * @param isUpsert default false
     * @param sort     sort
     * @return FindOneAndModifyResult
     */
    default TFindOneAndModifyResult findOneAndUpdate(final Bson filter, final Bson update, final boolean isUpsert, final Bson sort) {

        return this.findOneAndUpdate(filter, update, isUpsert, sort, null);
    }

    /**
     * 找到并更新
     *
     * @param filter   conditions
     * @param update   update
     * @param isUpsert default false
     * @param sort     sort
     * @param hint     hint
     * @return FindOneAndModifyResult
     */
    default TFindOneAndModifyResult findOneAndUpdate(final Bson filter, final Bson update, final boolean isUpsert, final Bson sort, final Bson hint) {

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
     * @return FindOneAndModifyResult
     */
    default TFindOneAndModifyResult findOneAndUpdate(final FindOneAndUpdateOptions options) {
        options.returnDocument(ReturnDocument.AFTER);

        return modifyExecutor().doFindOneAndUpdate(options);
    }

    /**
     * 找到并更新
     *
     * @param filter conditions
     * @param entity entity
     * @return FindOneAndModifyResult
     */
    default TFindOneAndModifyResult findOneAndUpdate(final Bson filter, final TEntity entity) {
        return this.findOneAndUpdate(filter, entity, false, null);
    }

    /**
     * 找到并更新
     *
     * @param filter   conditions
     * @param entity   entity
     * @param isUpsert default false
     * @param sort     sort
     * @return FindOneAndModifyResult
     */
    default TFindOneAndModifyResult findOneAndUpdate(final Bson filter, final TEntity entity, final boolean isUpsert, final Bson sort) {
        return this.findOneAndUpdate(filter, entity, isUpsert, sort, null);
    }

    /**
     * 找到并更新
     *
     * @param filter   conditions
     * @param entity   entity
     * @param isUpsert default false
     * @param sort     sort
     * @param hint     hint
     * @return FindOneAndModifyResult
     */
    TFindOneAndModifyResult findOneAndUpdate(final Bson filter, final TEntity entity, final boolean isUpsert, final Bson sort, final Bson hint);

    /**
     * 找到并删除
     *
     * @param filter conditions
     * @return FindOneAndModifyResult
     */
    default TFindOneAndModifyResult findOneAndDelete(final Bson filter) {
        return this.findOneAndDelete(filter, (Bson) null);
    }

    /**
     * 找到并删除
     *
     * @param filter conditions
     * @param sort   sort
     * @return FindOneAndModifyResult
     */
    default TFindOneAndModifyResult findOneAndDelete(final Bson filter, final Bson sort) {
        return this.findOneAndDelete(filter, sort, (Bson) null);
    }

    /**
     * 找到并删除
     *
     * @param filter conditions
     * @param sort   sort
     * @param hint   hint
     * @return FindOneAndModifyResult
     */
    default TFindOneAndModifyResult findOneAndDelete(final Bson filter, final Bson sort, final Bson hint) {

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
     * @return FindOneAndModifyResult
     */
    default TFindOneAndModifyResult findOneAndDelete(final FindOneAndDeleteOptions option) {

        return modifyExecutor().doFindOneAndDelete(option);
    }

    //#endregion

    //#region delete

    /**
     * @param id id
     * @return DeleteResult
     */
    default TDeleteResult deleteOne(final TKey id) {
        return this.deleteOne(id, (WriteConcern) null);
    }

    /**
     * @param id           id
     * @param writeConcern WriteConcern
     * @return DeleteResult
     */
    default TDeleteResult deleteOne(final TKey id, final WriteConcern writeConcern) {
        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        return this.deleteOne(filter, writeConcern);
    }

    /**
     * @param filter conditions
     * @return DeleteResult
     */
    default TDeleteResult deleteOne(final Bson filter) {
        return this.deleteOne(filter, (WriteConcern) null);
    }

    /**
     * @param filter       conditions
     * @param writeConcern WriteConcern
     * @return DeleteResult
     */
    default TDeleteResult deleteOne(final Bson filter, final WriteConcern writeConcern) {
        return this.deleteOne(filter, (Bson) null, writeConcern);
    }

    /**
     * @param filter       conditions
     * @param hint         hint
     * @param writeConcern WriteConcern
     * @return DeleteResult
     */
    default TDeleteResult deleteOne(final Bson filter, final Bson hint, final WriteConcern writeConcern) {

        DeleteOptions options = new DeleteOptions();
        options.hint(hint);
        options.filter(filter);
        if (writeConcern != null) {
            options.writeConcern(writeConcern);
        }

        return this.deleteOne(options);
    }

    /**
     * @param options DeleteOptions
     * @return DeleteResult
     */
    default TDeleteResult deleteOne(final DeleteOptions options) {
        return modifyExecutor().doDeleteOne(options);
    }

    /**
     * @param filter conditions
     * @return DeleteResult
     */
    default TDeleteResult deleteMany(final Bson filter) {
        return this.deleteMany(filter, null);
    }

    /**
     * @param filter       conditions
     * @param writeConcern WriteConcern
     * @return DeleteResult
     */
    default TDeleteResult deleteMany(final Bson filter, final WriteConcern writeConcern) {
        return this.deleteMany(filter, (Bson) null, writeConcern);
    }

    /**
     * @param filter       conditions
     * @param hint         hint
     * @param writeConcern WriteConcern
     * @return DeleteResult
     */
    default TDeleteResult deleteMany(final Bson filter, final Bson hint, final WriteConcern writeConcern) {

        DeleteOptions options = new DeleteOptions();
        options.hint(hint);
        options.filter(filter);
        if (writeConcern != null) {
            options.writeConcern(writeConcern);
        }

        return this.deleteMany(options);
    }

    /**
     * @param options DeleteOptions
     * @return DeleteResult
     */
    default TDeleteResult deleteMany(final DeleteOptions options) {
        return modifyExecutor().doDeleteMany(options);
    }


    //#endregion

    ModifyExecutor<TEntity, TKey, TInsertOneResult, TInsertManyResult, TUpdateResult, TFindOneAndModifyResult, TDeleteResult> modifyExecutor();

}
