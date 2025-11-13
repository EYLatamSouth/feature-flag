package com.ey.featureflag;

import com.ey.featureflag.services.FeatureFlagService;

import java.util.Objects;

public class FeatureFlagManager {
    private FeatureFlagManager() {
    }

    public static boolean isFeatureEnabled(String featureKey) {
        FeatureFlagService instance = FeatureFlagService.getInstance();
        return Objects.nonNull(instance) && instance.isFeatureEnabled(featureKey);
    }
}