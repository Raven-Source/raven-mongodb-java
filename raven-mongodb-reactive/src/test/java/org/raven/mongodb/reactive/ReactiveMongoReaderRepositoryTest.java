package org.raven.mongodb.reactive;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.raven.mongodb.criteria.FindOptions;
import org.raven.mongodb.test.model.User;
import org.raven.mongodb.test.model.User2;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

@SuppressWarnings("")
public class ReactiveMongoReaderRepositoryTest {
    private final int size = 10;

    @Before
    public void init() {

        ReactiveMongoRepository<User, Long> repos = new UserReactiveRepositoryImpl();
        Mono.from(repos.getDatabase().drop()).block();

        ArrayList<User> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {

            User user = new User();
            user.setName(UUID.randomUUID().toString());
            user.setAge(i * 10);
            list.add(user);
        }

        repos.insertMany(list).block();
    }

//    @After
//    public void clear() {
//
//        UserRepositoryImpl repos = new UserRepositoryImpl();
//        repos.getDatabase().drop();
//    }

    @Test
    public void getList() throws Exception {

        Semaphore semaphore = new Semaphore(0);

        ReactiveMongoRepository<User, Long> repos = new UserReactiveRepositoryImpl();
        repos.findMany(FindOptions.create())
                .subscribe(
                        list -> {
                            Assert.assertNotEquals(list.size(), 0);
                            System.out.println("list.size: " + list.size());
                            semaphore.release();
                        }
                );

        semaphore.acquire(1);

        repos.findMany(Filters.gte("_id", 1))
                .subscribe(
                        list -> {
                            Assert.assertNotEquals(list.size(), 0);

                            for (User user : list) {
                                Assert.assertNotNull(user.getName());
                            }
                            System.out.println("list.size: " + list.size());
                            semaphore.release();
                        }
                );

        semaphore.acquire(1);

        repos.findMany(Filters.eq("_id", 1))
                .subscribe(list -> {

                    Assert.assertEquals(list.size(), 1);
                    System.out.println("list.size: " + list.size());
                    semaphore.release();
                });

        semaphore.acquire(1);

        repos.findMany(null, null, Sorts.descending("_id"), 1, 0)
                .subscribe(list -> {

                    Assert.assertNotNull(list.get(0));
                    Assert.assertEquals(list.size(), 1);
                    Assert.assertEquals(list.get(0).getId().longValue(), 10);
                    System.out.println("list.size: " + list.size());

                    semaphore.release();
                });

        semaphore.acquire(1);
    }

    @Test
    public void get() {

        ReactiveMongoQueryRepository<User, Long> repos = new UserReactiveRepositoryImpl();
        User user = null;
        for (long i = 1; i <= size; i++) {
            user = repos.findOne(i).block().orElse(null);
            Assert.assertNotNull(user);

            user = repos.findOne(Filters.eq("Name", user.getName())).block().orElse(null);
            Assert.assertNotNull(user);

            user = repos.findOne(Filters.eq("Name", user.getName())
                    , Projections.include("_id")).block().orElse(null);
            Assert.assertEquals(user.getName(), null);

        }

        user = repos.findOne(Filters.eq("Name", "_none")).block().orElse(null);
        Assert.assertNull(user);

        repos.findOne(Filters.eq("Name", "_none")).map(u -> {
            Assert.assertTrue(!u.isPresent());
            return true;
        }).block();

        User2ReactiveRepositoryImpl repos2 = new User2ReactiveRepositoryImpl();
        User2 user2 = repos2.findOne(1L).block().orElse(null);
        Assert.assertNotNull(user2);


    }

}
