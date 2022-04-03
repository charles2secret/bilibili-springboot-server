package com.bilibili.domain.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

// for Spring Aop
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface ApiLimitedRole {

    String[] limitedRoleCodeList() default  {};
}
