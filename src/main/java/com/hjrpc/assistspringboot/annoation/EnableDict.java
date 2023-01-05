package com.hjrpc.assistspringboot.annoation;

import com.hjrpc.assistspringboot.AutoDictRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoDictRegistrar.class)
@Target(ElementType.TYPE)
public @interface EnableDict {
    @AliasFor("basePackages")
    String[] value() default {};

    @AliasFor("value")
    String[] basePackages() default {};
}
