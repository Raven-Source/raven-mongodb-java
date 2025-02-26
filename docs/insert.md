
# 新增 | insert & insertMany

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