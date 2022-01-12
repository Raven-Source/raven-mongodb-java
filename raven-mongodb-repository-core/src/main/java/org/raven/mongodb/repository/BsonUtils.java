package org.raven.mongodb.repository;

import com.mongodb.client.model.Projections;
import lombok.NonNull;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonValue;
import org.bson.codecs.Encoder;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author yi.liang
 */
@SuppressWarnings("unchecked")
public final class BsonUtils {

    private BsonUtils() {
    }

    public static <TEntity> BsonDocument convertToBsonDocument(@NonNull final TEntity entity, @NonNull final Encoder<TEntity> encoder) {

        return new BsonDocumentWrapper<TEntity>(entity, encoder);
    }

    public static Bson includeFields(@NonNull final List<String> includeFields) {

        Bson projection = null;
        if (includeFields != null && includeFields.size() > 0) {
            projection = Projections.include(includeFields);
        }

        return projection;
    }

    public static <T extends Bson> Bson combine(@NonNull final List<T> bsons) {

        BsonDocument document = new BsonDocument();

        for (Bson bson : bsons) {

            if (bson == null)
                continue;

            BsonDocument bsonDocument = bson.toBsonDocument();

            for (Map.Entry<String, BsonValue> stringBsonValueEntry : bsonDocument.entrySet()) {
                document.remove(stringBsonValueEntry.getKey());
                document.append(stringBsonValueEntry.getKey(), stringBsonValueEntry.getValue());
            }
        }

        return document;
    }

//    /**
//     * ID assignment
//     *
//     * @param keyClazz
//     * @param entity
//     * @param id
//     * @param <TEntity>
//     * @param <TKey>
//     */
//    public static <TEntity extends Entity<TKey>, TKey> void assignmentEntityID(final Class<TKey> keyClazz, final TEntity entity, final long id) {
//        Entity<TKey> tEntity = entity;
//
//        if (keyClazz.equals(Integer.class)) {
//            ((Entity<Integer>) tEntity).setId((int) id);
//        } else if (keyClazz.equals(Long.class)) {
//            ((Entity<Long>) tEntity).setId(id);
//        } else if (keyClazz.equals(Short.class)) {
//            ((Entity<Short>) tEntity).setId((short) id);
//        }
//
//    }
//
//    /**
//     * ID assignment
//     *
//     * @param entity
//     * @param id
//     * @param <TEntity>
//     * @param <TKey>
//     */
//    public static <TEntity extends Entity<TKey>, TKey> void assignmentEntityID(final TEntity entity, final ObjectId id) {
//        Entity<ObjectId> tEntity = (Entity<ObjectId>) entity;
//        tEntity.setId(id);
//
//    }

}
