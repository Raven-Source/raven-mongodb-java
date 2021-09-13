package org.raven.mongodb.repository;

import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.repository.query.FieldNest;
import org.raven.mongodb.repository.query.FilterBuilder;
import org.raven.mongodb.repository.query.UpdateBuilder;

import java.util.List;

/**
 * @author by yanfeng
 * date 2021/9/13 0:07
 */
@Slf4j
public class UpdaterBuilderTest {

    @Test
    public void updaterTest() {


        UpdateBuilder<User> updateBuilder = new UpdateBuilder<>(User.class);
        updateBuilder
                .set("b", 2)
                .unset("c")
                .setOnInsert("d", 4)
                .rename("e", "ee")
                .inc("f", 6)
                .mul("g", 7)
                .min("h", 10)
                .max("h", 11)
                .currentDate("j")
                .currentTimestamp("k")
                .addToSet("l", 12)
                .addEachToSet("m", List.of(13, 14, 15))
                .push("n", 16)
                .pushEach("o", List.of(17, 18, 19))
                .pull("p", 20)
                .pullAll("q", List.of(21, 22))
                .popFirst("r")
                .popLast("s")
                .bitwiseAnd("t", 10);


        Bson bson = updateBuilder.build();
        log.info(bson.toBsonDocument().toJson());


        Assert.assertEquals("{\"$set\": {\"b\": 2}, \"$unset\": {\"c\": \"\"}, \"$setOnInsert\": {\"d\": 4}, \"$rename\": {\"e\": \"ee\"}, \"$inc\": {\"f\": 6}, \"$mul\": {\"g\": 7}, \"$min\": {\"h\": 10}, \"$max\": {\"h\": 11}, \"$currentDate\": {\"j\": true, \"k\": {\"$type\": \"timestamp\"}}, \"$addToSet\": {\"l\": 12, \"m\": {\"$each\": [13, 14, 15]}}, \"$push\": {\"n\": 16, \"o\": {\"$each\": [17, 18, 19]}}, \"$pull\": {\"p\": 20}, \"$pullAll\": {\"q\": [21, 22]}, \"$pop\": {\"r\": -1, \"s\": 1}, \"$bit\": {\"t\": {\"and\": 10}}}"
                , bson.toBsonDocument().toJson()
        );
    }


}
