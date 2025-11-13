package com.ey.featureflag.exception;

public record FeatureFlagExceptionResponse(
        int status,
        String title,
        String message,
        String method,
        String className
) {
}
