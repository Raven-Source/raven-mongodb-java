package org.raven.mongodb.reactive;

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
import org.raven.mongodb.util.BsonUtils;
import org.raven.mongodb.spi.ReactiveIdGenerator;
import org.raven.mongodb.spi.Sequence;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @param <TKey> Key
 * @author yi.liang
 * date 2021.07.26 18:31
 */
@SuppressWarnings({"unchecked"})
public class IncrementReactiveIdGeneration<TKey extends Number> implements ReactiveIdGenerator<TKey> {

    private final String collectionName;
    private final Sequence sequence;
    private final Class<TKey> keyClazz;

    private final Supplier<MongoDatabase> databaseSupplier;

    /**
     * @param collectionName   collectionName
     * @param sequence         Sequence
     * @param keyClazz         Key Class
     * @param databaseSupplier MongoDatabase
     */
    public IncrementReactiveIdGeneration(@NonNull String collectionName
            , @NonNull Sequence sequence
            , @NonNull Class<TKey> keyClazz
            , @NonNull Supplier<MongoDatabase> databaseSupplier) {

        if (!keyClazz.equals(Integer.class) && !keyClazz.equals(Long.class) && keyClazz.equals(Short.class)) {
            throw new MongoException(String.format("The TKey %s, is Unsupported type", keyClazz.getName()));
        }

        this.collectionName = collectionName;
        this.sequence = sequence;
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
    public String name() {
        return "INCREMENT";
    }

    /**
     * @param count     count
     * @param iteration iteration
     * @return inc id
     */
    public Mono<Long> createIncId(final long count, final int iteration) {

        MongoCollection<BsonDocument> collection = databaseSupplier.get().getCollection(sequence.getSequenceName(), BsonDocument.class);

        Bson filter = Filters.eq(sequence.getCollectionName(), collectionName);
        Bson updater = Updates.inc(sequence.getIncrementName(), count);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options = options.upsert(true).returnDocument(ReturnDocument.AFTER);

        Mono<BsonDocument> result = Mono.from(collection.findOneAndUpdate(filter, updater, options));

        return result.flatMap(x -> {

            if (x != null) {
                Long id = x.getInt64(sequence.getIncrementName()).longValue();
                return Mono.just(id);
            } else if (iteration <= 1) {
                return createIncId(count, (iteration + 1));
            } else {
                return Mono.error(new MongoException("Failed to get on the IncID"));
            }
        });


    }

    private TKey convert(Long id) {
        return BsonUtils.convert(keyClazz, id);
    }

}
