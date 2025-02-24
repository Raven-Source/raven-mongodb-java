package org.raven.mongodb.criteria;

import com.mongodb.WriteConcern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;
import org.raven.mongodb.util.BsonUtils;

/**
 * @author by yanfeng
 * date 2021/9/22 21:34
 */
@Getter
@Setter
@Accessors(fluent = true)
public class FindOneAndDeleteOptions extends BaseModifyOptions<FindOneAndDeleteOptions> {

    private Bson sort;

    /**
     *
     */
    public FindOneAndDeleteOptions() {
    }

    /**
     * @return FindOneAndDeleteOptions
     */
    public static FindOneAndDeleteOptions create() {
        return new FindOneAndDeleteOptions();
    }

    @Override
    public Bson toBson() {
        return BsonUtils.combine(
                super.toBson(),
                BsonUtils.combine("sort", sort)
        );
    }

    public static class Builder<TEntity>
            implements SortBuilderAdapter<TEntity, Builder<TEntity>>
            , FilterBuilderAdapter<TEntity, Builder<TEntity>> {

        private final Class<TEntity> entityClass;
        private final FindOneAndDeleteOptions options;

        private Builder(Class<TEntity> entityClass) {
            this.entityClass = entityClass;
            this.options = FindOneAndDeleteOptions.create();
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
        public Class<TEntity> getEntityType() {
            return entityClass;
        }

        public FindOneAndDeleteOptions build() {
            return options;
        }


    }

}
