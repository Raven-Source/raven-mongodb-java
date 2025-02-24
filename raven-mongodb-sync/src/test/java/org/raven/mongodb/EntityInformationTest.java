package org.raven.mongodb;

import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.interceptors.DeletableInterceptor;
import org.raven.mongodb.interceptors.EntityInterceptor;
import org.raven.mongodb.interceptors.VersionedEntityInterceptor;
import org.raven.mongodb.model.User;
import org.raven.mongodb.model.UserExtend;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author by yanfeng
 * date 2021/9/23 21:05
 */
public class EntityInformationTest {


    MongoRepository<User, Long> userRepository = new UserRepositoryImpl();
    MongoRepository<UserExtend, Long> userExtendRepository = new UserExtendRepositoryImpl();

    @Test
    @SuppressWarnings("unchecked")
    public void test() throws Exception {


        Field field = BaseRepository.class.getDeclaredField("entityInformation");
        field.setAccessible(true);

        EntityInformation<User, Long> entityInformation = (EntityInformation<User, Long>) field.get(userRepository);

        Assert.assertEquals(entityInformation.getInterceptors().size(), 2);
        Set<Class<?>> set = new HashSet<>(Arrays.asList(DeletableInterceptor.class, VersionedEntityInterceptor.class));
        for (EntityInterceptor interceptor : entityInformation.getInterceptors()) {
            Assert.assertTrue(set.contains(interceptor.getClass()));
        }


        field = BaseRepository.class.getDeclaredField("entityInformation");
        field.setAccessible(true);

        EntityInformation<UserExtend, Long> userExtendEntityInformation = (EntityInformation<UserExtend, Long>) field.get(userExtendRepository);

        Assert.assertEquals(userExtendEntityInformation.getInterceptors().size(), 3);
        set = new HashSet<>(Arrays.asList(UserExtend.TestInterceptor.class, DeletableInterceptor.class, VersionedEntityInterceptor.class));
        for (EntityInterceptor interceptor : entityInformation.getInterceptors()) {
            Assert.assertTrue(set.contains(interceptor.getClass()));
        }


    }

}
