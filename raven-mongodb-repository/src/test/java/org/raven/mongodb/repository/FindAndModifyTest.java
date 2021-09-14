package org.raven.mongodb.repository;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.raven.mongodb.repository.query.FilterBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author by yanfeng
 * date 2021/9/13 22:19
 */
public class FindAndModifyTest {

    @Test
    public void test() throws Exception {

        int seed = 100000;

        MongoRepository<User, Long> repos = new UserRepositoryImpl();
        repos.get(1L);

        List<CompletableFuture<?>> failures = new ArrayList<>(seed);

        long start = System.currentTimeMillis();

        for (int i = 0; i < seed; i++) {

            CompletableFuture<User> future = CompletableFuture.supplyAsync(() ->
                    repos.findOneAndUpdate(Filters.eq("_id", 1), Updates.inc("Age", 1))
            );

            failures.add(future);

        }

        CompletableFuture.allOf(failures.toArray(new CompletableFuture[0])).get();

        long end = System.currentTimeMillis();

        System.out.println(seed / (double)(end - start) * 1000);

    }
}
