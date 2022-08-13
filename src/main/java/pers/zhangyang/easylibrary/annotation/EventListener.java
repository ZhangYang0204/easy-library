package pers.zhangyang.easylibrary.annotation;

import java.lang.annotation.*;

/**
 * 使用此注解，类会自动被作为事件类注册，要在easyLibrary.yml指定的包下的类才有效
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventListener {
}
