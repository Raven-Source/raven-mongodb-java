package org.raven.mongodb.repository.query;

import com.mongodb.client.model.Filters;
import org.bson.codecs.pojo.ClassModelUtils;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * @author by yanfeng
 * date 2021/9/12 23:01
 */
public class FilterBuilder<TEntity> {

    private Class<TEntity> entityClass;
    private List<Bson> bsons = new ArrayList<>();

    public FilterBuilder(final Class<TEntity> entityClass) {
        this.entityClass = entityClass;
    }

    public <TItem> FilterBuilder<TEntity> eq(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.eq(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> ne(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.ne(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> gt(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.gt(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> gte(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.gte(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> lt(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.lt(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> lte(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.lte(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> in(final String fieldName, final TItem... values) {
        assert fieldName != null;
        bsons.add(Filters.in(ClassModelUtils.getWriteName(entityClass, fieldName), values));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> in(final String fieldName, final Iterable<TItem> values) {
        assert fieldName != null;
        bsons.add(Filters.in(ClassModelUtils.getWriteName(entityClass, fieldName), values));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> nin(final String fieldName, final TItem... values) {
        assert fieldName != null;
        bsons.add(Filters.nin(ClassModelUtils.getWriteName(entityClass, fieldName), values));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> nin(final String fieldName, final Iterable<TItem> values) {
        assert fieldName != null;
        bsons.add(Filters.nin(ClassModelUtils.getWriteName(entityClass, fieldName), values));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> or(final FilterBuilder<TEntity> filterBuilder) {
        assert filterBuilder != null;
        bsons.add(Filters.or(filterBuilder.bsons));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> and(final FilterBuilder<TEntity> filterBuilder) {
        assert filterBuilder != null;
        bsons.add(Filters.and(filterBuilder.bsons));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> not(final FilterBuilder<TEntity> filterBuilder) {
        assert filterBuilder != null;
        bsons.add(Filters.not(filterBuilder.build()));
        return this;
    }


    public <TItem> FilterBuilder<TEntity> nor(final FilterBuilder<TEntity> filterBuilder) {
        assert filterBuilder != null;
        bsons.add(Filters.nor(filterBuilder.bsons));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> condition(BooleanSupplier supplier, Consumer<FilterBuilder<TEntity>> filterBuilderConsumer) {
        if (supplier.getAsBoolean()) {
            filterBuilderConsumer.accept(this);
        }

        return this;

    }

    public Bson build() {
        if (bsons.size() == 1) {
            return bsons.get(0);
        } else {
            return Filters.and(bsons);
        }
    }

}
