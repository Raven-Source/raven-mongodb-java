package org.raven.mongodb.repository;

import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.repository.interceptors.DeletableInterceptor;
import org.raven.mongodb.repository.interceptors.EntityInterceptor;
import org.raven.mongodb.repository.interceptors.VersionedEntityInterceptor;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author by yanfeng
 * date 2021/9/23 21:05
 */
public class EntityInformationTest {


    MongoRepository<User, Long> userRepository = new UserRepositoryImpl();
    MongoRepository<UserExtend, Long> userExtendRepository = new UserExtendRepositoryImpl();

    @Test
    public void test() throws Exception {


        Field field = AbstractMongoRepository.class.getDeclaredField("entityInformation");
        field.setAccessible(true);

        EntityInformation<User, Long> entityInformation = (EntityInformation<User, Long>) field.get(userRepository);

        Assert.assertEquals(entityInformation.getInterceptors().size(), 2);
        Set<Class> set = Set.of(DeletableInterceptor.class, VersionedEntityInterceptor.class);
        for (EntityInterceptor interceptor : entityInformation.getInterceptors()) {
            Assert.assertEquals(set.contains(interceptor.getClass()), true);
        }


        field = AbstractMongoRepository.class.getDeclaredField("entityInformation");
        field.setAccessible(true);

        EntityInformation<UserExtend, Long> userExtendEntityInformation = (EntityInformation<UserExtend, Long>) field.get(userExtendRepository);

        Assert.assertEquals(userExtendEntityInformation.getInterceptors().size(), 3);
        set = Set.of(UserExtend.TestInterceptor.class, DeletableInterceptor.class, VersionedEntityInterceptor.class);
        for (EntityInterceptor interceptor : entityInformation.getInterceptors()) {
            Assert.assertEquals(set.contains(interceptor.getClass()), true);
        }


    }

}
