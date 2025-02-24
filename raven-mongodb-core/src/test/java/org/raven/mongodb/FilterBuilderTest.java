package org.raven.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.model.Mall;
import org.raven.mongodb.model.Status;
import org.raven.mongodb.model.User;
import org.raven.mongodb.criteria.FieldNest;
import org.raven.mongodb.criteria.FilterBuilder;
import org.raven.mongodb.criteria.Operator;

import java.util.Date;

/**
 * @author by yanfeng
 * date 2021/9/13 0:07
 */
@Slf4j
@SuppressWarnings("deprecation")
public class FilterBuilderTest {

    @Test
    public void filterTest() {

        FilterBuilder<User> filterBuilder;

        filterBuilder = FilterBuilder.create(User.class);
        filterBuilder.eq(User.Fields.id, "123");

        log.info(filterBuilder.build().toBsonDocument().toJson());
        Assert.assertEquals(BsonDocument.parse("{\"_id\": \"123\"}"), filterBuilder.build().toBsonDocument());

        filterBuilder = FilterBuilder.create(User.class);
        filterBuilder.eq(User.Fields.name, "c6e6a5391d30").eq(User.Fields.deleted, false);

        log.info(filterBuilder.build().toBsonDocument().toJson());

        Assert.assertEquals(BsonDocument.parse("{\"$and\": [{\"Name\": \"c6e6a5391d30\"}, {\"Deleted\": false}]}"), filterBuilder.build().toBsonDocument());


        filterBuilder = FilterBuilder.create(User.class);
        filterBuilder.eq(User.Fields.mall + "." + Mall.Fields.id, "m1");
        filterBuilder.eq(User.Fields.mall + "." + Mall.Fields.status, Status.Normal);

        log.info(filterBuilder.build().toBsonDocument().toJson());
        Assert.assertEquals(BsonDocument.parse("{\"$and\": [{\"Mall._id\": \"m1\"}, {\"Mall.Status\": 1}]}"), filterBuilder.build().toBsonDocument());


        filterBuilder = FilterBuilder.create(User.class);
        filterBuilder.eq(FieldNest.create().link(User.Fields.mall, Mall.Fields.name).build(), "大悦城");

        log.info(filterBuilder.build().toBsonDocument().toJson());
        Assert.assertEquals(BsonDocument.parse("{\"Mall.Name\": \"大悦城\"}"), filterBuilder.build().toBsonDocument());

        filterBuilder = FilterBuilder.create(User.class);
        filterBuilder = filterBuilder.eq("a", "1")
                .ne("b", 2)
                .gt("c", 3)
                .lt("d", 4)
                .gte("e", 5)
                .lte("f", 6)
                .in("g", 7, 8, 9)
                .nin("h", 10, 11, 12);

        Bson bson = filterBuilder.build();
        log.info(bson.toBsonDocument().toJson());
        Assert.assertEquals("{\"$and\": [{\"a\": \"1\"}, {\"b\": {\"$ne\": 2}}, {\"c\": {\"$gt\": 3}}, {\"d\": {\"$lt\": 4}}, {\"e\": {\"$gte\": 5}}, {\"f\": {\"$lte\": 6}}, {\"g\": {\"$in\": [7, 8, 9]}}, {\"h\": {\"$nin\": [10, 11, 12]}}]}",
                bson.toBsonDocument().toJson()
        );


        bson = filterBuilder.build(Operator.OR);
        log.info(bson.toBsonDocument().toJson());
        Assert.assertEquals("{\"$or\": [{\"a\": \"1\"}, {\"b\": {\"$ne\": 2}}, {\"c\": {\"$gt\": 3}}, {\"d\": {\"$lt\": 4}}, {\"e\": {\"$gte\": 5}}, {\"f\": {\"$lte\": 6}}, {\"g\": {\"$in\": [7, 8, 9]}}, {\"h\": {\"$nin\": [10, 11, 12]}}]}",
                bson.toBsonDocument().toJson()
        );


        FilterBuilder<User> filterBuilder2 = FilterBuilder.create(User.class)
                .ne("x", 1)
                .eq("y", 2);


        bson = filterBuilder.or(filterBuilder2).build();
        log.info(bson.toBsonDocument().toJson());
        Assert.assertEquals("{\"$or\": [{\"$and\": [{\"a\": \"1\"}, {\"b\": {\"$ne\": 2}}, {\"c\": {\"$gt\": 3}}, {\"d\": {\"$lt\": 4}}, {\"e\": {\"$gte\": 5}}, {\"f\": {\"$lte\": 6}}, {\"g\": {\"$in\": [7, 8, 9]}}, {\"h\": {\"$nin\": [10, 11, 12]}}]}, {\"$and\": [{\"x\": {\"$ne\": 1}}, {\"y\": 2}]}]}",
                bson.toBsonDocument().toJson()
        );


        filterBuilder = FilterBuilder.create(User.class);
        filterBuilder = filterBuilder.eq("a", "1")
                .ne("id", 2)
                .eq("name", "adc")
                .eq("status", Status.Normal)
                .gte("createDate", new Date(100, 9, 1, 10, 0, 0))
                .eq("mall.name", 6);

        bson = filterBuilder.or(filterBuilder2).build();
        log.info(bson.toBsonDocument().toJson());

        Assert.assertEquals("{\"$or\": [{\"$and\": [{\"a\": \"1\"}, {\"_id\": {\"$ne\": 2}}, {\"Name\": \"adc\"}, {\"Status\": 1}, {\"CreateDate\": {\"$gte\": {\"$date\": \"2000-10-01T02:00:00Z\"}}}, {\"Mall.Name\": 6}]}, {\"$and\": [{\"x\": {\"$ne\": 1}}, {\"y\": 2}]}]}",
                bson.toBsonDocument().toJson()
        );


        bson = FilterBuilder.create(User.class).build();
        log.info(bson.toBsonDocument().toJson());

        filterBuilder = FilterBuilder.create(User.class);
        filterBuilder.condition(true, f -> f.eq(User.Fields.id, 2));

    }

