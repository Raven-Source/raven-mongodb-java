package org.raven.mongodb;

import lombok.NonNull;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonValue;
import org.bson.codecs.Encoder;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.raven.mongodb.query.ProjectionBuilder;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author yi.liang
 */
public final class BsonUtils {

    private BsonUtils() {
    }

    public static <TEntity> BsonDocument convertToBsonDocument(@NonNull final TEntity entity, @NonNull final Encoder<TEntity> encoder) {

        return new BsonDocumentWrapper<>(entity, encoder);
    }

    public static @Nullable <TEntity> Bson projection(final Class<TEntity> entityClass,
                                                      @Nullable final List<String> includeFields,
                                                      @Nullable final List<String> excludeFields) {

        ProjectionBuilder<TEntity> projectionBuilder = ProjectionBuilder.create(entityClass);
        if (includeFields != null && !includeFields.isEmpty()) {

            projectionBuilder.include(includeFields);
        }

        if (excludeFields != null && !excludeFields.isEmpty()) {

            projectionBuilder.exclude(excludeFields);
        }

        return projectionBuilder.isEmpty() ? null : projectionBuilder.build();
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

    @SuppressWarnings({"unchecked"})
    public static <TKey extends Number> TKey convert(final Class<TKey> keyClazz, Number id) {

        if (id.getClass().equals(keyClazz)) {
            return (TKey) id;
        } else if (keyClazz.equals(Integer.class)) {
            return (TKey) Integer.valueOf(id.intValue());
        } else if (keyClazz.equals(Long.class)) {
            return (TKey) Long.valueOf(id.longValue());
        }
        return null;

    }

    @SuppressWarnings({"unchecked"})
    public static <TKey> TKey convert(final Class<TKey> keyClazz, BsonValue id) {

        if (keyClazz.equals(Integer.class)) {
            return (TKey) Integer.valueOf(id.asInt32().intValue());
        } else if (keyClazz.equals(Long.class)) {
            return (TKey) Long.valueOf(id.asInt64().longValue());
        } else if (keyClazz.equals(ObjectId.class)) {
            return (TKey) id.asObjectId().getValue();
        } else if (keyClazz.equals(String.class)) {
            if (id.isString()) {
                return (TKey) id.asString().getValue();
            } else if (id.isObjectId()) {
                return (TKey) id.asObjectId().getValue().toHexString();
            }
        }

        return null;

    }
}