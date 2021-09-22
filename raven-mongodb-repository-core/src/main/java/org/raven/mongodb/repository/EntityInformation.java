package org.raven.mongodb.repository;

import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.raven.mongodb.repository.interceptors.EntityInterceptor;
import org.raven.mongodb.repository.spi.IdGenerationType;

import java.util.List;

/**
 * @author yi.liang
 * date 2021/9/20 17:30
 */
public interface EntityInformation<TEntity, TKey> extends EntityMetadata<TEntity> {

    Class<TKey> getIdType();

    IdGenerationType getIdGenerationType();

    BsonDocument toBsonDocument(TEntity entity);

    CodecRegistry getCodecRegistry();

    List<EntityInterceptor> getInterceptors();

    ClassModel<TEntity> getClassModel();
}
