package app.captureeasy.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Order {

	/**
	 * The order value. Default is {@link Ordered#LOWEST_PRECEDENCE}.
	 * @see Ordered#getOrder()
	 */
	int value() default Ordered.LOWEST_PRECEDENCE;

}
interface Ordered {

	/**
	 * Useful constant for the highest precedence value.
	 * @see java.lang.Integer#MIN_VALUE
	 */
	int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

	/**
	 * Useful constant for the lowest precedence value.
	 * @see java.lang.Integer#MAX_VALUE
	 */
	int LOWEST_PRECEDENCE = Integer.MAX_VALUE;


	/**
	 * Return the order value of this object, with a
	 * higher value meaning greater in terms of sorting.
	 * <p>Normally starting with 0, with <code>Integer.MAX_VALUE</code>
	 * indicating the greatest value. Same order values will result
	 * in arbitrary positions for the affected objects.
	 * <p>Higher values can be interpreted as lower priority. As a
	 * consequence, the object with the lowest value has highest priority
	 * (somewhat analogous to Servlet "load-on-startup" values).
	 * @return the order value
	 */
	int getOrder();

}
