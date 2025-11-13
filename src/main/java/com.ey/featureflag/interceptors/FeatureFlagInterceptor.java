package com.ey.featureflag.interceptors;

import com.ey.featureflag.services.FeatureFlagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

public class FeatureFlagInterceptor implements HandlerInterceptor {
    private final FeatureFlagService featureFlagService;

    private final String featureFlagKey;

    public FeatureFlagInterceptor(final FeatureFlagService featureFlagService,
                                  final String featureFlagKey) {
        this.featureFlagService = featureFlagService;
        this.featureFlagKey = featureFlagKey;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
        var isEnabled = featureFlagService.isFeatureEnabled(featureFlagKey);

        if (!isEnabled) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.getWriter().write("Feature flag " + featureFlagKey + " is disabled");
        }

        return isEnabled;
    }
}
