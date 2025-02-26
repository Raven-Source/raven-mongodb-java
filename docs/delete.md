
## 修改 | deleteOne & deleteMany

```java
List<Orders> ordersList = ordersRepository.findAll();
Orders orders = ordersList.get(0);

// {
//   "filter" : {
//     "_id" : 1
//   }
// }
// 根据主键id删除一条数据
long count = ordersRepository.deleteOne(
            orders.getId()
);

// {
//   "filter" : {
//     "isPay" : false
//   }
// }
// 根据条件删除所有相关数据
count = ordersRepository.deleteMany(
            f -> f.eq(Fields.isPay, false)
        );

```