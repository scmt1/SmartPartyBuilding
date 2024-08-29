package me.flyray.bsin.server.utils;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptTransaction {
}
