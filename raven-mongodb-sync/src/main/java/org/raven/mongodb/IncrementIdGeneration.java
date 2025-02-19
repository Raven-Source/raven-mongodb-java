package org.raven.mongodb;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import lombok.NonNull;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.raven.mongodb.spi.IdGenerator;
import org.raven.mongodb.spi.Sequence;
import org.raven.mongodb.util.BsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author yi.liang
 * date 2021.07.26 18:31
 */
@SuppressWarnings({"unchecked"})
public class IncrementIdGeneration<TKey extends Number> implements IdGenerator<TKey> {

    private final String collectionName;
    private final Sequence sequence;
    private final Class<TKey> keyClazz;

    private final Supplier<MongoDatabase> databaseSupplier;

    public IncrementIdGeneration(@NonNull String collectionName
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
    public TKey generateId() {
        return convert(createIncId(1, 0));
    }

    @Override
    public List<TKey> generateIdBatch(long count) {

        Long id = createIncId(count, 0);

        id = id - count;
        List<TKey> list = new ArrayList<>((int) count);
        for (int i = 0; i < count; i++) {
            list.add(convert(++id));
        }
        return list;
    }

    @Override
    public String name() {
        return "INCREMENT";
    }

    public Long createIncId(final long count, final int iteration) {

        MongoCollection<Document> collection = databaseSupplier.get().getCollection(sequence.getSequenceName());

        Bson filter = Filters.eq(sequence.getCollectionName(), collectionName);
        Bson updater = Updates.inc(sequence.getIncrementName(), count);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options = options.upsert(true).returnDocument(ReturnDocument.AFTER);

        Document result = collection.findOneAndUpdate(filter, updater, options);

        if (result != null) {
            return result.get(sequence.getIncrementName(), Long.class);
        } else if (iteration <= 1) {
            return createIncId(count, (iteration + 1));
        } else {
            throw new MongoException("Failed to get on the incr");
        }

    }

    private TKey convert(Long id) {
        return BsonUtils.convert(keyClazz, id);
    }

}
