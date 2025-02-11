package org.raven.mongodb.query;

import com.mongodb.client.model.PushOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.lang.Nullable;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static org.bson.codecs.pojo.ClassModelUtils.getWriteName;

/**
 * @author yi.liang
 * date 2021/9/13 20:55
 */
public class UpdateBuilder<TEntity> implements BsonBuilder {

    private final Class<TEntity> entityClass;
    private final List<Bson> bsons = new ArrayList<>();

    public UpdateBuilder(final Class<TEntity> entityClass) {
        this.entityClass = entityClass;
    }

    public static <TEntity> UpdateBuilder<TEntity> create(final Class<TEntity> entityClass) {
        return new UpdateBuilder<>(entityClass);
    }

    public UpdateBuilder<TEntity> newBuilder() {
        return create(this.entityClass);
    }

    public boolean isEmpty() {
        return bsons.isEmpty();
    }

    public <TItem> UpdateBuilder<TEntity> set(final String fieldName, @Nullable final TItem value) {

        bsons.add(Updates.set(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> unset(final String fieldName) {

        bsons.add(Updates.unset(getWriteName(entityClass, fieldName)));
        return this;

    }

    public UpdateBuilder<TEntity> setOnInsert(final UpdateBuilder<TEntity> updateBuilder) {

        bsons.add(Updates.setOnInsert(updateBuilder.build()));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> setOnInsert(final String fieldName, @Nullable final TItem value) {

        bsons.add(Updates.setOnInsert(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> rename(final String fieldName, @Nullable final String newFieldName) {

        assert newFieldName != null;
        bsons.add(Updates.rename(getWriteName(entityClass, fieldName), getWriteName(entityClass, newFieldName)));
        return this;

    }

    public UpdateBuilder<TEntity> inc(final String fieldName, @Nullable final Number number) {

        assert number != null;
        bsons.add(Updates.inc(getWriteName(entityClass, fieldName), number));
        return this;

    }

    public UpdateBuilder<TEntity> mul(final String fieldName, @Nullable final Number number) {

        assert number != null;
        bsons.add(Updates.mul(getWriteName(entityClass, fieldName), number));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> min(final String fieldName, final TItem value) {

        bsons.add(Updates.min(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> max(final String fieldName, final TItem value) {

        bsons.add(Updates.max(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> currentDate(final String fieldName) {

        bsons.add(Updates.currentDate(getWriteName(entityClass, fieldName)));
        return this;

    }

    public UpdateBuilder<TEntity> currentTimestamp(final String fieldName) {

        bsons.add(Updates.currentTimestamp(getWriteName(entityClass, fieldName)));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> addToSet(final String fieldName, @Nullable final TItem value) {

        bsons.add(Updates.addToSet(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> addEachToSet(final String fieldName, final List<TItem> values) {

        bsons.add(Updates.addEachToSet(getWriteName(entityClass, fieldName), values));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> push(final String fieldName, @Nullable final TItem value) {

        bsons.add(Updates.push(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> pushEach(final String fieldName, final List<TItem> values) {

        bsons.add(Updates.pushEach(getWriteName(entityClass, fieldName), values));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> pushEach(final String fieldName, final List<TItem> values, final PushOptions options) {

        bsons.add(Updates.pushEach(getWriteName(entityClass, fieldName), values, options));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> pull(final String fieldName, @Nullable final TItem value) {

        bsons.add(Updates.pull(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> pullByFilter(final FilterBuilder<TEntity> filter) {

        bsons.add(Updates.pullByFilter(filter.build()));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> pullAll(final String fieldName, final List<TItem> values) {

        bsons.add(Updates.pullAll(getWriteName(entityClass, fieldName), values));
        return this;

    }

    public UpdateBuilder<TEntity> popFirst(final String fieldName) {

        bsons.add(Updates.popFirst(getWriteName(entityClass, fieldName)));
        return this;

    }

    public UpdateBuilder<TEntity> popLast(final String fieldName) {

        bsons.add(Updates.popLast(getWriteName(entityClass, fieldName)));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseAnd(final String fieldName, final int value) {

        bsons.add(Updates.bitwiseAnd(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseAnd(final String fieldName, final long value) {

        bsons.add(Updates.bitwiseAnd(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseOr(final String fieldName, final int value) {

        bsons.add(Updates.bitwiseOr(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseOr(final String fieldName, final long value) {

        bsons.add(Updates.bitwiseOr(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseXor(final String fieldName, final int value) {

        bsons.add(Updates.bitwiseOr(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseXor(final String fieldName, final long value) {

        bsons.add(Updates.bitwiseOr(getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> condition(boolean condition, Consumer<UpdateBuilder<TEntity>> updateBuilderConsumer) {
        if (condition) {
            updateBuilderConsumer.accept(this);
        }

        return this;

    }

    public UpdateBuilder<TEntity> condition(BooleanSupplier supplier, Consumer<UpdateBuilder<TEntity>> updateBuilderConsumer) {
        if (supplier.getAsBoolean()) {
            updateBuilderConsumer.accept(this);
        }

        return this;

    }

    public UpdateBuilder<TEntity> combine(UpdateBuilder<TEntity> updateBuilder) {
        bsons.addAll(updateBuilder.bsons);

        return this;
    }

    public UpdateBuilder<TEntity> combine(List<Bson> that) {
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
            return Updates.combine(new ArrayList<>(bsons));
        }
    }
}
