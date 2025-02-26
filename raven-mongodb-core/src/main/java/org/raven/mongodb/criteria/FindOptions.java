package org.raven.mongodb.criteria;

import com.mongodb.ReadPreference;
import com.mongodb.client.model.Filters;
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
public class FindOptions extends BaseFindOptions<FindOptions> {

//    private List<String> includeFields;
//    private List<String> excludeFields;

    private Bson projection;
    private Bson sort;
    private int limit;
    private int skip;

    public FindOptions() {
        filter(Filters.empty());
    }

    /**
     * @return FindOptions
     */
    public static FindOptions create() {
        return new FindOptions();
    }


    @Override
    public Bson toBson() {
        return BsonUtils.combine(
                super.toBson(),
                BsonUtils.combine("projection", projection),
                BsonUtils.combine("sort", sort),
                BsonUtils.combine("limit", new BsonInt32(limit)),
                BsonUtils.combine("skip", new BsonInt32(skip))
        );
    }

    public static class Builder<TEntity>
            implements SortBuilderAdapter<TEntity, Builder<TEntity>>
            , FilterBuilderAdapter<TEntity, Builder<TEntity>>
            , ProjectionBuilderAdapter<TEntity, Builder<TEntity>> {

        private final Class<TEntity> entityClass;
        private final FindOptions options;

        private Builder(Class<TEntity> entityClass) {
            this.entityClass = entityClass;
            this.options = FindOptions.create();
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
        public Builder<TEntity> projection(final Bson bson) {
            options.projection(bson);
            return this;
        }

        @Override
        public Builder<TEntity> sort(final Bson bson) {
            options.sort(bson);
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

        public FindOptions build() {
            return options;
        }
    }
}
