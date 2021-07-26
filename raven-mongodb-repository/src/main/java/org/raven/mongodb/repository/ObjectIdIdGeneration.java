package org.raven.mongodb.repository;

import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.raven.mongodb.repository.spi.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yi.liang
 * @since JDK11
 * date 2021.07.26 18:31
 */
@SuppressWarnings({"unchecked"})
public class ObjectIdIdGeneration<TKey> implements IdGenerator<TKey> {

    private final Class<TKey> keyClazz;

    public ObjectIdIdGeneration(Class<TKey> keyClazz) {

        if (!String.class.equals(keyClazz) && !ObjectId.class.equals(keyClazz)) {
            throw new MongoException(String.format("The TKey %s, is Unsupported type", keyClazz.getName()));
        }

        this.keyClazz = keyClazz;
    }

    @Override
    public TKey generateId() {
        return (TKey) Objects.requireNonNull(createId());
    }

    @Override
    public List<TKey> generateIdBatch(long count) {

        List<TKey> list = new ArrayList<>((int) count);
        for (long i = 0; i < count; i++) {
            list.add(createId());
        }
        return list;
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
