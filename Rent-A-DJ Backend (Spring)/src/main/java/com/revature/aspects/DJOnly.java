package com.revature.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Specifies that this annotation can only be applied to methods
@Retention(RetentionPolicy.RUNTIME) // Annotation will be available at runtime for reflection
public @interface DJOnly {

}

