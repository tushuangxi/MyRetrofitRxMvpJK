package com.lding.pad.myseial.library.loadinglibrary.main;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

/**
 * 主要的域
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MainScope {
}
