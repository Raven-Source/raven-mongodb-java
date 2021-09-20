package org.raven.mongodb.repository;

import com.mongodb.client.model.Indexes;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.ClassModelUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

public class MongoRepositoryTest {

    private int size = 90;

    //@After
    @Before
    public void init() {

        ClassModel<User> classModel = ClassModelUtils.getClassModel(User.class);

        System.out.println(classModel.getName());

        MongoBaseRepository<User> repos = new UserRepositoryImpl();
        repos.getDatabase().drop();
        repos.getCollection().createIndex(Indexes.ascending("Mall._id"));
        repos.getCollection().createIndex(Indexes.descending("CreateDate"));

//        DefaultMongoSession defaultMongoSession = ((DefaultMongoSession) MongoSessionInstance.mongoSession);
//        Morphia morphia = new Morphia();
//        MongoClient mongo = new MongoClient("127.0.0.1", 27017);
//        Datastore datastore = morphia.createDatastore(mongo, "RepositoryTest");

    }

    @Test
    public void insert() throws Exception {

        User user = new User();
        String uuid = UUID.randomUUID().toString();
        user.setName(uuid);
        user.setAge(123);

        MongoRepository<User, Long> repos = new UserRepositoryImpl();
        repos.insert(user);

        Assert.assertNotEquals(user.getId().longValue(), 0);

        user = repos.get(user.getId());
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getName(), uuid);


        Mall mall = new Mall();
        mall.setName("shopping mall");

        MongoRepository<Mall, String> mall_repos = new MallRepositoryImpl();
        mall_repos.insert(mall);

        //Assert.assertNotNull(mall.getId());

    }

    @Test
    public void insertBatch() throws Exception {

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

    public void update(){

    }
}
