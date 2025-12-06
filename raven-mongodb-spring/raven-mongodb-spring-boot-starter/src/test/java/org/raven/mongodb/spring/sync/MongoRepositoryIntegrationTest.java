package org.raven.mongodb.spring.sync;

import org.junit.jupiter.api.*;
import org.raven.mongodb.contants.BsonConstant;
import org.raven.mongodb.criteria.FindOptions;
import org.raven.mongodb.spring.sync.config.TestMongoConfig;
import org.raven.mongodb.spring.sync.entity.TestUser;
import org.raven.mongodb.spring.sync.repository.TestUserQueryRepository;
import org.raven.mongodb.spring.sync.repository.TestUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for sync MongoDB repository Spring integration.
 *
 * @author yi.liang
 * @since 3.1.0
 */
@SpringBootTest
@ContextConfiguration(classes = TestMongoConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MongoRepositoryIntegrationTest {

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
        // Clean up test data
        try {
            userRepository.getCollection().drop();
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
    @DisplayName("Test insert single entity")
    void testInsertOne() {
        TestUser user = new TestUser("Alice", "alice@example.com", 25);

        Long id = userRepository.insert(user);

        assertNotNull(id);
        assertTrue(id > 0);
        assertEquals(id, user.getId());
    }

    @Test
    @Order(3)
    @DisplayName("Test insert multiple entities")
    void testInsertBatch() {
        List<TestUser> users = Arrays.asList(
                new TestUser("Bob", "bob@example.com", 30),
                new TestUser("Charlie", "charlie@example.com", 35),
                new TestUser("Diana", "diana@example.com", 28)
        );

        Map<Integer, Long> ids = userRepository.insertMany(users);

        assertNotNull(ids);
        assertEquals(3, ids.size());

        // Verify all users have IDs assigned
        for (TestUser user : users) {
            assertNotNull(user.getId());
            assertTrue(user.getId() > 0);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test find by ID")
    void testFindById() {
        // Insert test data
        TestUser user = new TestUser("Eve", "eve@example.com", 27);
        Long id = userRepository.insert(user);

        // Find by ID
        TestUser found = userRepository.findOne(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals("Eve", found.getName());
        assertEquals("eve@example.com", found.getEmail());
        assertEquals(27, found.getAge());
    }

    @Test
    @Order(5)
    @DisplayName("Test find all")
    void testFindAll() {
        // Insert test data
        List<TestUser> users = Arrays.asList(
                new TestUser("Frank", "frank@example.com", 32),
                new TestUser("Grace", "grace@example.com", 29)
        );
        userRepository.insertMany(users);

        // Find all
        List<TestUser> allUsers = userRepository.findMany(FindOptions.create());

        assertNotNull(allUsers);
        assertTrue(allUsers.size() >= 2);
    }

    @Test
    @Order(6)
    @DisplayName("Test find with filter")
    void testFindWithFilter() {
        // Insert test data
        TestUser user1 = new TestUser("Henry", "henry@example.com", 40);
        TestUser user2 = new TestUser("Ivy", "ivy@example.com", 22);
        userRepository.insertMany(Arrays.asList(user1, user2));

        // Find users with age > 30
        List<TestUser> users = userRepository.findMany(
                filter -> filter.gt(TestUser.Fields.age, 30)
        );

        assertNotNull(users);
        assertTrue(users.size() >= 1);
        assertTrue(users.stream().allMatch(u -> u.getAge() > 30));
    }

    @Test
    @Order(7)
    @DisplayName("Test update entity")
    void testUpdate() {
        // Insert test data
        TestUser user = new TestUser("Jack", "jack@example.com", 33);
        Long id = userRepository.insert(user);

        // Update user
        user.setAge(34);
        user.setEmail("jack.updated@example.com");

        userRepository.createUpdateBson(user, false);

        Long updateCount = userRepository.updateOne(
                filter -> filter.eq(BsonConstant.PRIMARY_KEY_NAME, id),
                user
        );

        assertEquals(1L, updateCount);

        // Verify update
        TestUser updated = userRepository.findOne(id);
        assertNotNull(updated);
        assertEquals(34, updated.getAge());
        assertEquals("jack.updated@example.com", updated.getEmail());
    }

    @Test
    @Order(8)
    @DisplayName("Test delete entity")
    void testDelete() {
        // Insert test data
        TestUser user = new TestUser("Kate", "kate@example.com", 26);
        Long id = userRepository.insert(user);

        // Delete user
        Long deleteCount = userRepository.deleteOne(id);

        assertEquals(1L, deleteCount);

        // Verify deletion
        TestUser deleted = userRepository.findOne(id);
        assertNull(deleted);
    }

    @Test
    @Order(9)
    @DisplayName("Test count")
    void testCount() {
        // Insert test data
        List<TestUser> users = Arrays.asList(
                new TestUser("Leo", "leo@example.com", 31),
                new TestUser("Mia", "mia@example.com", 24),
                new TestUser("Noah", "noah@example.com", 36)
        );
        userRepository.insertMany(users);

        // Count all
        Long count = userRepository.count();

        assertNotNull(count);
        assertTrue(count >= 3);

        // Count with filter
        Long countOver30 = userRepository.count(
                filter -> filter.gt(TestUser.Fields.age, 30)
        );

        assertNotNull(countOver30);
        assertTrue(countOver30 >= 2);
    }

    @Test
    @Order(10)
    @DisplayName("Test query repository (read-only)")
    void testQueryRepository() {
        // Insert test data using full repository
        TestUser user = new TestUser("Olivia", "olivia@example.com", 29);
        Long id = userRepository.insert(user);

        // Use query repository for read operations
        TestUser found = userQueryRepository.findOne(id);

        assertNotNull(found);
        assertEquals("Olivia", found.getName());

        // Verify count works
        Long count = userQueryRepository.count();
        assertTrue(count >= 1);
    }

    @Test
    @Order(11)
    @DisplayName("Test exists")
    void testExists() {
        // Insert test data
        TestUser user = new TestUser("Paul", "paul@example.com", 38);
        Long id = userRepository.insert(user);

        // Test exists
        Boolean exists = userRepository.exists(
                filter -> filter.eq(BsonConstant.PRIMARY_KEY_NAME, id)
        );

        assertTrue(exists);

        // Test not exists
        Boolean notExists = userRepository.exists(
                filter -> filter.eq(BsonConstant.PRIMARY_KEY_NAME, 999999L)
        );

        assertFalse(notExists);
    }
}
