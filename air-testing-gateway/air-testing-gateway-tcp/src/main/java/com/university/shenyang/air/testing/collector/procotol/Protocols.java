package com.university.shenyang.air.testing.collector.procotol;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指令区分注解
 * Created by chenjc on 2017/05/03.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Protocols {

    String id() default "";

    String type() default "";
}
