package org.raven.mongodb.repository.interceptors;

import com.mongodb.client.model.Updates;
import org.bson.BsonDocument;
import org.bson.codecs.pojo.PropertyModel;
import org.bson.conversions.Bson;
import org.raven.commons.data.Versioned;
import org.raven.mongodb.repository.EntityInformation;
import org.raven.mongodb.repository.UpdateOptions;

/**
 * @author by yanfeng
 * date 2021/9/22 20:00
 */
public class VersionedEntityInterceptor implements EntityInterceptor {

    @Override
    public void preUpdate(final UpdateOptions options,
                           final EntityInformation<?, ?> entityInformation) {

        if (Versioned.class.isAssignableFrom(entityInformation.getEntityType())) {

            PropertyModel<?> propertyModel = entityInformation.getClassModel().getPropertyModel(Versioned.VERSION);
            Bson update = options.update();
            BsonDocument bsonDocument;
            Bson incVersion = Updates.inc(propertyModel.getWriteName(), 1);

            if (update == null) {
                update = incVersion;
            } else {
                bsonDocument = update.toBsonDocument();
                if (!bsonDocument.containsKey(propertyModel.getName())) {
                    update = Updates.combine(update, incVersion);
                }
            }
            options.update(update);

        }
    }
}
