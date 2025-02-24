package org.raven.mongodb.criteria;

import com.mongodb.client.model.Sorts;
import lombok.NonNull;
import org.bson.*;
import org.bson.conversions.Bson;
import org.raven.mongodb.util.BsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.bson.codecs.pojo.ClassModelUtils.getWriteName;

/**
 * @author yi.liang
 * date 2021/9/20 22:30
 */
public class SortBuilder<TEntity> implements CriteriaBuilder {

    private final Class<TEntity> entityClass;
    private final List<Bson> bsons = new ArrayList<>();

    public SortBuilder(final Class<TEntity> entityClass) {
        this.entityClass = entityClass;
    }

    public static <TEntity> SortBuilder<TEntity> create(final Class<TEntity> entityClass) {
        return new SortBuilder<>(entityClass);
    }

    public SortBuilder<TEntity> newBuilder() {
        return create(this.entityClass);
    }

    public boolean isEmpty() {
        return bsons.isEmpty();
    }

    public SortBuilder<TEntity> asc(final String... fieldNames) {
        return asc(asList(fieldNames));
    }

    public SortBuilder<TEntity> asc(@NonNull final List<String> fieldNames) {
        return orderBy(fieldNames, new BsonInt32(1));
    }

    public SortBuilder<TEntity> desc(final String... fieldNames) {
        return desc(asList(fieldNames));
    }

    public SortBuilder<TEntity> desc(@NonNull final List<String> fieldNames) {
        return orderBy(fieldNames, new BsonInt32(-1));
    }

    public SortBuilder<TEntity> metaTextScore(final String fieldName) {
        bsons.add(Sorts.metaTextScore(getWriteName(entityClass, fieldName)));
        return this;
    }

    private SortBuilder<TEntity> orderBy(final List<String> fieldNames, final BsonValue value) {
        BsonDocument document = new BsonDocument();
        for (String fieldName : fieldNames) {
            document.append(getWriteName(entityClass, fieldName), value);
        }
        bsons.add(document);
        return this;
    }

    public SortBuilder<TEntity> condition(boolean condition, Consumer<SortBuilder<TEntity>> sortBuilderConsumer) {
        if (condition) {
            sortBuilderConsumer.accept(this);
        }

        return this;

    }

    public SortBuilder<TEntity> condition(BooleanSupplier supplier, Consumer<SortBuilder<TEntity>> sortBuilderConsumer) {
        if (supplier.getAsBoolean()) {
            sortBuilderConsumer.accept(this);
        }

        return this;

    }

    public SortBuilder<TEntity> combine(SortBuilder<TEntity> sortBuilder) {
        bsons.addAll(sortBuilder.bsons);

        return this;
    }

    public SortBuilder<TEntity> combine(List<Bson> that) {
        bsons.addAll(that);

        return this;
    }

    public Bson build() {
        if (bsons.isEmpty()) {
            return null;
        }
        if (bsons.size() == 1) {
            return bsons.get(0);
        } else {
            return BsonUtils.combine(bsons);
        }
    }

    @Override
    public String toString() {
        return build().toString();
    }
}
