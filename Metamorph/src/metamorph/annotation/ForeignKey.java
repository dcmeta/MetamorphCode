package metamorph.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)
public @interface ForeignKey {
	String table_reference();
	String reference_column();
	String column();
	Class class_reference();
}
