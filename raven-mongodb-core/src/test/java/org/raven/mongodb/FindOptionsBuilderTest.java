package org.raven.mongodb;

import com.mongodb.ReadPreference;
import org.bson.BsonDocument;
import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.criteria.FindOptions;
import org.raven.mongodb.model.Orders;

public class FindOptionsBuilderTest {

    @Test
    public void builderTest() {

        FindOptions findOptions = FindOptions.Builder.create(Orders.class)
                .filter(f -> f.eq(Orders.Fields.name, "raven"))
                .sort(s -> s.asc(Orders.Fields.price))
                .projection(p -> p.include(Orders.Fields.name, Orders.Fields.status).excludeId())
                .limit(2)
                .skip(5)
                .readPreference(ReadPreference.primary())
                .build();


        System.out.println(findOptions.toString());

        Assert.assertTrue(
                findOptions.toBson().equals(
                        BsonDocument.parse("{\"filter\": {\"name\": \"raven\"}, \"hint\": null, \"readPreference\": {\"mode\": \"primary\"}, \"projection\": {\"name\": 1, \"status\": 1, \"_id\": 0}, \"sort\": {\"price\": 1}, \"limit\": 2, \"skip\": 5}")
                )
        );


    }

}
