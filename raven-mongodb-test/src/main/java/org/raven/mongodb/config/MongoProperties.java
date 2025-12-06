    package org.raven.mongodb.config;

    import lombok.Getter;
    import lombok.Setter;
    import lombok.experimental.Accessors;
    import org.raven.mongodb.MongoOptions;

    @Getter
    @Setter
    @Accessors(chain = true)
    public class MongoProperties implements MongoOptions {

        /**
         * 数据库连接节点
         */
        private String connString;

        /**
         * 数据库名称
         */
        private String dbName;

    }
