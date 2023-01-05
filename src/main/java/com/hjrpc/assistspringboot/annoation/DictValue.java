package com.hjrpc.assistspringboot.annoation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DictValue {
    @AliasFor("value")
    String dictCode() default "";

    @AliasFor("dictCode")
    String value() default "";

    String targetFieldName() default "";
}
