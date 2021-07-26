package org.raven.mongodb.repository;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.mongodb.repository.spi.IdGenerator;

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
    private final MongoSequence mongoSequence;
    private final Class<TKey> keyClazz;

    private Supplier<MongoDatabase> databaseSupplier;

    public IncrementIdGeneration(String collectionName
        , MongoSequence mongoSequence
        , Class<TKey> keyClazz
        , Supplier<MongoDatabase> databaseSupplier) {

        if (!keyClazz.equals(Integer.class) && !keyClazz.equals(Long.class) && keyClazz.equals(Short.class)) {
            throw new MongoException(String.format("The TKey %s, is Unsupported type", keyClazz.getName()));
        }

        this.collectionName = collectionName;
        this.mongoSequence = mongoSequence;
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
    public String getType() {
        return "INCREMENT";
    }

    public Long createIncId(final long count, final int iteration) {

        MongoCollection<BsonDocument> collection = databaseSupplier.get().getCollection(mongoSequence.getSequenceName(), BsonDocument.class);

        Bson filter = Filters.eq(mongoSequence.getCollectionName(), collectionName);
        Bson updater = Updates.inc(mongoSequence.getIncrementID(), count);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options = options.upsert(true).returnDocument(ReturnDocument.AFTER);

        BsonDocument result = collection.findOneAndUpdate(filter, updater, options);

        if (result != null) {
            return result.getInt64(mongoSequence.getIncrementID()).longValue();
        } else if (iteration <= 1) {
            return createIncId(count, (iteration + 1));
        } else {
            throw new MongoException("Failed to get on the IncID");
        }

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
