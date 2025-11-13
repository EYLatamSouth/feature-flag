package com.ey.featureflag.services;

import com.ey.featureflag.exception.FeatureFlagException;
import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.LDClient;

import java.util.Map;

public class FeatureFlagService {
    private static FeatureFlagService instance;

    private final LDClient ldClient;
    private final LDContext ldContext;
    private Map<String, String> flagMap;

    public FeatureFlagService(final LDClient ldClient,
                              final LDContext ldContext,
                              final Map<String, String> flagMap) {
        this.ldClient = ldClient;
        this.ldContext = ldContext;
        this.flagMap = flagMap;
        instance = this;
    }

    public boolean isFeatureEnabled(String featureKey) {
        boolean isEnabled = false;
        String featureKeyRecovered = getFeatureFlagOnMap(featureKey);
        try {
            isEnabled = ldClient.boolVariation(
                    featureKeyRecovered,
                    ldContext,
                    false
            );
        } catch (Exception exception) {
        }
        return isEnabled;
    }

    private String getFeatureFlagOnMap(String featureKey) {
        if (flagMap.containsKey(featureKey)) {
            return flagMap.get(featureKey);
        } else {
            throw new FeatureFlagException(
                    "Feature Flag Disabled",
                    "getFeatureFlagOnMap",
                    this.getClass().getName(),
                    "Feature key " + featureKey + " not found in flag map",
                    412
            );
        }
    }

    public static synchronized FeatureFlagService getInstance() {
        return instance;
    }

    public void setFlagMap(Map<String, String> flagMap) {
        this.flagMap = flagMap;
    }
}
