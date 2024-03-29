//package org.raven.mongodb.repository.reactive;
//
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.Sorts;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.raven.mongodb.repository.FindOptions;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class AsyncMongoReaderRepositoryTest {
//    private int size = 10;
//
//    @Before
//    public void init() throws Exception {
//
//        ReactiveMongoRepository<User, Long> repos = new UserAsyncRepositoryImpl();
//        Mono.from(repos.getDatabase().drop()).block();
//
//        ArrayList<User> list = new ArrayList<>();
//        for (int i = 1; i <= size; i++) {
//
//            User user = new User();
//            user.setName(UUID.randomUUID().toString());
//            user.setAge(i * 10);
//            list.add(user);
//        }
//
//        repos.insertBatch(list).block();
//    }
//
////    @After
////    public void clear() {
////
////        UserRepositoryImpl repos = new UserRepositoryImpl();
////        repos.getDatabase().drop();
////    }
//
//    @Test
//    public void getList() {
//
//        List<User> list = null;
//
//        ReactiveMongoRepository<User, Long> repos = new UserReactiveRepositoryImpl();
//        list = repos.getList(FindOptions.Empty()).block();
//        Assert.assertNotEquals(list.size(), 0);
//
//        list = repos.getList(Filters.gte("_id", 1)).block();
//        Assert.assertNotEquals(list.size(), 0);
//
//        for (User user : list) {
//            Assert.assertNotNull(user.getName());
//        }
//
//        list = repos.getList(Filters.eq("_id", 1)).block();
//        Assert.assertEquals(list.size(), 1);
//
//
//        list = repos.getList(null, null, Sorts.descending("_id"), 1, 0).block();
//        Assert.assertNotNull(list.get(0));
//        Assert.assertEquals(list.size(), 1);
//        Assert.assertEquals(list.get(0).getId().longValue(), 10);
//
//    }
//
//    @Test
//    public void get() {
//
//        ReactiveMongoReadonlyRepository<User, Long> repos = new UserReactiveRepositoryImpl();
//        User user = null;
//        for (long i = 1; i <= size; i++) {
//            user = repos.get(i).block().orElse(null);
//            Assert.assertNotNull(user);
//
//            user = repos.get(Filters.eq("Name", user.getName())).block().orElse(null);
//            Assert.assertNotNull(user);
//
//            user = repos.get(Filters.eq("Name", user.getName()), new ArrayList<String>() {{
//                add("_id");
//            }}).block().orElse(null);
//            Assert.assertEquals(user.getName(), null);
//
//        }
//
//        user = repos.get(Filters.eq("Name", "_none")).block().orElse(null);
//        Assert.assertNull(user);
//
//        repos.get(Filters.eq("Name", "_none")).map(u -> {
//            Assert.assertTrue(u.isEmpty());
//            return true;
//        }).block();
//
//        User2ReactiveRepositoryImpl repos2 = new User2ReactiveRepositoryImpl();
//        User2 user2 = repos2.get(1L).block().orElse(null);
//        Assert.assertNotNull(user2);
//
//
//    }
//
//}
