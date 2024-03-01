package org.raven.mongodb;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.TransactionBody;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.raven.mongodb.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author by yanfeng
 * date 2021/9/13 22:19
 */
@Slf4j
public class FindAndModifyTest {

    UserRepositoryImpl userRepos;
    User3RepositoryImpl user3Repos;

    TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.secondaryPreferred())
            .readConcern(ReadConcern.MAJORITY)
            .writeConcern(WriteConcern.W1)
            .build();

    @Before
    public void init() {
        userRepos = new UserRepositoryImpl();
        user3Repos = new User3RepositoryImpl();

        User user = new User();
        String uuid = UUID.randomUUID().toString();
        user.setName(uuid);
        user.setAge(123);
        userRepos.insert(user);
    }

    @Test
    public void test() throws Exception {

        int seed = 5000;

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

        int seed = 2000;

        userRepos.findOne(1L);

        CompletableFuture<?>[] failures = new CompletableFuture[seed];

        long start = System.currentTimeMillis();
        MongoSession mongoSession = MongoSessionInstance.mongoSession;

        for (int i = 0; i < seed; i++) {

            int finalI = i;

            final ClientSession clientSession1 = mongoSession.startSession();

            TransactionBody<User> runnable = () -> {
//                ClientSession clientSession2 = null;

                // 开启事务

//                    clientSession1.startTransaction();
//                System.out.println("startTransaction");

//                    user3Repos.modifyWithClientSession(clientSession1).findOneAndUpdate(Filters.eq("_id", 1), Updates.inc("Age", 1));

//                    // 回滚事务
//                    clientSession.abortTransaction();
//
//                    clientSession2 = mongoSession.getMongoClient().startSession();
//                    clientSession2.startTransaction();
                User u = null;
                u = userRepos
                        .modifyWithClientSession(clientSession1)
                        .findOneAndUpdate(Filters.eq("_id", 1), Updates.inc("Age", 1));


                if (finalI % 3 == 0) {
                    clientSession1.abortTransaction();
//                    System.out.println("abortTransaction");
                } else {
//                    clientSession1.commitTransaction();
//
//                  System.out.println("commitTransaction");
                }

                clientSession1.close();

                return u;

//                    clientSession2.commitTransaction();

//                    clientSession1.commitTransaction();
//                    return u;

            };

            CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {

                try {
                    return clientSession1.withTransaction(runnable, txnOptions);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    throw ex;
                }
            });

            failures[i] = future;

        }

        CompletableFuture.allOf(failures).get();

        long end = System.currentTimeMillis();

        System.out.println(seed / (double) (end - start) * 1000);
    }
}