    @Test
    public void bsonDocTest() {

        FilterBuilder<BsonDocument> filterBuilder = FilterBuilder.create(BsonDocument.class);// FilterBuilder.empty(BsonDocument.class);

        filterBuilder
                .eq("a", "1")
                .ne("b", 2)
                .gt("c", 3)
                .lt("d", 4)
                .gte("e", 5)
                .lte("f", 6)
                .in("g", 7, 8, 9)
                .nin("h", 10, 11, 12)
                .andOperator();


        Bson bson = filterBuilder.build();
        log.info(bson.toBsonDocument().toJson());
        Assert.assertEquals("{\"$and\": [{\"a\": \"1\"}, {\"b\": {\"$ne\": 2}}, {\"c\": {\"$gt\": 3}}, {\"d\": {\"$lt\": 4}}, {\"e\": {\"$gte\": 5}}, {\"f\": {\"$lte\": 6}}, {\"g\": {\"$in\": [7, 8, 9]}}, {\"h\": {\"$nin\": [10, 11, 12]}}]}",
                bson.toBsonDocument().toJson()
        );

        filterBuilder.orOperator();

        bson = filterBuilder.build();
        log.info(bson.toBsonDocument().toJson());
        Assert.assertEquals("{\"$or\": [{\"a\": \"1\"}, {\"b\": {\"$ne\": 2}}, {\"c\": {\"$gt\": 3}}, {\"d\": {\"$lt\": 4}}, {\"e\": {\"$gte\": 5}}, {\"f\": {\"$lte\": 6}}, {\"g\": {\"$in\": [7, 8, 9]}}, {\"h\": {\"$nin\": [10, 11, 12]}}]}",
                bson.toBsonDocument().toJson()
        );

        filterBuilder.norOperator();

        bson = filterBuilder.build();
        log.info(bson.toBsonDocument().toJson());
        Assert.assertEquals("{\"$nor\": [{\"a\": \"1\"}, {\"b\": {\"$ne\": 2}}, {\"c\": {\"$gt\": 3}}, {\"d\": {\"$lt\": 4}}, {\"e\": {\"$gte\": 5}}, {\"f\": {\"$lte\": 6}}, {\"g\": {\"$in\": [7, 8, 9]}}, {\"h\": {\"$nin\": [10, 11, 12]}}]}",
                bson.toBsonDocument().toJson()
        );
    }


}

