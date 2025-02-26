## 原子操作 | findOneAndUpdate & findOneAndDelete

```java

// {
//   "filter" : {
//     "uid" : 1,
//     "isPay" : false,
//   },
//   "update" : {
//     "$inc" : {
//       "version" : 123
//     },
//     "$set" : {
//       "isPay" : true,
//     }
//   }
// }
// 根据uid和isPay查询订单，并更新订单的version字段，成功返回订单对象，失败返回null
Orders orders = ordersRepository.findOneAndUpdate(
        f -> f.eq(Fields.uid, uid).eq(Fields.isPay, false),
        u -> u.inc(Fields.version, 123).set(Fields.isPay, true)
);

// 根据id删除订单，成功返回订单对象，失败返回null
orders = ordersRepository.findOneAndDelete(orders.getId());

// 根据uid并按照时间倒叙删除一条订单，成功返回订单对象，失败返回null
orders = ordersRepository.findOneAndDelete(
            f -> f.eq(Fields.uid, uid),
            s -> s.desc(Fields.createTime)
        );

```