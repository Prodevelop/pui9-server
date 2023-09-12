package es.prodevelop.pui9.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is useful to keep generated elements by the code generator
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface PuiGenerated {

	boolean notOverride() default false;

}
