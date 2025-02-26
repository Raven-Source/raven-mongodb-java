
## 修改 | updateOne & updateMany

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
// 根据主键id更新一条相关数据
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


// 根据主键id更新一条相关数据
orders.setName(456);
long count = ordersRepository.updateOne(
        // Entity
        orders
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
// 根据条件更新所有相关数据
count = ordersRepository.updateMany(
            f -> f.eq(Fields.isPay, false),
            u -> u.set(Fields.status, Status.Delete)
        );

```