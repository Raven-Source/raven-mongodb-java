package org.raven.mongodb.test;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.val;
import org.raven.commons.util.DateTimeUtils;
import org.raven.mongodb.MongoRepository;
import org.raven.mongodb.test.model.Mall;
import org.raven.mongodb.test.model.Orders;
import org.raven.mongodb.test.model.Status;
import org.raven.mongodb.test.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class MongoRepositoryTestBase {

    protected int seed = 90;
    protected MongoRepository<User, Long> userRepository = new UserRepositoryImpl();
    protected MongoRepository<Orders, Long> ordersRepository = new OrdersRepositoryImpl();
    protected MongoRepository<Mall, String> mallRepository = new MallRepositoryImpl();


    public void baseInit() {

        userRepository.getDatabase().drop();
        userRepository.getCollection().createIndex(Indexes.ascending("User_id"));
        userRepository.getCollection().createIndex(Indexes.ascending("Mall._id"));
        userRepository.getCollection().createIndex(Indexes.descending("CreateDate"));


        mallRepository.getCollection().createIndex(Indexes.ascending("Mall_Name"), new IndexOptions().unique(true));

        Mall mall = null;

        List<User> users = new ArrayList<>();

        for (int i = 1; i <= seed; i++) {

            mall = new Mall();
            mall.setId("m" + (i / 3));
            mall.setName("大悦城");

            User user = new User();
            user.setName(UUID.randomUUID().toString());
            user.setMall(mall);
            user.setAge(i * 10);
            users.add(user);
        }


        val result = userRepository.insertMany(users);
        System.out.println(result.size());

        List<Long> uids = users.stream().map(User::getId).collect(Collectors.toList());

        List<Orders> ordersList = new ArrayList<>();
        Orders orders = null;

        for (int i = 0; i < seed * 2; i++) {
            orders = new Orders();

            orders.setUid((long) uids.get(i / 2));
            orders.setItemsId(
                    (long) uids.get(i / 3)
            );
            orders.setName("订单" + i);

            if (i % 3 == 0) {
                orders.setIsPay(true);
            }

            if (i % 5 == 0) {
                orders.setStatus(Status.Finish);
            }

            orders.setPrice(BigDecimal.valueOf(Math.random() * 100D));

            orders.setCreateTime(DateTimeUtils.parse("20230102", DateTimeUtils.DATE_DAY_FORMAT));

            orders.getRefs().addAll(Arrays.asList(i, i + 1, i + 2, i + 3, i + 4));

            ordersList.add(orders);

        }

        val result2 = ordersRepository.insertMany(ordersList);
        System.out.println(result2.size());
    }
}
