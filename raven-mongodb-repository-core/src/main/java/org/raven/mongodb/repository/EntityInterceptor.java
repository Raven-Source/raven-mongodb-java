//package org.raven.mongodb.repository;
//
///**
// * @author by yi.liang
// * date 2021/9/20 17:23
// */
//
//import com.mongodb.DBObject;
//
///**
// *
// */
//public interface EntityInterceptor {
//
//   default void postLoad(Object entity, DBObject dbObj, Mapper mapper);
//
//    void postPersist(Object entity, DBObject dbObj, Mapper mapper);
//
//    void preLoad(Object entity, DBObject dbObj, Mapper mapper);
//
//    void prePersist(Object entity, DBObject dbObj, Mapper mapper);
//
//    void preSave(Object entity, DBObject dbObj, Mapper mapper);
//}
