
# 查询 | findOne & findMany & count & exists

> 以下为 `OrderRepository`，包括不同条件、参数形式写法示例：

- `findOne` 根据主键或条件查询单条数据

```java
import org.raven.mongodb.test.model.Orders;
import org.raven.mongodb.test.model.Orders.Fields;
import org.raven.mongodb.test.model.Status;

Orders orders;
// 根据id查询
orders = ordersRepository.findOne(1L);

// {
//     "status": 1,
//     "price": {
//         "$gt": 79.0
//     }
// }
// 根据条件查询
orders = ordersRepository.findOne(
        // FilterBuilder<Orders>
        f -> f
            .eq(Fields.status, Status.Normal)
            .gt(Fields.price, 9.0)
);
```

- `findMany` 根据条件查询列表数据，也可 `findAll` 查询全部列表数据

```java

// 查询全部列表数据
List<Order> orderList = ordersRepository.findAll();

// {
//     "itemsId": {
//         "$ne": null
//     }
//     "status": 1
// }
// 根据条件查询列表
ordersList = ordersRepository.findMany(
        // FilterBuilder<Orders>
        f -> f
            .isNotNull(Fields.itemsId)
            .eq(Fields.status, Status.Normal)
        );

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
// 通过FindOptions.Builder创建条件
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

// "projection" : {
//   "name" : 1,
//   "uid" : 1,
//   "_id" : 0
// }
// 指定查询字段 name、uid, 排除字段 _id
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
