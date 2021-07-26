package org.raven.mongodb.repository.reactive;

import com.mongodb.MongoException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.raven.mongodb.repository.MongoSequence;
import org.raven.mongodb.repository.spi.ReactiveIdGenerator;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author yi.liang
 * @since JDK11
 * date 2021.07.26 18:31
 */
@SuppressWarnings({"unchecked"})
public class ObjectIdReactiveIdGeneration<TKey> implements ReactiveIdGenerator<TKey> {

    private final Class<TKey> keyClazz;

    public ObjectIdReactiveIdGeneration(Class<TKey> keyClazz) {

        if (!String.class.equals(keyClazz) && !ObjectId.class.equals(keyClazz)) {
            throw new MongoException(String.format("The TKey %s, is Unsupported type", keyClazz.getName()));
        }

        this.keyClazz = keyClazz;
    }

    @Override
    public Mono<TKey> generateId() {
        return Mono.just(Objects.requireNonNull(createId()));
    }

    @Override
    public Mono<List<TKey>> generateIdBatch(long count) {

        List<TKey> list = new ArrayList<>((int) count);
        for (long i = 0; i < count; i++) {
            list.add(createId());
        }
        return Mono.just(list);
    }

    @Override
    public String getType() {
        return "OBJECTID";
    }

    private TKey createId() {

        if (String.class.equals(keyClazz)) {
            return (TKey) new ObjectId().toString();
        } else if (ObjectId.class.equals(keyClazz)) {
            return (TKey) new ObjectId();
        }
        return null;
    }

}