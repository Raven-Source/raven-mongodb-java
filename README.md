[![Security Status](https://www.murphysec.com/platform3/v3/badge/1609167474302918656.svg)](https://www.murphysec.com/accept?code=525262e8fa030ee439a8e27f10097af7&type=1&from=2&t=2)

# 简介 | intro

#### 基于 `mongodb-driver` 的一些封装和增强，提供 `orm` 常用的数据库操作方法：

#### 1. 默认支持基于数据库实现自增id。
#### 2. 批量插入时，自动批量填充id。
#### 3. 避免不同 `entity` 版本的 `save` 方法导致数据覆盖问题等。
#### 4. 枚举对应的数值类型和字符串类型的支持。
#### 5. 提供 `EntityInterceptor` 接口，针对 `find`、`insert`、`update`、`delete` 等操作的预处理能力
#### 6. 提供优雅的链式写法。
#### 7. 提供同步操作和异步（reactive）操作方法。

#### 目的是简化写法、提高开发效率，以及避免一些问题。


# 快速开始 | quick start

### 1. 添加依赖：

```xml
<dependencies>
    <!-- mongodb start -->
    <dependency>
        <groupId>io.github.raven-source</groupId>
        <artifactId>raven-mongodb-repository-sync</artifactId>
        <version>3.0.10</version>
    </dependency>

    <dependency>
        <groupId>org.mongodb</groupId>
        <artifactId>mongodb-driver-sync</artifactId>
        <version>4.7.2</version>
    </dependency>
    <!-- mongodb end -->
</dependencies>
```

### 2. 配置文件：

```yaml
mongodb.options.connString=mongodb://127.0.0.1:27017/?
mongodb.options.dbName=TestDB
```

### 3. 例如 `SpringBoot` 项目可以自定义个 `Configuration` 、实体对象、以及对应的 `UserRepository` ：

```java
@Configuration
public class DataSourceConfiguration {

    @Bean
    public MongoSession mongoSessionMain(MongoProperties mongoProperties) {

        DefaultMongoSession defaultMongoSession = new DefaultMongoSession(
                mongoProperties.getConnString(),
                mongoProperties.getDbName(),
                null,
                null
        );

        return defaultMongoSession;

    }

}

@FieldNameConstants
@Getter
@Setter
public class Orders implements AutoIncr<Long> {

    @BsonId()
    private Long id;

    private Long uid;

    private Long itemsId;

    private String name;

    private Boolean isPay = Boolean.FALSE;

    private BigDecimal price;

    private Status status;

}

@Repository
public class OrdersRepositoryImpl extends MongoRepositoryImpl<Orders, Long> {
    public OrdersRepositoryImpl(MongoSession mongoSession) {
        super(mongoSession);

    }
}

```

### 4. 查询 | findOne & findMany & count & exists，以下为 `OrderRepository`，包括不同条件、参数形式写法示例：

- `findOne` 根据主键或条件查询单条数据
 
```java

import org.raven.mongodb.test.model.Orders;
import org.raven.mongodb.test.model.Orders.Fields;
import org.raven.mongodb.test.model.Status;


Orders orders;
// 根据id查询
orders = ordersRepository.findOne(1L);

// 根据条件查询
// {
//     "status": 1,
//     "price": {
//         "$gt": 79.0
//     }
// }
orders = ordersRepository.findOne(
        // FilterBuilder<Orders>
        f -> f
            .eq(Fields.status, Status.Normal)
            .gt(Fields.price, 9.0)
);
```

- `findMany` 根据条件查询列表数据

```java

List<Order> orderList;

// 根据条件查询列表
// {
//     "itemsId": {
//         "$ne": null
//     }
//     "status": 1
// }
ordersList = ordersRepository.findMany(
        // FilterBuilder<Orders>
        f -> f
            .isNotNull(Fields.itemsId)
            .eq(Fields.status, Status.Normal)
        );

// 通过FindOptions.Builder创建条件
// {
//     "filter": {
//         "$and": [{
//             "status": 1
//         }, {
//             "price": {
//                 "$gt": 79.0
//             }
//         }]
//     },
//     "projection": {
//         "name": 1,
//         "itemsId": 1,
//         "_id": 0
//     },
//     "sort": {
//         "itemsId": 1
//     },
//     "limit": 10,
//     "skip": 3
// }
FindOptions findOptions =
        FindOptions.Builder.create(Orders.class)
                .filter(
                        // FilterBuilder<Orders>
                        f -> f
                            .eq(Fields.status, Status.Normal)
                            .gt(Fields.price, 9.0)
                )
                .sort(
                        // SortBuilder<Orders>
                        s -> s
                            .asc(Fields.itemsId)
                )
                .projection(
                        // ProjectionBuilder<Orders>
                        p -> p
                            .include(Fields.name, Fields.itemsId)
                            .excludeId()
                )
                .skip(3)
                .limit(10)
                .build();

orderList = ordersRepository.findMany(findOptions);


```

> `where` 复杂`or` `and` 的嵌套情况，注意使用 `FilterBuilder` 的`and()` `or()` `add()`方法组合，以及`build()`参数。

```java

// {
//  "$and": [{
//      "$or": [{
//          "price": {
//              "$gt": 198
//          }
//      }, {
//          "status": 3
//      }]
//  }, {
//      "$and": [{
//          "price": {
//              "$lt": 58
//          }
//      }, {
//          "status": 4
//      }]
//  }, {
//      "isPay": true
//  }]
// }
Bson filter = FilterBuilder.create(User.class)
                .add(x -> x
                        .orOperator()
                        .gt(Fields.price, 198)
                        .eq(Fields.status, Status.Finish))
                .add(y -> y
                        .lt(Fields.price, 58)
                        .eq(Fields.status, Status.Fail))
                .add(z -> z
                        .eq(Fields.isPay, true))
                .build();

orderList = ordersRepository.findMany(filter);
```

> 针对参数有条件判断的情况，可以使用 `condition(boolean condition, Consumer<FilterBuilder<TEntity>> filterBuilderConsumer)` 方法
>
> 即实现 `if uids not null { where uids in (:uids) }`

```java
List<Long> uids = Lists.newArrayList(2L, 3L, 4L);

ordersList = ordersRepository.findMany(f -> f
        .eq(Fields.status, Status.Finish)
        .condition(uids != null && !uids.isEmpty(), x -> x.in(Fields.uid, uids))
);
```

> 指定具体查询字段 `projection`

```java
// 指定查询字段 name、uid, 排除字段 _id
// "projection" : {
//   "name" : 1,
//   "uid" : 1,
//   "_id" : 0
// },
ordersList = ordersRepository.findMany(
        f -> f.eq(Fields.status, Status.Finish),
        p -> p.include(Fields.name, Fields.uid).exclude(Fields.id)
);
```

- `count` 根据条件返回数据数量 `long`。

```java
// 根据条件查询数量
long count = ordersRepository.count(
        f -> f.eq(Fields.status, Status.Normal)
);

// 构建 CountOptions 参数
CountOptions countOptions = CountOptions.Builder.create(Orders.class)
        .filter(f -> f
                .eq(Fields.status, Status.Normal)
        )
        .build();
count = ordersRepository.count(countOptions);
System.out.println("count: " + count); // count: 144

// 跳过前5条数据
count = ordersRepository.count(countOptions.skip(5));
System.out.println("count: " + count); // count: 139


```

- `exists` 根据条件返回是否存在 `boolean`, 用`exists()`代替 `count() == 0`效率更高。

```java
// 
boolean exists = orderRepository.exists(
    f -> f.eq(Fields.status, Status.Finish)
);
```

### 5. 修改 | updateOne & updateMany

```java
List<Orders> ordersList = ordersRepository.findAll();
Orders orders = ordersList.get(0);

List<Integer> refs = Lists.newArrayList(888, 999);
Date now = new Date();

// {
//   "filter" : {
//     "_id" : 1
//   },
//   "update" : {
//     "$set" : {
//       "name" : 123,
//       "status" : 4,
//       "isPay" : false,
//       "createTime" : {
//         "$date" : "2025-02-25T09:09:17.758Z"
//       },
//       "refs" : [ 888, 999 ]
//     },
//     "$inc" : {
//       "version" : 1
//     }
//   }
// }
long count = ordersRepository.updateOne(
            orders.getId(),
            // UpdateBuilder<Orders>
            u -> u.set(Fields.name, 123)
                    .set(Fields.status, Status.Fail)
                    .set(Fields.isPay, false)
                    .set(Fields.createTime, now)
                    .inc(Fields.version, 1L)
                    .condition(!refs.isEmpty(), x -> x.set(Fields.refs, refs))
        );

// {
//   "filter" : {
//     "isPay" : false
//   },
//   "update" : {
//     "$set" : {
//       "status" : -1,
//   }
// }
count = ordersRepository.updateMany(
            f -> f.eq(Fields.isPay, false),
            u -> u.set(Fields.status, Status.Delete)
        );

```

### 6. 新增 | insert & insertMany

> 插入时，对象主键为 `null` 的会自动赋值。批量插入时自增id会一次性从数据库获取，没有性能问题。

```java

// 单条数据插入
User user = new User();
user.setName("raven");
user.setAge(123);

System.out.println("user.id:  " + user.getId()); // user.id:  null
repos.insert(user);
System.out.println("user.id:  " + user.getId()); // user.id:  91


// 批量数据插入
List<User> users = new ArrayList<>();
int seed = 50;
for (int i = 1; i <= seed; i++) {
    User user = new User();
    user.setName("raven" + i);
    user.setAge(i * 10);
    users.add(user);
}
userRepository.insertMany(users);

```

> 可以自定义生成id，实现 `IdGenerator<TKey>` 接口。

```java
public interface IdGenerator<TKey> {

    TKey generateId();

    List<TKey> generateIdBatch(long count);

    String name();
}

```