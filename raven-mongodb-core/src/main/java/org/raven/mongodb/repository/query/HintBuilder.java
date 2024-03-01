package org.raven.mongodb.repository.query;

import lombok.NonNull;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.raven.mongodb.repository.BsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.bson.codecs.pojo.ClassModelUtils.getWriteName;

/**
 * @author yi.liang
 * date 2021/9/20 23:25
 */
public class HintBuilder<TEntity> {

    private final Class<TEntity> entityClass;
    private final List<Bson> bsons = new ArrayList<>();

    public HintBuilder(final Class<TEntity> entityClass) {
        this.entityClass = entityClass;
    }

    public static <TEntity> HintBuilder<TEntity> empty(final Class<TEntity> entityClass) {
        return new HintBuilder<>(entityClass);
    }

    public boolean isEmpty() {
        return bsons.isEmpty();
    }

    public HintBuilder<TEntity> ascending(final String... fieldNames) {
        return ascending(asList(fieldNames));
    }

    public HintBuilder<TEntity> ascending(@NonNull final List<String> fieldNames) {
        return compoundIndex(fieldNames, new BsonInt32(1));
    }

    public HintBuilder<TEntity> descending(final String... fieldNames) {
        return descending(asList(fieldNames));
    }

    public HintBuilder<TEntity> descending(@NonNull final List<String> fieldNames) {
        return compoundIndex(fieldNames, new BsonInt32(-1));
    }

    private HintBuilder<TEntity> compoundIndex(final List<String> fieldNames, final BsonValue value) {
        BsonDocument document = new BsonDocument();
        for (String fieldName : fieldNames) {
            document.append(getWriteName(entityClass, fieldName), value);
        }
        bsons.add(document);
        return this;
    }

    public HintBuilder<TEntity> condition(boolean condition, Consumer<HintBuilder<TEntity>> hintBuilderConsumer) {

        if (condition) {
            hintBuilderConsumer.accept(this);
        }
        return this;
    }

    public HintBuilder<TEntity> condition(BooleanSupplier supplier, Consumer<HintBuilder<TEntity>> hintBuilderConsumer) {

        if (supplier.getAsBoolean()) {
            hintBuilderConsumer.accept(this);
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
}
