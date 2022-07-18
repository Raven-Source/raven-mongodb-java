package org.raven.mongodb.repository;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.InsertManyResult;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.raven.mongodb.repository.contants.BsonConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoReadonlyRepositoryTest {
    private int size = 90;
    MongoRepository<User, Long> repos = new UserRepositoryImpl();

    @Before
    public void init() throws Exception {

        repos.getDatabase().drop();
        repos.getCollection().createIndex(Indexes.ascending("Mall._id"));
        repos.getCollection().createIndex(Indexes.descending("CreateDate"));

        Mall mall = null;

        ArrayList<User> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {

            mall = new Mall();
            mall.setId("m" + (i / 3));
            mall.setName("大悦城");

            User user = new User();
            user.setName(UUID.randomUUID().toString());
            user.setMall(mall);
            user.setAge(i * 10);
            list.add(user);
        }

        InsertManyResult result = repos.insertBatch(list);
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

        list = repos.findList(FindOptions.Empty());
        Assert.assertNotEquals(list.size(), 0);

        list = repos.findList(Filters.gte("_id", 1));
        Assert.assertNotEquals(list.size(), 0);

        for (User user : list) {
            Assert.assertNotNull(user.getName());
        }

        list = repos.findList(Filters.eq("_id", 1));
        Assert.assertEquals(list.size(), 1);


        list = repos.findList(null, null, Sorts.descending("_id"), 1, 0);
        Assert.assertNotNull(list.get(0));
        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.get(0).getId().longValue(), size);

        list = repos.findList(Filters.eq("Mall._id", "m3"), null
                , Sorts.descending("CreateDate"), size, 0
                , Indexes.descending("CreateDate"), null);

        Assert.assertEquals(list.size(), 3);

    }

    @Test
    public void get() {

        User user = null;
        for (long i = 1; i <= size; i++) {
            user = repos.findOne(i);
            Assert.assertNotNull(user);

            user = repos.findOne(Filters.eq("Name", user.getName()));
            Assert.assertNotNull(user);

            user = repos.findOne(Filters.eq("Name", user.getName()), new ArrayList<String>() {{
                add("_id");
            }});
            Assert.assertEquals(user.getName(), null);
        }

        User2RepositoryImpl repos2 = new User2RepositoryImpl();
        User2 user2 = repos2.findOne(1L);
        Assert.assertNotNull(user2);

        Long id = user2.getId();
        user2 = repos2.findOne(f -> {
            return f.eq(BsonConstant.PRIMARY_KEY_NAME, id).build();
        });
        Assert.assertNotNull(user2);

        FindOptions findOptions = (FindOptions) FindOptions.Empty().filter(Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id));
        user2 = repos.findOne(findOptions, User2.class);
        user = repos.findOne(id);
        Assert.assertEquals(user.getName(), user2.getName());

    }

    @Test
    public void countTest() {
        long count = repos.count((Bson) null);
        Assert.assertEquals(count, size);

        count = repos.count(new CountOptions().skip(5));
        Assert.assertEquals(count, size - 5);

    }

}
