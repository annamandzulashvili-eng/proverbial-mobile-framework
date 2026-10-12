package io.github.annamandzulashvili.mobile.core.hooks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Which app profile a test class installs, for example "wdio". Without it the run default (-Dapp) is used. */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface App {
    String value();
}
