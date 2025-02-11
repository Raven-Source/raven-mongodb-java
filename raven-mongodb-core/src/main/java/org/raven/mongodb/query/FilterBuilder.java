package org.raven.mongodb.query;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.TextSearchOptions;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.Point;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.BsonType;
import org.bson.codecs.pojo.ClassModelUtils;
import org.bson.conversions.Bson;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author yi.liang
 * date 2021/9/12 23:01
 */
public class FilterBuilder<TEntity> implements BsonBuilder {

    private final Class<TEntity> entityClass;
    private final List<Bson> bsons = new ArrayList<>();

    @Getter
    @Setter
    private Operator operator;

    public FilterBuilder(final Class<TEntity> entityClass) {
        this(entityClass, Operator.AND);
    }

    public FilterBuilder(final Class<TEntity> entityClass, final Operator operator) {
        this.entityClass = entityClass;
        this.operator = operator;
    }

    public static <TEntity> FilterBuilder<TEntity> empty(final Class<TEntity> entityClass) {
        return new FilterBuilder<>(entityClass);
    }

    public static <TEntity> FilterBuilder<TEntity> empty(final Class<TEntity> entityClass, final Operator operator) {
        return new FilterBuilder<>(entityClass, operator);
    }

    public FilterBuilder<TEntity> newBuilder() {
        return new FilterBuilder<>(this.entityClass, this.operator);
    }

    public boolean isEmpty() {
        return bsons.isEmpty();
    }

