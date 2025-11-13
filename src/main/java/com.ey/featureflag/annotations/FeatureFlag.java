package com.ey.featureflag.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureFlag {
    String feature();
    Class<?> returnOnFailure() default Void.class;
    boolean shouldEnableWithFlag() default true;
    String messageWhenNotEnabled() default "";
}
