package org.raven.mongodb.criteria;

import com.mongodb.ReadPreference;
import lombok.*;
import lombok.experimental.Accessors;
import org.bson.BsonInt32;
import org.bson.conversions.Bson;
import org.raven.mongodb.util.BsonUtils;

/**
 * @author yi.liang
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CountOptions extends BaseFindOptions<CountOptions> {

    private int limit;
    private int skip;

    public CountOptions() {
    }

    @Override
    public Bson toBson() {
        return BsonUtils.combine(
                super.toBson(),
                BsonUtils.combine("limit", new BsonInt32(limit)),
                BsonUtils.combine("skip", new BsonInt32(skip))
        );
    }

    /**
     * @return CountOptions
     */
    public static CountOptions create() {
        return new CountOptions();
    }

    public static class Builder<TEntity>
            implements FilterBuilderAdapter<TEntity, Builder<TEntity>> {

        private final Class<TEntity> entityClass;
        private final CountOptions options;

        private Builder(Class<TEntity> entityClass) {
            this.entityClass = entityClass;
            this.options = CountOptions.create();
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

        public Builder<TEntity> limit(int limit) {
            options.limit(limit);
            return this;
        }

        public Builder<TEntity> skip(int skip) {
            options.skip(skip);
            return this;
        }

        public Builder<TEntity> readPreference(ReadPreference readPreference) {
            options.readPreference(readPreference);
            return this;
        }

        @Override
        public Class<TEntity> getEntityType() {
            return entityClass;
        }

        public CountOptions build() {
            return options;
        }
    }

}
