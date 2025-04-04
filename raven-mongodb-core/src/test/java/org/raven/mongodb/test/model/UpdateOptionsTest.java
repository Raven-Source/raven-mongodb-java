package org.raven.mongodb.test.model;

import com.mongodb.WriteConcern;
import org.bson.BsonDocument;
import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.criteria.UpdateOptions;
import org.raven.mongodb.test.model.Orders.Fields;

public class UpdateOptionsTest {

    @Test
    public void builderTest() {

        UpdateOptions updateOptions = UpdateOptions.Builder.create(Orders.class)
                .filter(f -> f.eq(Fields.name, "raven").eq(Fields.isPay, true))
                .upsert(true)
                .update(u -> u.set(Fields.status, Status.Delete).set(Fields.isPay, false))
                .writeConcern(WriteConcern.W2)
                .build();


        System.out.println(updateOptions.toString());

        Assert.assertTrue(
                updateOptions.toBson().equals(
                        BsonDocument.parse("{\"filter\": {\"$and\": [{\"name\": \"raven\"}, {\"isPay\": true}]}, \"hint\": null, \"writeConcern\": {\"w\": 2}, \"update\": {\"$set\": {\"status\": -1, \"isPay\": false}}, \"upsert\": true}")
                )
        );
    }

}
