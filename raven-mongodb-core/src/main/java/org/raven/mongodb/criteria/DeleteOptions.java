package org.raven.mongodb.criteria;

import com.mongodb.WriteConcern;
import org.bson.conversions.Bson;

/**
 * @author yi.liang
 * date 2021.09.23 00:16
 */
public class DeleteOptions extends BaseModifyOptions<DeleteOptions> implements CommandOptions {

    /**
     *
     */
    public DeleteOptions() {
    }

    /**
     * @return DeleteOptions
     */
    public static DeleteOptions create() {
        return new DeleteOptions();
    }

    public static class Builder<TEntity>
            implements FilterBuilderAdapter<TEntity, Builder<TEntity>> {

        private final Class<TEntity> entityClass;
        private final DeleteOptions options;

        private Builder(Class<TEntity> entityClass) {
            this.entityClass = entityClass;
            this.options = DeleteOptions.create();
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
        public Class<TEntity> getEntityType() {
            return entityClass;
        }

        public DeleteOptions build() {
            return options;
        }

    }
}
