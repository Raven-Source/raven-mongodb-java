package org.raven.mongodb.repository;

import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.repository.query.FieldNest;
import org.raven.mongodb.repository.query.FilterBuilder;

/**
 * @author by yanfeng
 * date 2021/9/13 0:07
 */
@Slf4j
public class FilterBuilderTest {

    @Test
    public void filterTest() {

        FilterBuilder<User> filterBuilder = new FilterBuilder<>(User.class);
        filterBuilder.eq(User.Fields.name, "c6e6a5391d30").eq(User.Fields.del, false);

        log.info(filterBuilder.build().toBsonDocument().toJson());

        Assert.assertEquals(BsonDocument.parse("{\"$and\": [{\"Name\": \"c6e6a5391d30\"}, {\"Del\": false}]}"), filterBuilder.build().toBsonDocument());


        filterBuilder = new FilterBuilder<>(User.class);
        filterBuilder.eq(User.Fields.mall + "." + Mall.Fields.id, "m1");

        log.info(filterBuilder.build().toBsonDocument().toJson());
        Assert.assertEquals(BsonDocument.parse("{\"Mall._id\": \"m1\"}"), filterBuilder.build().toBsonDocument());


        filterBuilder = new FilterBuilder<>(User.class);
        filterBuilder.eq(FieldNest.empty().link(User.Fields.mall, Mall.Fields.name).build(), "大悦城");

        log.info(filterBuilder.build().toBsonDocument().toJson());
        Assert.assertEquals(BsonDocument.parse("{\"Mall.Name\": \"大悦城\"}"), filterBuilder.build().toBsonDocument());

    }


}
