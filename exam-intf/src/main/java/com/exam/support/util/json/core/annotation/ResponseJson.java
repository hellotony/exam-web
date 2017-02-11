package com.exam.support.util.json.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseJson {

    boolean translate() default false;

    Location location() default Location.BYREQUEST;

    compressType compressType() default compressType.NOCOMPRESS;

    enum Location {
        UNDEFINED, DATA, MESSAGE, BYREQUEST
    }

    enum compressType {
        NOCOMPRESS, SNAPPY, GZIP, BYREQUEST
    }
}
