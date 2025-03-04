package com.accrualbatch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.batch")
@Component
public class BatchProperties {
    private final Jdbc jdbc = new Jdbc();

    public Jdbc getJdbc() {
        return jdbc;
    }

    public static class Jdbc {
        private String initializeSchema;
        private boolean enabled;

        public String getInitializeSchema() {
            return initializeSchema;
        }

        public void setInitializeSchema(String initializeSchema) {
            this.initializeSchema = initializeSchema;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}

