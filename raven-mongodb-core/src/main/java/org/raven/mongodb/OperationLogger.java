package org.raven.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.raven.commons.util.PropertiesUtils;
import org.raven.commons.util.StringUtils;
import org.raven.mongodb.criteria.CommandOptions;

@Slf4j
public class OperationLogger {

    private static final String PROPERTIES_FILE = "META-INF/mongodb.properties";

    private boolean logEnabled;

    public OperationLogger() {
        String value = PropertiesUtils.getString(PROPERTIES_FILE, AvailableSettings.SHOW_OPERATION);
        if (StringUtils.isNotBlank(value)) {
            try {
                logEnabled = Boolean.parseBoolean(value);
            } catch (Exception ignored){
            }
        }
    }

    public OperationLogger(final boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    public void log(final String operation, final CommandOptions options) {
        if (log.isDebugEnabled() || logEnabled) {
            log.info("{}: {}", operation, options);
        }
    }

}
