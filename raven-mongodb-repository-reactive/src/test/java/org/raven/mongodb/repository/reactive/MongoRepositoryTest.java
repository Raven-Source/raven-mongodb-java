package org.raven.mongodb.repository.reactive;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.raven.mongodb.repository.CountOptions;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.UUID;

public class MongoRepositoryTest {

    int size = 10;

    //@After
    @Before
    public void init() {

        ReactiveMongoBaseRepository<User> repos = new UserRepositoryImpl();
        Mono.from(repos.getDatabase().drop()).block();
    }

    @Test
    public void insert() throws Exception {

        User user = new User();
        String uuid = UUID.randomUUID().toString();
        user.setName(uuid);
        user.setAge(123);

        ReactiveMongoRepository<User, Long> repos = new UserRepositoryImpl();
        repos.insert(user).block();

        Assert.assertNotEquals(user.getId().longValue(), 0);

        user = repos.get(user.getId()).block().orElse(null);
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getName(), uuid);


        Mall mall = new Mall();
        mall.setName("shopping mall");

        ReactiveMongoRepository<Mall, String> mall_repos = new MallRepositoryImpl();
        mall_repos.insert(mall).block();


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

        ReactiveMongoRepository<User, Long> repos = new UserRepositoryImpl();
        repos.insertBatch(list).block();

        for (User user : list) {
            Assert.assertNotEquals(user.getId().longValue(), 0);
        }

        long count = repos.count(CountOptions.Empty()).block();
        Assert.assertEquals(count, size);

    }
}
