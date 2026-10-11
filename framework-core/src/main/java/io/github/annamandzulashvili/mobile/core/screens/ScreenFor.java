package io.github.annamandzulashvili.mobile.core.screens;

import io.github.annamandzulashvili.mobile.core.enums.Platform;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Marks a concrete screen as the implementation of its abstract parent for the listed platforms. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenFor {
    Platform[] value();
}
