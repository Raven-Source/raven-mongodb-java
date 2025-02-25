package org.raven.mongodb.test;

import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.ClassModelUtils;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.raven.commons.util.Lists;
import org.raven.mongodb.MongoRepository;
import org.raven.mongodb.criteria.*;
import org.raven.mongodb.test.model.Mall;
import org.raven.mongodb.test.model.Orders;
import org.raven.mongodb.test.model.Status;
import org.raven.mongodb.test.model.User;
import org.raven.mongodb.test.model.Orders.Fields;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.logging.Level;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MongoRepositoryTest extends MongoRepositoryTestBase {

    @Before
    public void a1_init() {

        super.baseInit();

        ILoggerFactory loggerContext = (ILoggerFactory) LoggerFactory.getILoggerFactory();

        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.ALL);

        ClassModel<User> classModel = ClassModelUtils.getClassModel(User.class);

        System.out.println(classModel.getName());

    }

    @Test
    public void a2_insert() throws Exception {

        User user = new User();
        String uuid = UUID.randomUUID().toString();
        user.setName(uuid);
        user.setAge(123);

        UserRepositoryImpl repos = new UserRepositoryImpl();

        BsonDocument bsonDocument = repos.getEntityInformation().toBsonDocument(user);
        int size = bsonDocument.size();

        System.out.println("user.id:  " + user.getId());
        repos.insert(user);
        System.out.println("user.id:  " + user.getId());

        Assert.assertNotEquals(user.getId().longValue(), 0);

        user = repos.findOne(user.getId());
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getName(), uuid);


        //Assert.assertNotNull(mall.getId());

    }

    @Test
    public void a3_insertBatch() {

        ArrayList<User> list = new ArrayList<>();
        for (int i = 1; i <= seed; i++) {
            User user = new User();
            user.setName(UUID.randomUUID().toString());
            user.setAge(i * 10);
            list.add(user);
        }

        userRepository.getCollection().drop();
        userRepository.insertMany(list);

        for (User user : list) {
            Assert.assertNotEquals(user.getId().longValue(), 0);
        }

        long count = userRepository.count(CountOptions.create());
        Assert.assertEquals(count, seed);

    }

    @Test
    public void a4_update() {

        MongoRepository<User, Long> repos = new UserRepositoryImpl();
        List<User> users = repos.findMany(FindOptions.create().limit(10));

        for (User user : users) {
            repos.updateOne(
                    UpdateOptions.create()
                            .filter(
                                    Filters.eq("_id", user.getId())
                            )
                            .update(Updates.inc("Version", 9))

            );
        }

        User user = users.get(0);

        Long result = repos.updateOne(Filters.eq("_id", user.getId()), user);

        Assert.assertTrue(result > 0);

    }

    @Test
    public void a5_update() {

        MongoRepository<User, Long> repos = new UserRepositoryImpl();
        List<User> users = repos.findMany(FindOptions.create().limit(1));
        User user = users.get(0);

        int age = user.getAge();
        Long version = user.getVersion();

        user = repos.findOneAndUpdate(
                (FindOneAndUpdateOptions) FindOneAndUpdateOptions.create()
                        .filter(
                                Filters.eq("_id", user.getId())
                        )
                        .update(Updates.inc("Age", 5))

        );

        Assert.assertEquals(user.getAge(), age + 5);
        Assert.assertEquals(user.getVersion().longValue(), Long.sum(version, 1L));


    }


    @Test
    public void a6_update() {

        MongoRepository<User, Long> repos = new UserRepositoryImpl();
        List<User> users = repos.findMany(FindOptions.create().limit(1));
        User user = users.get(0);

        Mall mall = new Mall();
        mall.setId("001");
        mall.setName("新世界");

        Bson updates = Updates.set("Mall", mall);
        repos.updateOne(
                UpdateOptions.create()
                        .filter(
                                Filters.eq("_id", user.getId())
                        )
                        .update(updates)

        );

        user = repos.findOne(user.getId());
        Assert.assertEquals(user.getMall().getName(), mall.getName());

    }


    @Test
    public void a7_update() {

        MongoRepository<Mall, String> mall_repos = new MallRepositoryImpl();

        Mall mall = new Mall();
        mall.setName("shopping mall");

        mall_repos.insert(mall);
        MongoWriteException mongoWriteException = null;
        log.info("insert mall success");
        try {
            mall_repos.insert(mall);
        } catch (MongoWriteException e) {
            mongoWriteException = e;
            log.info("insert mall fail: " + e.getMessage());
        } catch (Exception e) {
            throw e;
        }

        Assert.assertNotNull(mongoWriteException);

        List<Mall> list = mall_repos.findMany((Bson) null);

        Bson filter = FilterBuilder.create(Mall.class).eq(Mall.Fields.id, list.get(0).getId()).build();
        Bson update = UpdateBuilder.create(Mall.class).set(Mall.Fields.status, Status.Delete).build();

        long c = mall_repos.updateOne(filter, update);
        Assert.assertEquals(c, 1L);


    }

    @Test
    public void a8_update() {

        List<Orders> ordersList = ordersRepository.findAll();
        Orders orders = ordersList.get(0);

        List<Integer> refs = Lists.newArrayList(888, 999);
        Date now = new Date();
        long count = ordersRepository.updateOne(
                // Function<FilterBuilder<T>, Expression<Boolean>>
                orders.getId(),
                // UpdateSetExpression<T> updateSetExpression
                u -> u.set(Fields.name, 123)
                        .set(Fields.status, Status.Fail)
                        .set(Fields.isPay, false)
                        .set(Fields.createTime, now)
                        .inc(Fields.version, 1L)
                        .condition(!refs.isEmpty(), x -> x.set(Fields.refs, refs))
        );

        System.out.println("count: " + count);
        Assert.assertEquals(count, 1L);

        count = ordersRepository.updateMany(
                f -> f.eq(Fields.isPay, false)
                , u -> u.set(Fields.status, Status.Delete)
        );

        System.out.println("count: " + count);
        Assert.assertTrue(count > 1L);
    }
}
