package org.raven.mongodb.repository.query;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static org.bson.codecs.pojo.ClassModelUtils.getWriteName;

/**
 * @author yi.liang
 * date 2021/9/12 23:01
 */
public class FilterBuilder<TEntity> {

    private final Class<TEntity> entityClass;
    private List<Bson> bsons = new ArrayList<>();

    public static <TEntity> FilterBuilder<TEntity> empty(final Class<TEntity> entityClass) {
        return new FilterBuilder<>(entityClass);
    }

    public FilterBuilder(final Class<TEntity> entityClass) {
        this.entityClass = entityClass;
    }

    public <TItem> FilterBuilder<TEntity> eq(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.eq(getWriteName(entityClass, fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> ne(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.ne(getWriteName(entityClass, fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> gt(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.gt(getWriteName(entityClass, fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> gte(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.gte(getWriteName(entityClass, fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> lt(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.lt(getWriteName(entityClass, fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> lte(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.lte(getWriteName(entityClass, fieldName), value));
        return this;
    }

    @SafeVarargs
    public final <TItem> FilterBuilder<TEntity> in(final String fieldName, final TItem... values) {
        assert fieldName != null;
        bsons.add(Filters.in(getWriteName(entityClass, fieldName), values));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> in(final String fieldName, final Iterable<TItem> values) {
        assert fieldName != null;
        bsons.add(Filters.in(getWriteName(entityClass, fieldName), values));
        return this;
    }

    @SafeVarargs
    public final <TItem> FilterBuilder<TEntity> nin(final String fieldName, final TItem... values) {
        assert fieldName != null;
        bsons.add(Filters.nin(getWriteName(entityClass, fieldName), values));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> nin(final String fieldName, final Iterable<TItem> values) {
        assert fieldName != null;
        bsons.add(Filters.nin(getWriteName(entityClass, fieldName), values));
        return this;
    }

    public FilterBuilder<TEntity> or(final FilterBuilder<TEntity> that) {
        assert that != null;

        Bson bson = Filters.or(Filters.and(bsons), that.build());
        bsons = new ArrayList<>();
        bsons.add(bson);
        return this;
    }

    public FilterBuilder<TEntity> and(final FilterBuilder<TEntity> that) {
        assert that != null;
        bsons.addAll(that.bsons);
        return this;
    }

    public FilterBuilder<TEntity> not(final FilterBuilder<TEntity> that) {
        assert that != null;
        bsons.add(Filters.not(that.build()));
        return this;
    }


    public FilterBuilder<TEntity> nor(final FilterBuilder<TEntity> that) {
        assert that != null;

        Bson bson = Filters.nor(Filters.and(bsons), that.build());
        bsons = new ArrayList<>();
        bsons.add(bson);
        return this;
    }

    public FilterBuilder<TEntity> condition(BooleanSupplier supplier, Consumer<FilterBuilder<TEntity>> filterBuilderConsumer) {
        if (supplier.getAsBoolean()) {
            filterBuilderConsumer.accept(this);
        }

        return this;

    }

    public Bson build() {
        return build(Operator.AND);
    }


    public Bson build(Operator operator) {
        if (bsons.size() == 1) {
            return bsons.get(0);
        } else {
            if (operator == Operator.OR) {
                return Filters.or(bsons);
            } else if (operator == Operator.NOR) {
                return Filters.nor(bsons);
            } else {
                return Filters.and(bsons);
            }
        }
    }
}
