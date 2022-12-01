package org.raven.mongodb.repository;

import com.mongodb.client.ClientSession;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.raven.mongodb.repository.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author by yanfeng
 * date 2021/9/13 22:19
 */
public class FindAndModifyTest {


    UserRepositoryImpl userRepos;
    User3RepositoryImpl user3Repos;

    @Before
    public void init() {
        userRepos = new UserRepositoryImpl();
        user3Repos = new User3RepositoryImpl();

//        User user = userRepos.get(1L);
//        User3 user3 = new User3();
//        user3.setAge(user.getAge());
    }

    @Test
    public void test() throws Exception {

        int seed = 15;

        User user = userRepos.findOne(1L);
        int age = user.getAge();

        List<CompletableFuture<?>> failures = new ArrayList<>(seed);

        long start = System.currentTimeMillis();

        for (int i = 0; i < seed; i++) {

            CompletableFuture<User> future = CompletableFuture.supplyAsync(() ->
                    userRepos.findOneAndUpdate(Filters.eq("_id", 1), Updates.inc("Age", 1))
            );

            failures.add(future);

        }

        CompletableFuture.allOf(failures.toArray(new CompletableFuture[0])).get();

        long end = System.currentTimeMillis();

        System.out.println(seed / (double) (end - start) * 1000);

        user = userRepos.findOne(1L);

        Assert.assertEquals(age + seed, user.getAge());
    }


    @Test
    public void test2() throws Exception {


        int seed = 10;

        userRepos.findOne(1L);

        List<CompletableFuture<?>> failures = new ArrayList<>(seed);

        long start = System.currentTimeMillis();

        for (int i = 0; i < seed; i++) {

            CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {

                MongoSession mongoSession = MongoSessionInstance.mongoSession;
                ClientSession clientSession = null;

                try {
                    // 开启事务
                    clientSession = mongoSession.getMongoClient().startSession();
                    clientSession.startTransaction();

                    user3Repos.findOneAndUpdate(Filters.eq("_id", 1), Updates.inc("Age", 1));

//                    // 回滚事务
//                    clientSession.abortTransaction();

                    User u = userRepos.findOneAndUpdate(Filters.eq("_id", 1), Updates.inc("Age", 1));
                    clientSession.commitTransaction();
                    return u;
                } catch (Exception ex) {
                    if (clientSession != null) {
                        clientSession.abortTransaction();
                    }
                    return null;
                } finally {
                    if (clientSession != null) {
                        clientSession.close();
                    }
                }

            });

            failures.add(future);

        }

        CompletableFuture.allOf(failures.toArray(new CompletableFuture[0])).get();

        long end = System.currentTimeMillis();

        System.out.println(seed / (double) (end - start) * 1000);
    }
}
