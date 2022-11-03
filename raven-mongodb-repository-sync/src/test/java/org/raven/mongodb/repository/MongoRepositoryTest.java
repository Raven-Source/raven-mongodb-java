package org.raven.mongodb.repository;

import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
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
import org.raven.mongodb.repository.model.Mall;
import org.raven.mongodb.repository.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MongoRepositoryTest {

    private int size = 90;

    //@After
    @Before
    public void a1_init() {

        ClassModel<User> classModel = ClassModelUtils.getClassModel(User.class);

        System.out.println(classModel.getName());

        MongoBaseRepository<User> userRepository = new UserRepositoryImpl();
        userRepository.getDatabase().drop();
        userRepository.getCollection().createIndex(Indexes.ascending("User_id"));
        userRepository.getCollection().createIndex(Indexes.descending("CreateDate"));

        MongoRepository<Mall, String> mallRepository = new MallRepositoryImpl();
        mallRepository.getCollection().createIndex(Indexes.ascending("Mall_Name"), new IndexOptions().unique(true));

//        DefaultMongoSession defaultMongoSession = ((DefaultMongoSession) MongoSessionInstance.mongoSession);
//        Morphia morphia = new Morphia();
//        MongoClient mongo = new MongoClient("127.0.0.1", 27017);
//        Datastore datastore = morphia.createDatastore(mongo, "RepositoryTest");

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

        repos.insert(user);

        Assert.assertNotEquals(user.getId().longValue(), 0);

        user = repos.findOne(user.getId());
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getName(), uuid);


        Mall mall = new Mall();
        mall.setName("shopping mall");

        MongoRepository<Mall, String> mall_repos = new MallRepositoryImpl();
        mall_repos.insert(mall);
        log.info("insert mall success");
        try {
            mall_repos.insert(mall);
        } catch (MongoWriteException ex) {
            log.info("insert mall fail: " + ex.getMessage());
        } catch (Exception ex) {
            throw ex;
        }

        //Assert.assertNotNull(mall.getId());

    }

    @Test
    public void a3_insertBatch() {

        ArrayList<User> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            User user = new User();
            user.setName(UUID.randomUUID().toString());
            user.setAge(i * 10);
            list.add(user);
        }

        MongoRepository<User, Long> repos = new UserRepositoryImpl();
        repos.insertBatch(list);

        for (User user : list) {
            Assert.assertNotEquals(user.getId().longValue(), 0);
        }

        long count = repos.count(CountOptions.Empty());
        Assert.assertEquals(count, size);

    }

    @Test
    public void a4_update() {

        new MongoRepositoryTest().a3_insertBatch();

        MongoRepository<User, Long> repos = new UserRepositoryImpl();
        List<User> users = repos.findList(FindOptions.Empty().limit(10));

        for (User user : users) {
            repos.updateOne(
                    UpdateOptions.Empty()
                            .filter(
                                    Filters.eq("_id", user.getId())
                            )
                            .update(Updates.inc("Version", 9))

            );
        }

        User user = users.get(0);

        Long result = repos.updateOne(Filters.eq("_id", user.getId()), user);

        Assert.assertEquals(result > 0, true);

    }

    @Test
    public void a5_update() {

        new MongoRepositoryTest().a3_insertBatch();

        MongoRepository<User, Long> repos = new UserRepositoryImpl();
        List<User> users = repos.findList(FindOptions.Empty().limit(1));
        User user = users.get(0);

        int age = user.getAge();
        Long version = user.getVersion();

        user = repos.findOneAndUpdate(
                (FindOneAndUpdateOptions) FindOneAndUpdateOptions.Empty()
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

        new MongoRepositoryTest().a3_insertBatch();

        MongoRepository<User, Long> repos = new UserRepositoryImpl();
        List<User> users = repos.findList(FindOptions.Empty().limit(1));
        User user = users.get(0);

        Mall mall = new Mall();
        mall.setId("001");
        mall.setName("新世界");

        Bson updates = Updates.set("Mall", mall);
        repos.updateOne(
                UpdateOptions.Empty()
                        .filter(
                                Filters.eq("_id", user.getId())
                        )
                        .update(updates)

        );

        user = repos.findOne(user.getId());
        Assert.assertEquals(user.getMall().getName(), mall.getName());

    }
}
