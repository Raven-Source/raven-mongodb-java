package org.raven.mongodb.operation;

import com.mongodb.WriteConcern;
import org.raven.commons.data.Entity;
import org.raven.mongodb.*;
import org.raven.mongodb.criteria.DeleteOptions;
import org.raven.mongodb.criteria.FindOneAndDeleteOptions;
import org.raven.mongodb.criteria.FindOneAndUpdateOptions;
import org.raven.mongodb.criteria.UpdateOptions;

import java.util.List;

public interface ModifyExecutor<TEntity extends Entity<TKey>, TKey, TInsertOneResult, TInsertManyResult, TUpdateResult, TFindOneAndModifyResult, TDeleteResult> extends EntityMetadata<TEntity> {

    TInsertOneResult doInsert(final TEntity entity, final WriteConcern writeConcern);

    TInsertManyResult doInsertMany(final List<TEntity> entities, final WriteConcern writeConcern);

    TUpdateResult doUpdate(final UpdateOptions options, final ExecuteType executeType);

    TDeleteResult doDelete(final DeleteOptions options, final ExecuteType executeType);

    TFindOneAndModifyResult doFindOneAndUpdate(final FindOneAndUpdateOptions options);

    TFindOneAndModifyResult doFindOneAndDelete(final FindOneAndDeleteOptions options);


}
