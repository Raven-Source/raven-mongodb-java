package org.raven.mongodb.criteria;

import com.mongodb.WriteConcern;
import com.mongodb.client.model.ReturnDocument;
import lombok.*;
import lombok.experimental.Accessors;
import org.bson.BsonString;
import org.bson.conversions.Bson;
import org.raven.mongodb.util.BsonUtils;

/**
 * @author yi.liang
 * date 2021/9/22 20:21
 */
@Getter
@Setter
@Accessors(fluent = true)
public class FindOneAndUpdateOptions extends BaseUpdateOptions<FindOneAndUpdateOptions> {

    private Bson sort;

    private ReturnDocument returnDocument = ReturnDocument.AFTER;

    /**
     *
     */
    public FindOneAndUpdateOptions() {
    }

    /**
     * @return FindOneAndUpdateOptions
     */
    public static FindOneAndUpdateOptions create() {
        return new FindOneAndUpdateOptions();
    }


    @Override
    public Bson toBson() {
        return BsonUtils.combine(
                super.toBson(),
                BsonUtils.combine("sort", sort),
                BsonUtils.combine("returnDocument", new BsonString(returnDocument.name()))
        );
    }

    public static class Builder<TEntity>
            implements SortBuilderAdapter<TEntity, Builder<TEntity>>
            , FilterBuilderAdapter<TEntity, Builder<TEntity>>
            , UpdateBuilderAdapter<TEntity, Builder<TEntity>> {

        private final Class<TEntity> entityClass;
        private final FindOneAndUpdateOptions options;

        private Builder(Class<TEntity> entityClass) {
            this.entityClass = entityClass;
            this.options = FindOneAndUpdateOptions.create();
        }

        public static <TEntity> Builder<TEntity> create(final Class<TEntity> entityClass) {
            return new Builder<>(entityClass);
        }

        @Override
        public Builder<TEntity> filter(final Bson bson) {
            options.filter(bson);
            return this;
        }

        @Override
        public Builder<TEntity> hint(final Bson bson) {
            options.hint(bson);
            return this;
        }

        @Override
        public Builder<TEntity> sort(final Bson bson) {
            options.sort(bson);
            return this;
        }

        public Builder<TEntity> writeConcern(final WriteConcern writeConcern) {
            options.writeConcern(writeConcern);
            return this;
        }

        @Override
        public Builder<TEntity> update(final Bson bson) {
            options.update(bson);
            return this;
        }

        public Builder<TEntity> upsert(final boolean upsert) {
            options.upsert(upsert);
            return this;
        }

        @Override
        public Class<TEntity> getEntityType() {
            return entityClass;
        }

        public FindOneAndUpdateOptions build() {
            return options;
        }


    }

}
