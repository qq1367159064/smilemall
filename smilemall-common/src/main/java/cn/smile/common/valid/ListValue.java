package cn.smile.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/15/015
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { ListValueConstraintValidator.class })
public @interface ListValue {
	
	int [] value();
	
	String message() default "{ cn.smile.common.valid.ListValue }";
	
	Class<?>[] groups() default { };
	
	Class<? extends Payload>[] payload() default { };
}
