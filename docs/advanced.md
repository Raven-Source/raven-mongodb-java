## 实体监听器 | EntityInterceptor

> 通过实现 `EntityInterceptor` 接口，可在数据库操作的关键生命周期阶段注入自定义逻辑。
> 接口定义

```java
public interface EntityInterceptor {

    default void preFind(final BaseFindOptions<?> options,
                         final EntityInformation<?, ?> entityInformation) {
    }

    default void preInsert(Object entity,
                           final EntityInformation<?, ?> entityInformation) {
    }

    default void preUpdate(final BaseUpdateOptions<?> options,
                           final EntityInformation<?, ?> entityInformation) {
    }

    default void preDelete(final BaseModifyOptions<?> options,
                           final EntityInformation<?, ?> entityInformation) {
    }
}
```

## 使用场景
#### 自动过滤逻辑删除数据
#### 审计日志记录
#### 数据版本控制
#### 租户逻辑隔离
#### 动态修改查询/更新条件

> 租户查询条件示例，拦截器实现（注：这里只是一个示例，实际开发中需要根据业务场景进行定制）

```java
// 定义拦截器
public class TenantInterceptor implements EntityInterceptor {
    
    @Override
    public void preFind(BaseFindOptions<?> options,
                        EntityInformation<?, ?> entityInformation) {

        Long tenantId = TenantUtils.getTenantId();
        if (tenantId != null) {
            Bson filter = Filters.eq("tenantId", tenantId);
            options.filter(BsonUtils.combine(options.filter(), filter));
        }
    }
}

// 实体定义, 添加 EntityListeners 注解
@EntityListeners({DeletableInterceptor.class, VersionedEntityInterceptor.class})
public class User implements AutoIncr<Long>, Tenant {
    
    @BsonId()
    private Long id;

    private String name;
    
    private Long tenantId;
}

```

## 自定义Id生成实现

> 可以自定义 `IdGeneratorProvider` 和 `IdGenerator` 实现类，在 `Repository` 构成函数注入

```java
public interface IdGenerator<TKey> {

    TKey generateId();

    List<TKey> generateIdBatch(long quantity);

    String name();
}


public interface IdGeneratorProvider<T, TMongoDatabase> {

    <TEntity, TKey> T build(String collectionName, Sequence sequence, Class<TEntity> entityClazz, Class<TKey> keyClazz, Supplier<TMongoDatabase> databaseSupplier);

}

```