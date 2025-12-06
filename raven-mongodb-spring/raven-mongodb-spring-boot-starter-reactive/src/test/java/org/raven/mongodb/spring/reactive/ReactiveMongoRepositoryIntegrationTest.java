package org.raven.mongodb.spring.reactive;

import org.junit.jupiter.api.*;
import org.raven.mongodb.contants.BsonConstant;
import org.raven.mongodb.criteria.FindOptions;
import org.raven.mongodb.spring.reactive.config.TestReactiveMongoConfig;
import org.raven.mongodb.spring.reactive.entity.TestUser;
import org.raven.mongodb.spring.reactive.repository.TestUserQueryRepository;
import org.raven.mongodb.spring.reactive.repository.TestUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for reactive MongoDB repository Spring integration.
 *
 * @author yi.liang
 * @since 3.1.0
 */
@SpringBootTest
@ContextConfiguration(classes = TestReactiveMongoConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReactiveMongoRepositoryIntegrationTest {

    @Autowired
    private TestUserRepository userRepository;

    @Autowired
    private TestUserQueryRepository userQueryRepository;

    @BeforeEach
    void setUp() {
        assertNotNull(userRepository, "UserRepository should be autowired");
        assertNotNull(userQueryRepository, "UserQueryRepository should be autowired");
        tearDown();
    }

    void tearDown() {
        // Clean up test data reactively
        try {
            Mono.from(userRepository.getCollection().drop()).block();
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test repository bean creation and injection")
    void testRepositoryBeanCreation() {
        assertNotNull(userRepository);
        assertNotNull(userQueryRepository);

        // Verify collection name
        String collectionName = userRepository.getCollectionName();
        assertNotNull(collectionName);
        assertTrue(collectionName.contains("TestUser") || collectionName.equals("test_user"));
    }

    @Test
    @Order(2)
    @DisplayName("Test insert single entity reactively")
    void testInsertOne() {
        TestUser user = new TestUser("Alice", "alice@example.com", 25);

        Mono<Optional<Long>> idMono = userRepository.insert(user);

        StepVerifier.create(idMono)
            .assertNext(optionalId -> {
                assertTrue(optionalId.isPresent());
                Long id = optionalId.get();
                assertTrue(id > 0);
                assertEquals(id, user.getId());
            })
            .verifyComplete();
    }

    @Test
    @Order(3)
    @DisplayName("Test insert multiple entities reactively")
    void testInsertBatch() {
        List<TestUser> users = Arrays.asList(
            new TestUser("Bob", "bob@example.com", 30),
            new TestUser("Charlie", "charlie@example.com", 35),
            new TestUser("Diana", "diana@example.com", 28)
        );

        Mono<Map<Integer, Long>> idsMono = userRepository.insertMany(users);

        StepVerifier.create(idsMono)
            .assertNext(ids -> {
                assertNotNull(ids);
                assertEquals(3, ids.size());

                // Verify all users have IDs assigned
                for (TestUser user : users) {
                    assertNotNull(user.getId());
                    assertTrue(user.getId() > 0);
                }
            })
            .verifyComplete();
    }

    @Test
    @Order(4)
    @DisplayName("Test find by ID reactively")
    void testFindById() {
        // Insert test data
        TestUser user = new TestUser("Eve", "eve@example.com", 27);

        Mono<TestUser> result = userRepository.insert(user)
            .map(Optional::get)
            .flatMap(id -> userRepository.findOne(id))
            .map(Optional::get);

        StepVerifier.create(result)
            .assertNext(found -> {
                assertNotNull(found);
                assertEquals("Eve", found.getName());
                assertEquals("eve@example.com", found.getEmail());
                assertEquals(27, found.getAge());
            })
            .verifyComplete();
    }

    @Test
    @Order(5)
    @DisplayName("Test find all reactively")
    void testFindAll() {
        // Insert test data
        List<TestUser> users = Arrays.asList(
            new TestUser("Frank", "frank@example.com", 32),
            new TestUser("Grace", "grace@example.com", 29)
        );

        Mono<List<TestUser>> result = userRepository.insertMany(users)
            .then(userRepository.findMany(FindOptions.create()));

        StepVerifier.create(result)
            .assertNext(allUsers -> {
                assertNotNull(allUsers);
                assertTrue(allUsers.size() >= 2);
            })
            .verifyComplete();
    }

    @Test
    @Order(6)
    @DisplayName("Test find with filter reactively")
    void testFindWithFilter() {
        // Insert test data
        TestUser user1 = new TestUser("Henry", "henry@example.com", 40);
        TestUser user2 = new TestUser("Ivy", "ivy@example.com", 22);

        Mono<List<TestUser>> result = userRepository.insertMany(Arrays.asList(user1, user2))
            .then(userRepository.findMany(filter -> filter.gt(TestUser.Fields.age, 30)));

        StepVerifier.create(result)
            .assertNext(users -> {
                assertNotNull(users);
                assertTrue(users.size() >= 1);
                assertTrue(users.stream().allMatch(u -> u.getAge() > 30));
            })
            .verifyComplete();
    }

    @Test
    @Order(7)
    @DisplayName("Test update entity reactively")
    void testUpdate() {
        // Insert test data
        TestUser user = new TestUser("Jack", "jack@example.com", 33);

        Mono<TestUser> result = userRepository.insert(user)
            .map(Optional::get)
            .flatMap(id -> {
                user.setAge(34);
                user.setEmail("jack.updated@example.com");

                return userRepository.updateOne(
                    filter -> filter.eq(BsonConstant.PRIMARY_KEY_NAME, id),
                    user
                ).then(userRepository.findOne(id).map(Optional::get));
            });

        StepVerifier.create(result)
            .assertNext(updated -> {
                assertNotNull(updated);
                assertEquals(34, updated.getAge());
                assertEquals("jack.updated@example.com", updated.getEmail());
            })
            .verifyComplete();
    }

    @Test
    @Order(8)
    @DisplayName("Test delete entity reactively")
    void testDelete() {
        // Insert test data
        TestUser user = new TestUser("Kate", "kate@example.com", 26);

        Mono<Optional<TestUser>> result = userRepository.insert(user)
            .map(Optional::get)
            .flatMap(id -> userRepository.deleteOne(id)
                .then(userRepository.findOne(id)));

        StepVerifier.create(result)
            .assertNext(deleted -> assertFalse(deleted.isPresent()))
            .verifyComplete();
    }

    @Test
    @Order(9)
    @DisplayName("Test count reactively")
    void testCount() {
        // Insert test data
        List<TestUser> users = Arrays.asList(
            new TestUser("Leo", "leo@example.com", 31),
            new TestUser("Mia", "mia@example.com", 24),
            new TestUser("Noah", "noah@example.com", 36)
        );

        Mono<Long> countAll = userRepository.insertMany(users)
            .then(userRepository.count());

        StepVerifier.create(countAll)
            .assertNext(count -> {
                assertNotNull(count);
                assertTrue(count >= 3);
            })
            .verifyComplete();

        // Count with filter
        Mono<Long> countOver30 = userRepository.count(
            filter -> filter.gt(TestUser.Fields.age, 30)
        );

        StepVerifier.create(countOver30)
            .assertNext(count -> {
                assertNotNull(count);
                assertTrue(count >= 2);
            })
            .verifyComplete();
    }

    @Test
    @Order(10)
    @DisplayName("Test query repository (read-only) reactively")
    void testQueryRepository() {
        // Insert test data using full repository
        TestUser user = new TestUser("Olivia", "olivia@example.com", 29);

        Mono<TestUser> result = userRepository.insert(user)
            .map(Optional::get)
            .flatMap(id -> userQueryRepository.findOne(id).map(Optional::get));

        StepVerifier.create(result)
            .assertNext(found -> {
                assertNotNull(found);
                assertEquals("Olivia", found.getName());
            })
            .verifyComplete();

        // Verify count works
        Mono<Long> count = userQueryRepository.count();

        StepVerifier.create(count)
            .assertNext(c -> assertTrue(c >= 1))
            .verifyComplete();
    }

    @Test
    @Order(11)
    @DisplayName("Test exists reactively")
    void testExists() {
        // Insert test data
        TestUser user = new TestUser("Paul", "paul@example.com", 38);

        Mono<Boolean> existsResult = userRepository.insert(user)
            .map(Optional::get)
            .flatMap(id -> userRepository.exists(
                filter -> filter.eq(BsonConstant.PRIMARY_KEY_NAME, id)
            ));

        StepVerifier.create(existsResult)
            .assertNext(exists -> assertTrue(exists))
            .verifyComplete();

        // Test not exists
        Mono<Boolean> notExistsResult = userRepository.exists(
            filter -> filter.eq(BsonConstant.PRIMARY_KEY_NAME, 999999L)
        );

        StepVerifier.create(notExistsResult)
            .assertNext(exists -> assertFalse(exists))
            .verifyComplete();
    }

    @Test
    @Order(12)
    @DisplayName("Test reactive stream with multiple operations")
    void testReactiveStream() {
        // Create a reactive pipeline
        Flux<TestUser> pipeline = Flux.just(
                new TestUser("Quinn", "quinn@example.com", 27),
                new TestUser("Rachel", "rachel@example.com", 32),
                new TestUser("Sam", "sam@example.com", 29)
            )
            .collectList()
            .flatMap(users -> userRepository.insertMany(users))
            .thenMany(userRepository.findMany(filter -> filter.gte(TestUser.Fields.age, 27)))
            .flatMapIterable(list -> list);

        StepVerifier.create(pipeline)
            .expectNextCount(3)
            .verifyComplete();
    }
}
