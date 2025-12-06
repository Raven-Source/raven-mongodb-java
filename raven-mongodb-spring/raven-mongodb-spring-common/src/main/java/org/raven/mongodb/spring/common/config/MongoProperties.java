package org.raven.mongodb.spring.common.config;

import lombok.Data;

/**
 * MongoDB configuration properties.
 * <p>
 * This class can be used with @ConfigurationProperties to bind properties from configuration files.
 * Supports both single and multiple datasource configurations.
 * </p>
 *
 * <h2>Single Datasource Example:</h2>
 * <pre>
 * mongodb.options.connString=mongodb://127.0.0.1:27017/
 * mongodb.options.dbName=TestDB
 * </pre>
 *
 * <h2>Multiple Datasource Example:</h2>
 * <pre>
 * mongodb.options.user.connString=mongodb://127.0.0.1:27017/?maxPoolSize=100
 * mongodb.options.user.dbName=userDbTest
 *
 * mongodb.options.admin.connString=mongodb://127.0.0.1:27017/?maxPoolSize=50
 * mongodb.options.admin.dbName=adminDbTest
 * </pre>
 *
 * @author yi.liang
 */
@Data
public class MongoProperties {

    /**
     * MongoDB connection string.
     * Example: mongodb://127.0.0.1:27017/ or mongodb://user:password@host:port/?maxPoolSize=100
     */
    private String connString = "mongodb://127.0.0.1:27017/";

    /**
     * Database name to use.
     */
    private String dbName;

    /**
     * Write concern level.
     * Supported values: ACKNOWLEDGED, W1, W2, W3, UNACKNOWLEDGED, JOURNALED, MAJORITY
     * Default: ACKNOWLEDGED
     */
    private String writeConcern = "ACKNOWLEDGED";

    /**
     * Read preference.
     * Supported values: PRIMARY, PRIMARY_PREFERRED, SECONDARY, SECONDARY_PREFERRED, NEAREST
     * Default: PRIMARY
     */
    private String readPreference = "PRIMARY";
}
