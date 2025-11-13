package com.ey.featureflag.exception.handler;

import com.ey.featureflag.exception.FeatureFlagException;
import com.ey.featureflag.exception.FeatureFlagExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class FeatureFlagExceptionHandler {

    private FeatureFlagExceptionHandler() {}

    public static ResponseEntity<Object> handleException(FeatureFlagException exception) {
        FeatureFlagExceptionResponse errorResponse = new FeatureFlagExceptionResponse(
                exception.getHttpStatus(),
                exception.getTitle(),
                exception.getMessage(),
                exception.getMethod(),
                exception.getClassName()
        );

        return new ResponseEntity<>(errorResponse, headers(), HttpStatus.valueOf(exception.getHttpStatus()));
    }

    private static HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
