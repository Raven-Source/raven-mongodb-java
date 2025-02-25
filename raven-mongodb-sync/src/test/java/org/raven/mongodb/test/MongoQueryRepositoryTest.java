package org.raven.mongodb.test;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.raven.commons.util.Lists;
import org.raven.mongodb.contants.BsonConstant;
import org.raven.mongodb.criteria.CountOptions;
import org.raven.mongodb.criteria.FindOptions;
import org.raven.mongodb.test.model.*;
import org.raven.mongodb.test.model.Orders.Fields;

import java.util.List;

public class MongoQueryRepositoryTest extends MongoRepositoryTestBase {

    @Before
    public void init() {
        super.baseInit();
    }

//    @After
//    public void clear() {
//
//        UserRepositoryImpl repos = new UserRepositoryImpl();
//        repos.getDatabase().drop();
//    }

    @Test
    public void getList() {

        List<User> list = null;

        list = userRepository.findMany(FindOptions.create());
        Assert.assertNotEquals(list.size(), 0);

        list = userRepository.findMany(Filters.gte("_id", 1));
        Assert.assertNotEquals(list.size(), 0);

        for (User user : list) {
            Assert.assertNotNull(user.getName());
        }

        list = userRepository.findMany(Filters.eq("_id", 1));
        Assert.assertEquals(list.size(), 1);


        list = userRepository.findMany(null, null, Sorts.descending("_id"), 1, 0);
        Assert.assertNotNull(list.get(0));
        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.get(0).getId().longValue(), seed);

        list = userRepository.findMany(Filters.eq("Mall._id", "m3"), null
                , Sorts.descending("CreateDate"), seed, 0
                , Indexes.descending("CreateDate"), null);

        Assert.assertEquals(list.size(), 3);


        List<Orders> ordersList;
        ordersList = ordersRepository.findMany(
                f -> f
                        .isNotNull(Fields.itemsId)
                        .eq(Fields.status, Status.Normal)
        );

        FindOptions findOptions = FindOptions.Builder.create(Orders.class)
                .filter(f -> f
                        .eq(Fields.status, Status.Normal)
                        .gt(Fields.price, 79.0)
                )
                .sort(s -> s
                        .asc(Fields.itemsId)
                )
                .projection(p -> p
                        .include(Fields.name, Fields.itemsId)
                        .excludeId()
                )
                .skip(3)
                .limit(10)
                .build();

        ordersList = ordersRepository.findMany(findOptions);


        List<Long> uids = Lists.newArrayList(2L, 3L, 4L);

        ordersList = ordersRepository.findMany(f -> f
                .eq(Fields.status, Status.Finish)
                .condition(uids != null && !uids.isEmpty(), x -> x.in(Fields.uid, uids))
        );

        ordersList = ordersRepository.findMany(
                f -> f.eq(Fields.status, Status.Finish),
                p -> p.include(Fields.name, Fields.uid).exclude(Fields.id)
        );


        Assert.assertTrue(ordersList.size() > 1);

    }

    @Test
    public void get() {

        User user = null;
        for (long i = 1; i <= seed; i++) {
            user = userRepository.findOne(i);
            Assert.assertNotNull(user);

            user = userRepository.findOne(Filters.eq("Name", user.getName()));
            Assert.assertNotNull(user);

            user = userRepository.findOne(Filters.eq("Name", user.getName())
                    , Projections.include("_id"));
            Assert.assertNull(user.getName());
        }

        User2RepositoryImpl repos2 = new User2RepositoryImpl();
        User2 user2 = repos2.findOne(1L);
        Assert.assertNotNull(user2);

        Long id = user2.getId();
        user2 = repos2.findOne(f -> {
            return f.eq(BsonConstant.PRIMARY_KEY_NAME, id);
        });
        Assert.assertNotNull(user2);

        FindOptions findOptions = FindOptions.create().filter(Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id));
        user2 = userRepository.findOne(findOptions, User2.class);
        user = userRepository.findOne(id);
        Assert.assertEquals(user.getName(), user2.getName());


        Orders orders;
        orders = ordersRepository.findOne(1L);

        orders = ordersRepository.findOne(
                f -> f
                        .eq(Fields.status, Status.Normal)
                        .gt(Fields.price, 79.0)
        );

//
//        Long itemsId = orders.getItemsId();
//        ordersRepository.updateOne(
//                f -> f
//                        .eq(Orders.Fields.itemsId, itemsId)
//                        .ne(Orders.Fields.isPay, false)
//                        .build(),
//                u -> u
//                        .set(Orders.Fields.status, Status.Delete)
//                        .build()
//        );

       long count = ordersRepository.deleteOne(orders.getId());

    }

    @Test
    public void countTest() {
        long count = userRepository.count((Bson) null);
        Assert.assertEquals(count, seed);

        count = userRepository.count(new CountOptions().skip(5));
        Assert.assertEquals(count, seed - 5);


        CountOptions countOptions = CountOptions.Builder.create(Orders.class)
                .filter(f -> f
                        .eq(Fields.status, Status.Normal)
                )
                .build();
        count = ordersRepository.count(countOptions);
        System.out.println("count: " + count);
        count = ordersRepository.count(countOptions.skip(5));
        System.out.println("count: " + count);


    }

}
