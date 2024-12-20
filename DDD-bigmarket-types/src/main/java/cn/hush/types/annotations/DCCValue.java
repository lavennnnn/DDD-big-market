package cn.hush.types.annotations;

import java.lang.annotation.*;

/**
 * @author Hush
 * @description
 * @create 2024-12-21 上午1:51
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DCCValue {

    String value() default "";

}
