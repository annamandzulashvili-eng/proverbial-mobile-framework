package io.github.annamandzulashvili.mobile.core.hooks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Starts the session with a BrowserStack GPS location, "latitude,longitude". */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GpsLocation {
    String value();
}
