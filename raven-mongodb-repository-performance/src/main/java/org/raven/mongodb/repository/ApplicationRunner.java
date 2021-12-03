package org.raven.mongodb.repository;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author by yanfeng
 * date 2021/11/28 14:37
 */
@Configuration
public class ApplicationRunner implements CommandLineRunner {

    private User3RepositoryImpl user3Repos;

    private UserRepositoryImpl userRepos;

    @Autowired
    private MongoSession mongoSession;

//    ExecutorService executorService = Executors.newFixedThreadPool(100);

    @Override
    public void run(String... args) throws Exception {

        userRepos = new UserRepositoryImpl(mongoSession);

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);

        rootLogger = loggerContext.getLogger("org.raven.mongodb.repository");
        rootLogger.setLevel(Level.OFF);


        int seed = 100000;
        for (String arg : args) {
            if (arg != null) {
                String[] val = arg.split("=");
                if (val.length >= 2) {
                    if (val[0].equals("seed")) {
                       seed = Integer.parseInt(val[1]);
                    }
                }
            }
        }

        System.out.println("start seed:" + seed);

        User user = userRepos.get(1L);
        if (user == null) {
            user = new User();

            System.out.println("insert user");
            userRepos.insert(user);
        }

        List<CompletableFuture<?>> failures = new ArrayList<>(seed);

        long start = System.currentTimeMillis();

        long id = user.getId();
        for (int i = 0; i < seed; i++) {

            CompletableFuture<User> future = CompletableFuture.supplyAsync(() ->
                    userRepos.findOneAndUpdate(Filters.eq("_id", id), Updates.inc("Age", 1))
                    //, executorService
            );

            failures.add(future);

        }

        CompletableFuture.allOf(failures.toArray(new CompletableFuture[0])).get();

        long end = System.currentTimeMillis();

        System.out.println("qps:" + seed / (double) (end - start) * 1000.0);

        System.out.println("end");
    }
}
