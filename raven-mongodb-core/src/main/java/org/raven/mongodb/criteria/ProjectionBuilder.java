package org.raven.mongodb.criteria;

import com.mongodb.client.model.Projections;
import org.bson.conversions.Bson;
import org.raven.mongodb.util.BsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.bson.codecs.pojo.ClassModelUtils.getWriteName;

/**
 * date 2022/11/5 17:12
 */
public class ProjectionBuilder<TEntity> implements BsonBuilder {

    private final Class<TEntity> entityClass;
    private final List<Bson> bsons = new ArrayList<>();

    public ProjectionBuilder(final Class<TEntity> entityClass) {
        this.entityClass = entityClass;
    }

    public static <TEntity> ProjectionBuilder<TEntity> create(final Class<TEntity> entityClass) {
        return new ProjectionBuilder<>(entityClass);
    }

    public ProjectionBuilder<TEntity> newBuilder() {
        return create(this.entityClass);
    }

    public boolean isEmpty() {
        return bsons.isEmpty();
    }

    public ProjectionBuilder<TEntity> include(final String... fieldNames) {
        bsons.add(
                Projections.include(
                        Arrays.stream(fieldNames).map(x -> getWriteName(entityClass, x)).collect(Collectors.toList())

                )
        );
        return this;

    }

    public ProjectionBuilder<TEntity> include(final List<String> fieldNames) {
        bsons.add(
                Projections.include(
                        fieldNames.stream().map(x -> getWriteName(entityClass, x)).collect(Collectors.toList())

                )
        );
        return this;
    }

    public ProjectionBuilder<TEntity> exclude(final String... fieldNames) {
        bsons.add(
                Projections.exclude(
                        Arrays.stream(fieldNames).map(x -> getWriteName(entityClass, x)).collect(Collectors.toList())

                )
        );
        return this;

    }

    public ProjectionBuilder<TEntity> exclude(final List<String> fieldNames) {
        bsons.add(
                Projections.exclude(
                        fieldNames.stream().map(x -> getWriteName(entityClass, x)).collect(Collectors.toList())

                )
        );
        return this;
    }

    public ProjectionBuilder<TEntity> excludeId() {
        bsons.add(Projections.excludeId());
        return this;
    }


    public ProjectionBuilder<TEntity> elemMatch(final String fieldName) {
        bsons.add(Projections.elemMatch(getWriteName(entityClass, fieldName)));
        return this;
    }


    public ProjectionBuilder<TEntity> elemMatch(final String fieldName, final Bson filter) {
        bsons.add(Projections.elemMatch(getWriteName(entityClass, fieldName), filter));
        return this;
    }

    public ProjectionBuilder<TEntity> meta(final String fieldName, final String metaFieldName) {
        bsons.add(Projections.meta(getWriteName(entityClass, fieldName), metaFieldName));
        return this;
    }

    public ProjectionBuilder<TEntity> metaTextScore(final String fieldName, final String metaFieldName) {
        bsons.add(Projections.metaTextScore(getWriteName(entityClass, fieldName)));
        return this;
    }

    public ProjectionBuilder<TEntity> slice(final String fieldName, final int limit) {
        bsons.add(Projections.slice(getWriteName(entityClass, fieldName), limit));
        return this;
    }

    public ProjectionBuilder<TEntity> slice(final String fieldName, final int skip, final int limit) {
        bsons.add(Projections.slice(getWriteName(entityClass, fieldName), skip, limit));
        return this;
    }

    public ProjectionBuilder<TEntity> condition(boolean condition, Consumer<ProjectionBuilder<TEntity>> projectionBuilderConsumer) {

        if (condition) {
            projectionBuilderConsumer.accept(this);
        }
        return this;
    }

    public ProjectionBuilder<TEntity> condition(BooleanSupplier supplier, Consumer<ProjectionBuilder<TEntity>> projectionBuilderConsumer) {

        if (supplier.getAsBoolean()) {
            projectionBuilderConsumer.accept(this);
        }
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
