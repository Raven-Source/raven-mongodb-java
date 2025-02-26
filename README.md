[![Security Status](https://www.murphysec.com/platform3/v3/badge/1609167474302918656.svg)](https://www.murphysec.com/accept?code=525262e8fa030ee439a8e27f10097af7&type=1&from=2&t=2)

## 目录
- [核心特性](#features)
- [快速开始](#quick start)

[//]: # (- [实体定义规范]&#40;#实体定义规范&#41;)
- **数据操作**
    - [查询操作](./docs/find.md)
    - [插入操作](./docs/insert.md)
    - [更新操作](./docs/update.md)
    - [删除操作](./docs/delete.md)
    - [原子操作](./docs/findAndModify.md)
- [异步接口](./docs/reactive.md)
- [高级功能](./docs/advanced.md)

## 核心特性 | features <a id="features"></a>

#### 基于 `mongodb-driver` 的一些封装和增强，提供 `orm` 常用的数据库操作方法：

#### 1. 默认支持基于数据库实现自增id。
#### 2. 批量插入时，自动批量填充id。
#### 3. 避免不同 `entity` 版本的 `save` 方法导致数据覆盖问题等。
#### 4. 枚举对应的数值类型和字符串类型的支持。
#### 5. 提供 `EntityInterceptor` 接口，针对 `find`、`insert`、`update`、`delete` 等操作的预处理能力
#### 6. 提供优雅的链式写法。
#### 7. 提供同步操作和异步（reactive）操作方法。

#### 目的是简化写法、提高开发效率，以及避免一些问题。


## 快速开始 | quick start <a id="quick start"></a>

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

### 4. 开始查询

```java
import org.raven.mongodb.test.model.Orders;
import org.raven.mongodb.test.model.Orders.Fields;
import org.raven.mongodb.test.model.Status;

Orders orders;
// 根据id查询
orders = ordersRepository.findOne(1L);

// 根据条件查询
orders = ordersRepository.findOne(
        // FilterBuilder<Orders>
        f -> f
            .eq(Fields.status, Status.Normal)
            .gt(Fields.price, 9.0)
);


```