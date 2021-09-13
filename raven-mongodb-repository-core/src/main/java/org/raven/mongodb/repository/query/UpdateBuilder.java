package org.raven.mongodb.repository.query;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.PushOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.lang.Nullable;
import org.bson.codecs.pojo.ClassModelUtils;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * @author by yanfeng
 * date 2021/9/13 20:55
 */
public class UpdateBuilder<TEntity> {

    private Class<TEntity> entityClass;
    private List<Bson> bsons = new ArrayList<>();

    public UpdateBuilder(final Class<TEntity> entityClass) {
        this.entityClass = entityClass;
    }

    public <TItem> UpdateBuilder<TEntity> set(final String fieldName, @Nullable final TItem value) {

        bsons.add(Updates.set(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> unset(final String fieldName) {

        bsons.add(Updates.unset(ClassModelUtils.getWriteName(entityClass, fieldName)));
        return this;

    }

    public UpdateBuilder<TEntity> setOnInsert(final UpdateBuilder<TEntity> updateBuilder) {

        bsons.add(Updates.setOnInsert(updateBuilder.build()));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> setOnInsert(final String fieldName, @Nullable final TItem value) {

        bsons.add(Updates.setOnInsert(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> rename(final String fieldName, @Nullable final String newFieldName) {

        bsons.add(Updates.rename(ClassModelUtils.getWriteName(entityClass, fieldName), ClassModelUtils.getWriteName(entityClass, newFieldName)));
        return this;

    }

    public UpdateBuilder<TEntity> inc(final String fieldName, @Nullable final Number number) {

        bsons.add(Updates.inc(ClassModelUtils.getWriteName(entityClass, fieldName), number));
        return this;

    }

    public UpdateBuilder<TEntity> mul(final String fieldName, @Nullable final Number number) {

        bsons.add(Updates.mul(ClassModelUtils.getWriteName(entityClass, fieldName), number));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> min(final String fieldName, final TItem value) {

        bsons.add(Updates.min(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> max(final String fieldName, final TItem value) {

        bsons.add(Updates.max(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> currentDate(final String fieldName) {

        bsons.add(Updates.currentDate(ClassModelUtils.getWriteName(entityClass, fieldName)));
        return this;

    }

    public UpdateBuilder<TEntity> currentTimestamp(final String fieldName) {

        bsons.add(Updates.currentTimestamp(ClassModelUtils.getWriteName(entityClass, fieldName)));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> addToSet(final String fieldName, @Nullable final TItem value) {

        bsons.add(Updates.addToSet(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> addEachToSet(final String fieldName, final List<TItem> values) {

        bsons.add(Updates.addEachToSet(ClassModelUtils.getWriteName(entityClass, fieldName), values));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> push(final String fieldName, @Nullable final TItem value) {

        bsons.add(Updates.push(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> pushEach(final String fieldName, final List<TItem> values) {

        bsons.add(Updates.pushEach(ClassModelUtils.getWriteName(entityClass, fieldName), values));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> pushEach(final String fieldName, final List<TItem> values, final PushOptions options) {

        bsons.add(Updates.pushEach(ClassModelUtils.getWriteName(entityClass, fieldName), values, options));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> pull(final String fieldName, @Nullable final TItem value) {

        bsons.add(Updates.pull(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> pullByFilter(final FilterBuilder<TEntity> filter) {

        bsons.add(Updates.pullByFilter(filter.build()));
        return this;

    }

    public <TItem> UpdateBuilder<TEntity> pullAll(final String fieldName, final List<TItem> values) {

        bsons.add(Updates.pullAll(ClassModelUtils.getWriteName(entityClass, fieldName), values));
        return this;

    }

    public UpdateBuilder<TEntity> popFirst(final String fieldName) {

        bsons.add(Updates.popFirst(ClassModelUtils.getWriteName(entityClass, fieldName)));
        return this;

    }

    public UpdateBuilder<TEntity> popLast(final String fieldName) {

        bsons.add(Updates.popLast(ClassModelUtils.getWriteName(entityClass, fieldName)));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseAnd(final String fieldName, final int value) {

        bsons.add(Updates.bitwiseAnd(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseAnd(final String fieldName, final long value) {

        bsons.add(Updates.bitwiseAnd(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseOr(final String fieldName, final int value) {

        bsons.add(Updates.bitwiseOr(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseOr(final String fieldName, final long value) {

        bsons.add(Updates.bitwiseOr(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseXor(final String fieldName, final int value) {

        bsons.add(Updates.bitwiseOr(ClassModelUtils.getWriteName(entityClass, fieldName), value));
        return this;

    }

    public UpdateBuilder<TEntity> bitwiseXor(final String fieldName, final long value) {

        bsons.add(Updates.bitwiseOr(ClassModelUtils.getWriteName(entityClass, fieldName), value));
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

    public Bson build() {
        if (bsons.size() == 1) {
            return bsons.get(0);
        } else {
            return Updates.combine(bsons);
        }
    }
}
