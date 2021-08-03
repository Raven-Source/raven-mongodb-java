package org.raven.mongodb.repository.reactive;

import com.mongodb.MongoException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.NonNull;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.mongodb.repository.MongoSequence;
import org.raven.mongodb.repository.spi.ReactiveIdGenerator;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author yi.liang
 * @since JDK11
 * date 2021.07.26 18:31
 */
@SuppressWarnings({"unchecked"})
public class IncrementReactiveIdGeneration<TKey extends Number> implements ReactiveIdGenerator<TKey> {

    private final String collectionName;
    private final MongoSequence mongoSequence;
    private final Class<TKey> keyClazz;

    private Supplier<MongoDatabase> databaseSupplier;

    /**
     * @param mongoSequence
     * @param keyClazz
     */
    public IncrementReactiveIdGeneration(@NonNull String collectionName
        , @NonNull MongoSequence mongoSequence
        , @NonNull Class<TKey> keyClazz
        , @NonNull Supplier<MongoDatabase> databaseSupplier) {

        if (!keyClazz.equals(Integer.class) && !keyClazz.equals(Long.class) && keyClazz.equals(Short.class)) {
            throw new MongoException(String.format("The TKey %s, is Unsupported type", keyClazz.getName()));
        }

        this.collectionName = collectionName;
        this.mongoSequence = mongoSequence;
        this.keyClazz = keyClazz;

        this.databaseSupplier = databaseSupplier;
    }

    @Override
    public Mono<TKey> generateId() {
        return createIncId(1, 0).map(this::convert);
    }

    @Override
    public Mono<List<TKey>> generateIdBatch(long count) {

        return createIncId(count, 0).map(id -> {

            id = id - count;
            List<TKey> list = new ArrayList<>((int) count);
            for (int i = 0; i < count; i++) {
                list.add(convert(++id));
            }
            return list;
        });

    }

    @Override
    public String getType() {
        return "INCREMENT";
    }

    /**
     * @param count
     * @param iteration
     * @return
     */
    public Mono<Long> createIncId(final long count, final int iteration) {

        MongoCollection<BsonDocument> collection = databaseSupplier.get().getCollection(mongoSequence.getSequenceName(), BsonDocument.class);

        Bson filter = Filters.eq(mongoSequence.getCollectionName(), collectionName);
        Bson updater = Updates.inc(mongoSequence.getIncrement(), count);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options = options.upsert(true).returnDocument(ReturnDocument.AFTER);

        Mono<BsonDocument> result = Mono.from(collection.findOneAndUpdate(filter, updater, options));

        return result.flatMap(x -> {

            if (x != null) {
                Long id = x.getInt64(mongoSequence.getIncrement()).longValue();
                return Mono.just(id);
            } else if (iteration <= 1) {
                return createIncId(count, (iteration + 1));
            } else {
                return Mono.error(new MongoException("Failed to get on the IncID"));
            }
        });


    }

    private TKey convert(Long id) {

        if (keyClazz.equals(Integer.class)) {
            return (TKey) Integer.valueOf(id.intValue());
        } else if (keyClazz.equals(Long.class)) {
            return (TKey) id;
        } else if (keyClazz.equals(Short.class)) {
            return (TKey) Short.valueOf(id.shortValue());
        }
        return null;
    }

}
