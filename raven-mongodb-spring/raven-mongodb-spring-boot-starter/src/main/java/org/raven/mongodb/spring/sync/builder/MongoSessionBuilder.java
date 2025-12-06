package org.raven.mongodb.spring.sync.builder;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import lombok.extern.slf4j.Slf4j;
import org.raven.mongodb.DefaultMongoSession;
import org.raven.mongodb.MongoSession;
import org.raven.mongodb.spring.common.config.MongoProperties;

/**
 * Builder for creating MongoSession instances from properties.
 * <p>
 * This builder provides a fluent API for creating MongoSession instances
 * with proper configuration parsing and validation.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * MongoSession session = MongoSessionBuilder.create()
 *     .properties(properties)
 *     .build();
 * </pre>
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Slf4j
public class MongoSessionBuilder {

    private MongoProperties properties;
    private String connString;
    private String dbName;
    private WriteConcern writeConcern;
    private ReadPreference readPreference;

    private MongoSessionBuilder() {
    }

    /**
     * Creates a new builder instance.
     *
     * @return new builder
     */
    public static MongoSessionBuilder create() {
        return new MongoSessionBuilder();
    }

    /**
     * Sets properties from MongoProperties object.
     *
     * @param properties the properties
     * @return this builder
     */
    public MongoSessionBuilder properties(MongoProperties properties) {
        this.properties = properties;
        return this;
    }

    /**
     * Sets connection string.
     *
     * @param connString the connection string
     * @return this builder
     */
    public MongoSessionBuilder connString(String connString) {
        this.connString = connString;
        return this;
    }

    /**
     * Sets database name.
     *
     * @param dbName the database name
     * @return this builder
     */
    public MongoSessionBuilder dbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    /**
     * Sets write concern.
     *
     * @param writeConcern the write concern
     * @return this builder
     */
    public MongoSessionBuilder writeConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    /**
     * Sets read preference.
     *
     * @param readPreference the read preference
     * @return this builder
     */
    public MongoSessionBuilder readPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
        return this;
    }

    /**
     * Builds the MongoSession instance.
     *
     * @return configured MongoSession
     */
    public MongoSession build() {
        // Use properties if provided
        if (properties != null) {
            if (connString == null) {
                connString = properties.getConnString();
            }
            if (dbName == null) {
                dbName = properties.getDbName();
            }
            if (writeConcern == null) {
                writeConcern = parseWriteConcern(properties.getWriteConcern());
            }
            if (readPreference == null) {
                readPreference = parseReadPreference(properties.getReadPreference());
            }
        }

        // Validate required fields
        if (connString == null || connString.isEmpty()) {
            throw new IllegalArgumentException("Connection string is required");
        }
        if (dbName == null || dbName.isEmpty()) {
            throw new IllegalArgumentException("Database name is required");
        }

        // Set defaults
        if (writeConcern == null) {
            writeConcern = WriteConcern.ACKNOWLEDGED;
        }
        if (readPreference == null) {
            readPreference = ReadPreference.primary();
        }

        log.info("Creating MongoSession with connection: {}, database: {}",
            maskConnectionString(connString), dbName);

        return new DefaultMongoSession(connString, dbName, writeConcern, readPreference);
    }

    /**
     * Parses WriteConcern from string value.
     */
    private WriteConcern parseWriteConcern(String writeConcernStr) {
        if (writeConcernStr == null || writeConcernStr.isEmpty()) {
            return WriteConcern.ACKNOWLEDGED;
        }
        return WriteConcern.valueOf(writeConcernStr);
    }

    /**
     * Parses ReadPreference from string value.
     */
    private ReadPreference parseReadPreference(String readPreferenceStr) {
        if (readPreferenceStr == null || readPreferenceStr.isEmpty()) {
            return ReadPreference.primary();
        }
        return ReadPreference.valueOf(readPreferenceStr);
    }

    /**
     * Masks sensitive information in connection string for logging.
     */
    private String maskConnectionString(String connString) {
        if (connString == null) {
            return "null";
        }
        return connString.replaceAll("://([^:]+):([^@]+)@", "://$1:****@");
    }
}
