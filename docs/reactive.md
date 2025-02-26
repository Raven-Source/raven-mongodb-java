
## 异步接口跟同步接口参数一样，但是返回值是 `Mono` 和 `Flux`


> 示例

```java

public void findTest() throws Exception {

    Semaphore semaphore = new Semaphore(0);

    ReactiveMongoRepository<User, Long> repos = new UserReactiveRepositoryImpl();

    repos.findOne(Filters.gte("_id", 1))
            .subscribe(
                    user -> {
                        Assert.assertTrue(!user.isPresent());
                        System.out.println("user.name: " + user.get().getName());
                        semaphore.release();
                    }
            );

    semaphore.acquire(1);

    repos.findMany(Filters.empty(), null, Sorts.descending("_id"), 1, 0)
            .subscribe(list -> {

                Assert.assertNotNull(list.get(0));
                Assert.assertEquals(list.size(), 1);
                Assert.assertEquals(list.get(0).getId().longValue(), 10);
                System.out.println("list.size: " + list.size());

                semaphore.release();
            });

    semaphore.acquire(1);
}

```