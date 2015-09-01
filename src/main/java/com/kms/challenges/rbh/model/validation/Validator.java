/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.model.validation;

import com.kms.challenges.rbh.model.validation.annotation.FormField;
import com.kms.challenges.rbh.model.validation.annotation.MatchWith;
import com.kms.challenges.rbh.model.validation.annotation.MinLength;
import com.kms.challenges.rbh.model.validation.annotation.Require;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    public static <T> T parseToBeanAndValidate(
            Class<T> clazz, Map<String, String[]> parameterMap,
            Map<String, ValidationError> errorMap) throws
                                                   IllegalAccessException, InstantiationException {
        T bean = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            for (Annotation ano : field.getAnnotations()) {
                if (ano instanceof FormField) {
                    FormField formfieldAnnotation = (FormField) ano;
                    Class fieldType = field.getType();
                    String[] parameterValue = parameterMap.get(formfieldAnnotation.value());
                    Object fieldValue = getFieldValue(parameterValue, fieldType);
                    for (Annotation ano1 : field.getAnnotations()) {
                        if (ano1 instanceof Require) {
                            if (((Require) ano1).require()) {
                                if (parameterValue == null || parameterValue.length == 0 || (parameterValue.length ==
                                                                                             1 && StringUtils
                                                                                                     .isEmpty(
                                                                                                             parameterValue[0]))) {
                                    errorMap.put(formfieldAnnotation.value(),
                                                 new ValidationError(formfieldAnnotation.value(),
                                                                     ((Require) ano1).errorMessage()));
                                }
                            }
                        }
                        LOGGER.debug(String.format("Validate ano: %s | field %s --> value: %s",
                                                   ano1.annotationType().getSimpleName(), formfieldAnnotation.value(),
                                                   parameterValue[0]));
                        // validate min length
                        if (ano1 instanceof MinLength) {
                            LOGGER.debug(String.format("Validate minlength value: %s; length: %s",
                                                       parameterValue[0], parameterValue[0].length()));

                            int min = ((MinLength) ano1).min();
                            String message = ((MinLength) ano1).errorMessage();
                            if (min > 0) {
                                if (parameterValue != null && parameterValue.length > 0 &&
                                    (parameterValue[0].length() <= min)) {
                                    errorMap.put(formfieldAnnotation.value(),
                                                 new ValidationError(formfieldAnnotation.value(), message));
                                }
                            }
                        }

                        if (ano1 instanceof MatchWith) {
                            if (fieldValue != null) {
                                //get the other field value
                                String matchFieldName = ((MatchWith) ano1).fieldName();
                                Object matchFieldValue = null;
                                for (Field field1 : clazz.getDeclaredFields()) {
                                    field1.setAccessible(true);
                                    for (Annotation annotation : field1.getAnnotations()) {
                                        if (annotation instanceof FormField) {
                                            FormField fieldAnnotation = (FormField) annotation;
                                            if (fieldAnnotation.value().equals(matchFieldName)) {
                                                matchFieldValue = getFieldValue(parameterMap.get(matchFieldName),
                                                                                field1.getType());
                                            }
                                        }
                                    }
                                }
                                if (!fieldValue
                                        .equals(matchFieldValue)) {
                                    errorMap.put(formfieldAnnotation.value(),
                                                 new ValidationError(formfieldAnnotation.value(),
                                                                     ((MatchWith) ano1).errorMessage()));
                                }
                            }
                        }

                    }
                    field.set(bean, fieldValue);
                }
            }
        }
        return bean;
    }

    private static <T> T getFieldValue(String[] parameterValue, Class<T> fieldType) {
        if (parameterValue == null) {
            return null;
        }
        if (fieldType == String.class) {
            return fieldType.cast(parameterValue[0]);
        }
        if (fieldType == int.class || fieldType == Integer.class) {
            return fieldType.cast(Integer.parseInt(parameterValue[0]));
        }
        if (fieldType == boolean.class || fieldType == Boolean.class) {
            return fieldType.cast(Boolean.parseBoolean(parameterValue[0]));
        }
        if (fieldType == long.class || fieldType == Long.class) {
            return fieldType.cast(Long.parseLong(parameterValue[0]));
        }
        if (fieldType == float.class || fieldType == Float.class) {
            return fieldType.cast(Float.parseFloat(parameterValue[0]));
        }
        if (fieldType.isArray()) {
            Object[] array = new Object[parameterValue.length];
            for (int i = 0; i < parameterValue.length; i++) {
                array[i] = getFieldValue(new String[]{parameterValue[i]}, fieldType.getComponentType());
            }
            return fieldType.cast(array);
        }
        //supprot only list of string
        if (fieldType == List.class) {
            return fieldType.cast(Arrays.asList(parameterValue));
        }
        throw new IllegalArgumentException(String.format("Class type %s are not supported", fieldType.toString()));
    }
}