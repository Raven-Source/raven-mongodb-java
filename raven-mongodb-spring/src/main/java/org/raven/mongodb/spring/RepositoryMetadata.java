package org.raven.mongodb.spring;


public interface RepositoryMetadata {

    Class<?> getIdType();

    Class<?> getEntityType();

    Class<?> getRepositoryInterface();
}