    public <TItem> FilterBuilder<TEntity> eq(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.eq(getWriteName(fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> ne(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.ne(getWriteName(fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> gt(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.gt(getWriteName(fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> gte(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.gte(getWriteName(fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> lt(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.lt(getWriteName(fieldName), value));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> lte(final String fieldName, final TItem value) {
        assert fieldName != null;
        bsons.add(Filters.lte(getWriteName(fieldName), value));
        return this;
    }

    @SafeVarargs
    public final <TItem> FilterBuilder<TEntity> in(final String fieldName, final TItem... values) {
        assert fieldName != null;
        bsons.add(Filters.in(getWriteName(fieldName), values));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> in(final String fieldName, final Iterable<TItem> values) {
        assert fieldName != null;
        bsons.add(Filters.in(getWriteName(fieldName), values));
        return this;
    }

    @SafeVarargs
    public final <TItem> FilterBuilder<TEntity> nin(final String fieldName, final TItem... values) {
        assert fieldName != null;
        bsons.add(Filters.nin(getWriteName(fieldName), values));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> nin(final String fieldName, final Iterable<TItem> values) {
        assert fieldName != null;
        bsons.add(Filters.nin(getWriteName(fieldName), values));
        return this;
    }

    public FilterBuilder<TEntity> exists(final String fieldName) {
        return this.exists(fieldName, true);
    }

    public FilterBuilder<TEntity> exists(final String fieldName, final boolean exists) {
        assert fieldName != null;
        bsons.add(Filters.exists(getWriteName(fieldName), exists));
        return this;
    }

    public FilterBuilder<TEntity> type(final String fieldName, final BsonType type) {
        bsons.add(Filters.type(getWriteName(fieldName), type));
        return this;
    }

    public FilterBuilder<TEntity> type(final String fieldName, final String type) {
        bsons.add(Filters.type(getWriteName(fieldName), type));
        return this;
    }

    public FilterBuilder<TEntity> mod(final String fieldName, final long divisor, final long remainder) {
        bsons.add(Filters.mod(getWriteName(fieldName), divisor, remainder));
        return this;
    }

    public FilterBuilder<TEntity> regex(final String fieldName, final String pattern) {

        return this.regex(getWriteName(fieldName), pattern, null);
    }

    public FilterBuilder<TEntity> regex(final String fieldName, final String pattern, @Nullable final String options) {
        bsons.add(Filters.regex(getWriteName(fieldName), pattern, options));
        return this;
    }

    public FilterBuilder<TEntity> regex(final String fieldName, final Pattern pattern) {

        bsons.add(Filters.regex(getWriteName(fieldName), pattern));
        return this;
    }

    public FilterBuilder<TEntity> text(final String fieldName) {
        return this.text(fieldName, new TextSearchOptions());
    }

    public FilterBuilder<TEntity> text(final String fieldName, final TextSearchOptions textSearchOptions) {
        bsons.add(Filters.text(getWriteName(fieldName), textSearchOptions));
        return this;
    }

    public FilterBuilder<TEntity> where(final String javaScriptExpression) {
        bsons.add(Filters.where(javaScriptExpression));
        return this;
    }

    public <TExpression> FilterBuilder<TEntity> expr(final TExpression expression) {
        bsons.add(Filters.expr(expression));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> all(final String fieldName, final TItem... values) {
        bsons.add(Filters.all(getWriteName(fieldName), values));
        return this;
    }

    public <TItem> FilterBuilder<TEntity> all(final String fieldName, final Iterable<TItem> values) {
        bsons.add(Filters.all(getWriteName(fieldName), values));
        return this;
    }

    public FilterBuilder<TEntity> elemMatch(final String fieldName, final Bson filter) {
        bsons.add(Filters.elemMatch(getWriteName(fieldName), filter));
        return this;
    }

    public FilterBuilder<TEntity> size(final String fieldName, final int size) {
        bsons.add(Filters.size(getWriteName(fieldName), size));
        return this;
    }

    public FilterBuilder<TEntity> bitsAllClear(final String fieldName, final int bitmask) {
        bsons.add(Filters.bitsAllClear(getWriteName(fieldName), bitmask));
        return this;
    }

    public FilterBuilder<TEntity> bitsAllSet(final String fieldName, final int bitmask) {
        bsons.add(Filters.bitsAllSet(getWriteName(fieldName), bitmask));
        return this;
    }

    public FilterBuilder<TEntity> bitsAnyClear(final String fieldName, final int bitmask) {
        bsons.add(Filters.bitsAnyClear(getWriteName(fieldName), bitmask));
        return this;
    }

    public FilterBuilder<TEntity> bitsAnySet(final String fieldName, final int bitmask) {
        bsons.add(Filters.bitsAnySet(getWriteName(fieldName), bitmask));
        return this;
    }

    public FilterBuilder<TEntity> geoWithin(final String fieldName, final Geometry geometry) {
        bsons.add(Filters.geoWithin(getWriteName(fieldName), geometry));
        return this;
    }

    public FilterBuilder<TEntity> geoWithin(final String fieldName, final Bson geometry) {
        bsons.add(Filters.geoWithin(getWriteName(fieldName), geometry));
        return this;
    }

    public FilterBuilder<TEntity> geoWithinBox(final String fieldName, final double lowerLeftX, final double lowerLeftY,
                                               final double upperRightX, final double upperRightY) {
        bsons.add(Filters.geoWithinBox(getWriteName(fieldName),
                lowerLeftX, lowerLeftY, upperRightX, upperRightY));
        return this;
    }

    public FilterBuilder<TEntity> geoWithinPolygon(final String fieldName, final List<List<Double>> points) {

        bsons.add(Filters.geoWithinPolygon(getWriteName(fieldName), points));
        return this;
    }

    public FilterBuilder<TEntity> geoWithinCenter(final String fieldName, final double x, final double y, final double radius) {

        bsons.add(Filters.geoWithinCenter(getWriteName(fieldName), x, y, radius));
        return this;
    }

    public FilterBuilder<TEntity> geoWithinCenterSphere(final String fieldName, final double x, final double y, final double radius) {

        bsons.add(Filters.geoWithinCenterSphere(getWriteName(fieldName), x, y, radius));
        return this;
    }

    public FilterBuilder<TEntity> geoIntersects(final String fieldName, final Bson geometry) {
        bsons.add(Filters.geoIntersects(getWriteName(fieldName), geometry));
        return this;
    }

    public FilterBuilder<TEntity> geoIntersects(final String fieldName, final Geometry geometry) {
        bsons.add(Filters.geoIntersects(getWriteName(fieldName), geometry));
        return this;
    }

    public FilterBuilder<TEntity> near(final String fieldName, final Point geometry, @Nullable final Double maxDistance,
                                       @Nullable final Double minDistance) {

        bsons.add(Filters.near(getWriteName(fieldName), geometry, maxDistance, minDistance));
        return this;
    }

    public FilterBuilder<TEntity> near(final String fieldName, final Bson geometry, @Nullable final Double maxDistance,
                                       @Nullable final Double minDistance) {

        bsons.add(Filters.near(getWriteName(fieldName), geometry, maxDistance, minDistance));
        return this;
    }

    public FilterBuilder<TEntity> near(final String fieldName, final double x, final double y, @Nullable final Double maxDistance,
                                       @Nullable final Double minDistance) {

        bsons.add(Filters.near(getWriteName(fieldName), x, y, maxDistance, minDistance));
        return this;
    }

    public FilterBuilder<TEntity> nearSphere(final String fieldName, final Point geometry, @Nullable final Double maxDistance,
                                             @Nullable final Double minDistance) {

        bsons.add(Filters.nearSphere(getWriteName(fieldName), geometry, maxDistance, minDistance));
        return this;
    }

    public FilterBuilder<TEntity> nearSphere(final String fieldName, final Bson geometry, @Nullable final Double maxDistance,
                                             @Nullable final Double minDistance) {

        bsons.add(Filters.nearSphere(getWriteName(fieldName), geometry, maxDistance, minDistance));
        return this;
    }

    public FilterBuilder<TEntity> nearSphere(final String fieldName, final double x, final double y, @Nullable final Double maxDistance,
                                             @Nullable final Double minDistance) {

        bsons.add(Filters.nearSphere(getWriteName(fieldName), x, y, maxDistance, minDistance));
        return this;
    }

    public FilterBuilder<TEntity> jsonSchema(final Bson schema) {
        bsons.add(Filters.jsonSchema(schema));
        return this;
    }

    public FilterBuilder<TEntity> and(@NonNull final FilterBuilder<TEntity> that) {
        return and(that.build());
    }

    public FilterBuilder<TEntity> and(@NonNull final Bson that) {

        if (isEmpty()) {
            bsons.add(that);
            return this;
        }

        Bson bson = Filters.and(this.build(), that);
        bsons.clear();
        bsons.add(bson);
        return this;
    }

    public FilterBuilder<TEntity> or(@NonNull final FilterBuilder<TEntity> that) {
        return or(that.build());
    }

    public FilterBuilder<TEntity> or(@NonNull final Bson that) {

        if (isEmpty()) {
            bsons.add(that);
            return this;
        }

        Bson bson = Filters.or(this.build(), that);
        bsons.clear();
        bsons.add(bson);
        return this;
    }

    public FilterBuilder<TEntity> not() {

        Bson bson = Filters.not(this.build());
        bsons.clear();
        bsons.add(bson);
        return this;
    }

    public FilterBuilder<TEntity> nor() {

        Bson bson = Filters.nor(this.build());
        bsons.clear();
        bsons.add(bson);
        return this;
    }

    public FilterBuilder<TEntity> condition(boolean condition
            , @NonNull Consumer<FilterBuilder<TEntity>> filterBuilderConsumer) {

        if (condition) {
            filterBuilderConsumer.accept(this);
        }

        return this;

    }

    public FilterBuilder<TEntity> condition(BooleanSupplier supplier
            , @NonNull Consumer<FilterBuilder<TEntity>> filterBuilderConsumer) {

        if (supplier.getAsBoolean()) {
            filterBuilderConsumer.accept(this);
        }

        return this;

    }

    public FilterBuilder<TEntity> combine(FilterBuilder<TEntity> filterBuilder) {
        bsons.addAll(filterBuilder.bsons);

        return this;
    }

    public FilterBuilder<TEntity> combine(List<Bson> that) {
        bsons.addAll(that);

        return this;
    }

    public String getWriteName(final String fieldName) {
        return ClassModelUtils.getWriteName(entityClass, fieldName);
    }

    public Bson build() {
        return build(operator);
    }


    public Bson build(Operator operator) {
        if (bsons.isEmpty()) {
            return Filters.empty();
        }
        if (bsons.size() == 1) {
            return bsons.get(0);
        } else {
            if (operator == Operator.OR) {
                return Filters.or(new ArrayList<>(bsons));
            } else if (operator == Operator.NOR) {
                return Filters.nor(new ArrayList<>(bsons));
            } else {
                return Filters.and(new ArrayList<>(bsons));
            }
        }
    }
}
