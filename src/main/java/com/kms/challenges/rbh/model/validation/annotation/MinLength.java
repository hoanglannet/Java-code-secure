/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.model.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by khoahoang on 9/1/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MinLength {
    int min() default 0;

    String errorMessage() default "Input is illegal.";
}
