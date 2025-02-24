package org.raven.mongodb.criteria;

import com.mongodb.WriteConcern;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

/**
 * @author yi.liang
 * date 2021/9/22 16:56
 */
@Getter
@Accessors(fluent = true)
public class UpdateOptions extends BaseUpdateOptions<UpdateOptions> {

    public UpdateOptions() {
    }

    /**
     * @return UpdateOptions
     */
    public static UpdateOptions create() {
        return new UpdateOptions();
    }

    public static class Builder<TEntity>
            implements FilterBuilderAdapter<TEntity, Builder<TEntity>>
            , UpdateBuilderAdapter<TEntity, Builder<TEntity>> {

        private final Class<TEntity> entityClass;
        private final UpdateOptions options;

        private Builder(Class<TEntity> entityClass) {
            this.entityClass = entityClass;
            this.options = UpdateOptions.create();
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

        public Builder<TEntity> writeConcern(WriteConcern writeConcern) {
            options.writeConcern(writeConcern);
            return this;
        }

        @Override
        public Builder<TEntity> update(final Bson bson) {
            options.update(bson);
            return this;
        }

        public Builder<TEntity> upsert(boolean upsert) {
            options.upsert(upsert);
            return this;
        }

        @Override
        public Class<TEntity> getEntityType() {
            return entityClass;
        }

        public UpdateOptions build() {
            return options;
        }

    }
}
