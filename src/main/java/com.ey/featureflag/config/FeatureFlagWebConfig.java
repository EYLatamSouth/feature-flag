package com.ey.featureflag.config;

import com.ey.featureflag.interceptors.FeatureFlagInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class FeatureFlagWebConfig implements WebMvcConfigurer {

    private final FeatureFlagInterceptor featureFlagInterceptor;

    private final List<String> pathsToIntercept;

    public FeatureFlagWebConfig(final FeatureFlagInterceptor featureFlagInterceptor,
                                final List<String> pathsToIntercept) {
        this.featureFlagInterceptor = featureFlagInterceptor;
        this.pathsToIntercept = pathsToIntercept;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(featureFlagInterceptor).addPathPatterns(pathsToIntercept);
    }
}
