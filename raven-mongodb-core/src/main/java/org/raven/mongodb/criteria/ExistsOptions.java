package org.raven.mongodb.criteria;

import com.mongodb.ReadPreference;
import org.bson.conversions.Bson;

/**
 * @author yi.liang
 */
public class ExistsOptions extends BaseFindOptions<ExistsOptions> {

    public ExistsOptions() {
    }

    /**
     * @return ExistsOptions
     */
    public static ExistsOptions create() {
        return new ExistsOptions();
    }

    public static class Builder<TEntity>
            implements FilterBuilderAdapter<TEntity, Builder<TEntity>> {

        private final Class<TEntity> entityClass;
        private final ExistsOptions options;

        private Builder(Class<TEntity> entityClass) {
            this.entityClass = entityClass;
            this.options = ExistsOptions.create();
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

        public Builder<TEntity> readPreference(ReadPreference readPreference) {
            options.readPreference(readPreference);
            return this;
        }

        @Override
        public Class<TEntity> getEntityType() {
            return entityClass;
        }

        public ExistsOptions build() {
            return options;
        }
    }
}
