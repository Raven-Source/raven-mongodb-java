package org.raven.mongodb;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import lombok.val;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.raven.mongodb.contants.BsonConstant;
import org.raven.mongodb.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoReadonlyRepositoryTest {
    private final int seed = 90;
    MongoRepository<User, Long> userRepository = new UserRepositoryImpl();

    MongoRepository<Orders, Long> ordersRepository = new OrdersRepositoryImpl();

    @Before
    public void init() {

        userRepository.getDatabase().drop();
        userRepository.getCollection().createIndex(Indexes.ascending("Mall._id"));
        userRepository.getCollection().createIndex(Indexes.descending("CreateDate"));

        Mall mall = null;

        ArrayList<User> list = new ArrayList<>();
        for (int i = 1; i <= seed; i++) {

            mall = new Mall();
            mall.setId("m" + (i / 3));
            mall.setName("大悦城");

            User user = new User();
            user.setName(UUID.randomUUID().toString());
            user.setMall(mall);
            user.setAge(i * 10);
            list.add(user);
        }

        val result = userRepository.insertBatch(list);
        System.out.println(result);
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

        list = userRepository.findList(FindOptions.create());
        Assert.assertNotEquals(list.size(), 0);

        list = userRepository.findList(Filters.gte("_id", 1));
        Assert.assertNotEquals(list.size(), 0);

        for (User user : list) {
            Assert.assertNotNull(user.getName());
        }

        list = userRepository.findList(Filters.eq("_id", 1));
        Assert.assertEquals(list.size(), 1);


        list = userRepository.findList(null, null, Sorts.descending("_id"), 1, 0);
        Assert.assertNotNull(list.get(0));
        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.get(0).getId().longValue(), seed);

        list = userRepository.findList(Filters.eq("Mall._id", "m3"), null
                , Sorts.descending("CreateDate"), seed, 0
                , Indexes.descending("CreateDate"), null);

        Assert.assertEquals(list.size(), 3);

    }

    @Test
    public void get() {

        User user = null;
        for (long i = 1; i <= seed; i++) {
            user = userRepository.findOne(i);
            Assert.assertNotNull(user);

            user = userRepository.findOne(Filters.eq("Name", user.getName()));
            Assert.assertNotNull(user);

            user = userRepository.findOne(Filters.eq("Name", user.getName()), new ArrayList<String>() {{
                add("_id");
            }});
            Assert.assertNull(user.getName());
        }

        User2RepositoryImpl repos2 = new User2RepositoryImpl();
        User2 user2 = repos2.findOne(1L);
        Assert.assertNotNull(user2);

        Long id = user2.getId();
        user2 = repos2.findOne(f -> {
            return f.eq(BsonConstant.PRIMARY_KEY_NAME, id).build();
        });
        Assert.assertNotNull(user2);

        FindOptions findOptions = FindOptions.create().filter(Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id));
        user2 = userRepository.findOne(findOptions, User2.class);
        user = userRepository.findOne(id);
        Assert.assertEquals(user.getName(), user2.getName());


//        Orders orders;
//        orders = ordersRepository.findOne(1L);
//
//        orders = ordersRepository.findOne(
//                f -> f
//                        .eq(Orders.Fields.status, Status.Normal)
//                        .gt(Orders.Fields.price, 1.0)
//                        .build()
//        );
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
    }

    @Test
    public void countTest() {
        long count = userRepository.count((Bson) null);
        Assert.assertEquals(count, seed);

        count = userRepository.count(new CountOptions().skip(5));
        Assert.assertEquals(count, seed - 5);

    }

}
