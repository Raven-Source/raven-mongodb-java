package org.raven.mongodb.repository.spring;


public interface RepositoryMetadata {

    Class<?> getIdType();

    Class<?> getEntityType();

    Class<?> getRepositoryInterface();
}
