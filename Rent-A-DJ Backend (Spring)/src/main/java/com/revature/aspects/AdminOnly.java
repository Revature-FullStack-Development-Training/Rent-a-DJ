package com.revature.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // this annotation can only be applied to methods
@Retention(RetentionPolicy.RUNTIME) // this annotation will be available at runtime
public @interface AdminOnly {

    //Our AuthAspect will check for this annotation before allowing HTTP requests
}
