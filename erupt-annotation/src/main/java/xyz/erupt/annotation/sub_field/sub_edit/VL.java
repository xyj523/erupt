package xyz.erupt.annotation.sub_field.sub_edit;

import java.beans.Transient;

/**
 * @author YuePeng
 * date 2018-10-11.
 */
public @interface VL {

    String value();

    String label();

    @Transient
    String desc() default "";
}
