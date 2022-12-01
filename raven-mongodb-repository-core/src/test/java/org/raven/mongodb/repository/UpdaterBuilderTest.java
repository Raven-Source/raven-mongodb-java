package org.raven.mongodb.repository;

import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.repository.model.Mall;
import org.raven.mongodb.repository.model.User;
import org.raven.mongodb.repository.query.UpdateBuilder;

import java.util.Arrays;

/**
 * @author by yanfeng
 * date 2021/9/13 0:07
 */
@Slf4j
public class UpdaterBuilderTest {

    @Test
    public void updaterTest() {

        Mall mall = new Mall();
        mall.setId("001");
        mall.setName("新世界");

        UpdateBuilder<User> updateBuilder = new UpdateBuilder<>(User.class);
        updateBuilder
                .set("a", 1)
                .set("b", 2)
                .set("Mall", mall)
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
                .addEachToSet("m", Arrays.asList(13, 14, 15))
                .push("n", 16)
                .pushEach("o",Arrays.asList(17, 18, 19))
                .pull("p", 20)
                .pullAll("q", Arrays.asList(21, 22))
                .popFirst("r")
                .popLast("s")
                .bitwiseAnd("t", 10);


        Bson bson = updateBuilder.build();
        log.info(bson.toBsonDocument().toJson());


        Assert.assertEquals("{\"$set\": {\"a\": 1, \"b\": 2, \"Mall\": {\"_id\": \"001\", \"Name\": \"新世界\", \"Status\": 1}}, \"$unset\": {\"c\": \"\"}, \"$setOnInsert\": {\"d\": 4}, \"$rename\": {\"e\": \"ee\"}, \"$inc\": {\"f\": 6}, \"$mul\": {\"g\": 7}, \"$min\": {\"h\": 10}, \"$max\": {\"h\": 11}, \"$currentDate\": {\"j\": true, \"k\": {\"$type\": \"timestamp\"}}, \"$addToSet\": {\"l\": 12, \"m\": {\"$each\": [13, 14, 15]}}, \"$push\": {\"n\": 16, \"o\": {\"$each\": [17, 18, 19]}}, \"$pull\": {\"p\": 20}, \"$pullAll\": {\"q\": [21, 22]}, \"$pop\": {\"r\": -1, \"s\": 1}, \"$bit\": {\"t\": {\"and\": 10}}}"
                , bson.toBsonDocument().toJson()
        );
    }


}
