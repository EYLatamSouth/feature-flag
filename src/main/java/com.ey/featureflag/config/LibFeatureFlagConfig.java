package com.ey.featureflag.config;

import com.ey.featureflag.annotations.FeatureFlagAspect;
import com.ey.featureflag.services.FeatureFlagService;
import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.LDValue;
import com.launchdarkly.sdk.server.Components;
import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.server.LDConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class LibFeatureFlagConfig {

    @Value("${feature.flag.api.key}")
    private String apiKey;

    @Value("${feature.flag.api.name}")
    private String apiName;

    @Value("${feature.flag.sdk.key}")
    private String sdkKey;

    @Value("${feature.flag.connection.timeout}")
    private int connectionTimeout;

    @Value("${feature.flag.releases}")
    private String releasesFlagKey;

    private static final String FEATURE_FLAG_SERVICE_BEAN_NAME = "featureFlagService";

    private final ConfigurableBeanFactory beanFactory;

    public LibFeatureFlagConfig(final ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Bean
    public LDClient ldClient() {
        LDClient ldClient = new LDClient(sdkKey, getLdConfig(connectionTimeout));
        ldClient.getFlagTracker().addFlagChangeListener(flagKey -> {
                    if (flagKey.getKey().equals(releasesFlagKey)) {
                        updateFeatureFlagServiceMap(ldClient);
                    }
                }
        );
        return ldClient;
    }

    @Bean
    public FeatureFlagService featureFlagService(LDClient ldClient) {
        return new FeatureFlagService(ldClient, ldContext(), flagMap(ldClient));
    }

    @Bean
    public FeatureFlagAspect featureFlagAspect(FeatureFlagService featureFlagService) {
        return new FeatureFlagAspect(featureFlagService);
    }

    private Map<String, String> flagMap(LDClient ldClient) {
        LDValue ldValue = ldClient.jsonValueVariation(this.releasesFlagKey, ldContext(), LDValue.ofNull());
        Map<String, String> flagMap = new HashMap<>();
        ldValue.values().forEach(
                value -> flagMap.put(
                        value.get("key").toString().replace("\"", ""),
                        value.get("release").toString().replace("\"", "")
                )
        );
        return flagMap;
    }

    private LDConfig getLdConfig(final Integer connectionTimeout) {
        return new LDConfig.Builder()
                .http(
                        Components.httpConfiguration()
                                .connectTimeout(Duration.ofSeconds(connectionTimeout))
                )
                .build();
    }

    private LDContext ldContext() {
        return LDContext.builder(apiKey)
                .kind("api")
                .name(apiName)
                .build();
    }

    private void updateFeatureFlagServiceMap(LDClient ldClient) {
        FeatureFlagService featureFlagService = (FeatureFlagService) beanFactory.getBean(FEATURE_FLAG_SERVICE_BEAN_NAME);
        featureFlagService.setFlagMap(flagMap(ldClient));
    }
}
