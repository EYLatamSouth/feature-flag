package com.ey.featureflag.annotations;

import com.ey.featureflag.exception.FeatureFlagException;
import com.ey.featureflag.services.FeatureFlagService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
public class FeatureFlagAspect {

    private final FeatureFlagService featureFlagService;

    public FeatureFlagAspect(final FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @Around("@annotation(featureFlag)")
    public Object checkMethodFeatureFlag(ProceedingJoinPoint joinPoint, FeatureFlag featureFlag) throws Throwable {
        String feature = featureFlag.feature();
        if (featureFlag.shouldEnableWithFlag() == featureFlagService.isFeatureEnabled(feature)) {
            return joinPoint.proceed();
        } else {
            logDisabledFeature(featureFlag, joinPoint.getSignature().getName());
            return handleReturnOnFailure(featureFlag);
        }
    }

    @Around("@within(featureFlag)")
    public Object checkClassFeatureFlag(ProceedingJoinPoint joinPoint, FeatureFlag featureFlag) throws Throwable {
        if (featureFlag.shouldEnableWithFlag() == featureFlagService.isFeatureEnabled(featureFlag.feature())) {
            return joinPoint.proceed();
        } else {
            throw new FeatureFlagException(
                   404,
                    featureFlag.feature(),
                    "Feature Flag Disabled",
                    "checkClassFeatureFlag",
                    this.getClass().getName()
            );
        }
    }

    private static void logDisabledFeature(FeatureFlag featureFlag, String callingMethodName) {
        if (Objects.isNull(featureFlag.messageWhenNotEnabled()) || featureFlag.messageWhenNotEnabled().isEmpty()) {

        } else {
        }
    }

    private static Object handleReturnOnFailure(FeatureFlag featureFlag) {
        Object returnValue = null;
        Class<?> clazz = featureFlag.returnOnFailure();
        if (!Void.class.equals(clazz)) {
            try {
                returnValue = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {

            }
        }
        return returnValue;
    }
}
