package org.raven.mongodb;

import com.mongodb.ReadPreference;
import org.bson.BsonDocument;
import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.criteria.CountOptions;
import org.raven.mongodb.model.Orders;

public class CountOptionsTest {

    @Test
    public void builderTest() {

        CountOptions countOptions = CountOptions.Builder.create(Orders.class)
                .filter(f -> f.eq(Orders.Fields.name, "raven"))
                .limit(1)
                .skip(2)
                .readPreference(ReadPreference.primary())
                .build();


        System.out.println(countOptions.toString());

        Assert.assertTrue(
                countOptions.toBson().equals(
                        BsonDocument.parse("{\"filter\": {\"name\": \"raven\"}, \"hint\": null, \"readPreference\": {\"mode\": \"primary\"}, \"limit\": 1, \"skip\": 2}")
                )
        );

    }

}
