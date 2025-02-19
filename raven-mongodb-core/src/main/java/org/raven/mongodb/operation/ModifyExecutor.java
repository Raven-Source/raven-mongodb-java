package org.raven.mongodb.operation;

import com.mongodb.WriteConcern;
import org.raven.commons.data.Entity;
import org.raven.mongodb.*;

import java.util.List;

public interface ModifyExecutor<TEntity extends Entity<TKey>, TKey, TInsertOneResult, TInsertManyResult, TUpdateResult, TFindOneAndModifyResult, TDeleteResult> {

    EntityInformation<TEntity, TKey> getEntityInformation();

    TInsertOneResult doInsert(final TEntity entity, final WriteConcern writeConcern);

    TInsertManyResult doInsertBatch(final List<TEntity> entities, final WriteConcern writeConcern);

    TUpdateResult doUpdate(final UpdateOptions options, final UpdateType updateType);

    TFindOneAndModifyResult doFindOneAndUpdate(final FindOneAndUpdateOptions options);

    TFindOneAndModifyResult doFindOneAndDelete(final FindOneAndDeleteOptions options);

    TDeleteResult doDeleteOne(final DeleteOptions options);

    TDeleteResult doDeleteMany(final DeleteOptions options);

}
