package expanded.testmeta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * These variables are only used for nicer printing when logging all tests and the final breakdown report - they're by
 * no means necessary.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    int id() default -1;

    String name() default "";

    String[] input() default "";

    String expected() default "";
}