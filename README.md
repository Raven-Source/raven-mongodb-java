[![Security Status](https://www.murphysec.com/platform3/v3/badge/1609167474302918656.svg)](https://www.murphysec.com/accept?code=525262e8fa030ee439a8e27f10097af7&type=1&from=2&t=2)

# 简介 | intro

#### 基于 `mongodb-driver` 的一些封装和增强，提供 `orm` 常用的数据库操作方法：

#### 1. 基于数据库的单条 & 批量插入自增id。
#### 2. 避免不同 `entity` 版本的 `save` 方法导致数据覆盖问题等。
#### 3. 枚举对应的数值类型和字符串类型的支持。
#### 4. 提供 `EntityInterceptor` 接口，针对 `find`、`insert`、`update`、`delete` 等操作的预处理能力
#### 5. 提供优雅的链式写法。
#### 6. 提供同步操作和异步（reactive）操作接口。

#### 目的是简化写法、提高开发效率，以及避免一些问题。


# 快速开始 | quick start

#### 1. 添加依赖：

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

#### 2. 配置文件：

```yaml
mongodb.options.connString=mongodb://127.0.0.1:27017/?
mongodb.options.dbName=TestDB
```

#### 3. 例如 `SpringBoot` 项目可以自定义个 `Configuration` 、实体对象、以及对应的 `UserRepository` ：

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

#### 4. 开始查询、更新：

```java
Orders orders;
// 根据id查询
orders = ordersRepository.findOne(1L);

// 根据条件查询
orders = ordersRepository.findOne(
        f -> f
            .eq(Orders.Fields.status, Status.Normal)
            .gt(Orders.Fields.price, 1.0)
);

// 根据条件查询列表
List<Orders> ordersList = ordersRepository.findList(
        FindOptions.Builder.create(Orders.class)
                .filter(f -> f
                        .eq(Orders.Fields.status, Status.Normal)
                        .gt(Orders.Fields.price, 1.0)
                )
                .sort(s -> s
                        .asc(Orders.Fields.itemsId)
                )
                .projection(p -> p
                        .include(Orders.Fields.name, Orders.Fields.itemsId)
                        .excludeId()
                )
                .skip(3)
                .limit(10)
                .build()
);


// 根据条件更新
Long itemsId = orders.getItemsId();
ordersRepository.updateOne(
        f -> f
            .eq(Orders.Fields.itemsId, itemsId)
            .ne(Orders.Fields.isPay, false)
        ,
        u -> u
            .set(Orders.Fields.status, Status.Delete)
);

```