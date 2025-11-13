package com.ey.featureflag.exception;

public class FeatureFlagException extends RuntimeException{
    private final String method;
    private final String className;

    private final String title;
    private final int httpStatus;

    public FeatureFlagException(
            final int httpStatus,
            final String featureKey,
            final String title,
            final String method,
            final String className
            ) {
        super("Feature flag '" + featureKey + "' is disabled");
        this.method = method;
        this.className = className;
        this.httpStatus = httpStatus;
        this.title = title;
    }

    public FeatureFlagException(
            final String title,
            final String method,
            final String className,
            final String message,
            final int httpStatus
    ) {
        super(message);
        this.method = method;
        this.className = className;
        this.httpStatus = httpStatus;
        this.title = title;
    }

    public String getMethod() {
        return method;
    }

    public String getClassName() {
        return className;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getTitle() {
        return title;
    }
}
