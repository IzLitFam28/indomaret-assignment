package com.indomaret.assignment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.rules")
@Getter
@Setter
public class AppRulesProperties {
    private Search search = new Search();
    private Branch branch = new Branch();

    @Getter
    @Setter
    public static class Search {
        private boolean includeWhitelistByDefault = true;
    }

    @Getter
    @Setter
    public static class Branch {
        private boolean cascadeSoftDeleteToStores = true;
    }
}