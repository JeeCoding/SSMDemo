package com.huzhframework.ssmdemo.sys.bind.annotation;


import com.huzhframework.ssmdemo.sys.shiro.Constants;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    /**
     * 当前用户在request中的名字
     *
     * @return
     */
    String value() default Constants.CURRENT_USER;

}
