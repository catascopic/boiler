package au.com.codeka.carrot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import au.com.codeka.carrot.bindings.JsonArrayBindings;
import au.com.codeka.carrot.bindings.JsonObjectBindings;
import au.com.codeka.carrot.util.SafeString;

/**
 * Various helpers for working with {@link Object}s.
 */
public class ValueHelper {
	/**
	 * Does the given value represent "true". For example, it's a Boolean that's
	 * true, a non-zero integer, etc.
	 *
	 * @param value The value to test.
	 * @return True if the value is "true-ish", false otherwise.
	 * @throws CarrotException When the value cannot be determined to be true or
	 *         false.
	 */
	public static boolean isTrue(Object value) throws CarrotException {
		if (value == null) {
			return false;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof Number) {
			return ((Number) value).intValue() != 0;
		}
		if (value instanceof String) {
			return !((String) value).isEmpty();
		}
		if (value instanceof CharSequence) {
			return ((CharSequence) value).length() > 0;
		}
		if (value instanceof Collection) {
			return !((Collection<?>) value).isEmpty();
		}
		if (value instanceof Map) {
			return !((Map<?, ?>) value).isEmpty();
		}
		if (value instanceof Bindings) {
			return !((Bindings) value).isEmpty();
		}
		if (value instanceof Iterable) {
			// evaluate non-empty iterables to true, empty iterables to false
			return !((Iterable<?>) value).iterator().hasNext();
		}
		// any unknown non-null, non-boolean value evaluates to true
		return true;
	}

	/**
	 * Returns the "negative" of the given value. For example, if you pass in
	 * "1" then "-1" is returned, etc.
	 *
	 * @param value The value to negate.
	 * @return The negated value.
	 * @throws CarrotException Thrown if the value can't be converted to a
	 *         number.
	 */
	public static Number negate(Object value) throws CarrotException {
		Number num = toNumber(value);
		if (num instanceof Integer) {
			return -((Integer) num);
		}
		if (num instanceof Long) {
			return -((Long) num);
		}
		if (num instanceof Float) {
			return -((Float) num);
		}
		if (num instanceof Double) {
			return -((Double) num);
		}
		throw new CarrotException("Value '" + value + "' cannot be negated.");
	}

	/**
	 * Converts the given value to a {@link Number}.
	 *
	 * @param value The value to convert.
	 * @return A {@link Number} that the value represents.
	 * @throws CarrotException Thrown if the value can't be converted to a
	 *         number.
	 */
	public static Number toNumber(Object value) throws CarrotException {
		if (value instanceof Number) {
			return (Number) value;
		}
		if (value instanceof String) {
			return parseNumber((String) value);
		}
		throw new CarrotException("Cannot convert '" + value + "' to a number.");
	}

	static Number parseNumber(String value) {
		if (value.contains(".")) {
			return Double.parseDouble(value);
		}
		return Long.parseLong(value);
	}

	/**
	 * Adds the two values together. We attempt to make them the most-precise
	 * they can be (i.e. if one of them is a double then double is returned, if
	 * one of them is a long then long is returned, etc).
	 *
	 * @param o1 The left-hand side to add.
	 * @param o2 The right-hand side to add.
	 * @return The two numbers added together.
	 * @throws CarrotException Thrown is either of the values can't be converted
	 *         to a number.
	 */
	public static Number add(Object o1, Object o2) throws CarrotException {
		Number n1 = toNumber(o1);
		Number n2 = toNumber(o2);
		if (n1 instanceof Double || n2 instanceof Double) {
			return n1.doubleValue() + n2.doubleValue();
		} else if (n1 instanceof Float || n2 instanceof Float) {
			return n1.floatValue() + n2.floatValue();
		} else if (n1 instanceof Long || n2 instanceof Long) {
			return n1.longValue() + n2.longValue();
		} else if (n1 instanceof Integer || n2 instanceof Integer) {
			return n1.longValue() + n2.longValue();
		}
		throw new CarrotException("Unknown number type '" + o1 + "' or '" + o2 + "'.");
	}

	/**
	 * Divides the left hand side by the right hand side, and returns the
	 * result.
	 *
	 * @param o1 The left hand side of the division.
	 * @param o2 The right hand side of the division.
	 * @return The result of "lhs / rhs".
	 * @throws CarrotException Thrown is either of the values cannot be
	 *         converted to a number.
	 */
	public static Number divide(Object o1, Object o2) throws CarrotException {
		Number n1 = toNumber(o1);
		Number n2 = toNumber(o2);
		if (n1 instanceof Double || n2 instanceof Double) {
			return n1.doubleValue() / n2.doubleValue();
		} else if (n1 instanceof Float || n2 instanceof Float) {
			return n1.floatValue() / n2.floatValue();
		} else if (n1 instanceof Long || n2 instanceof Long) {
			return n1.longValue() / n2.longValue();
		} else if (n1 instanceof Integer || n2 instanceof Integer) {
			return n1.longValue() / n2.longValue();
		}
		throw new CarrotException("Unknown number type '" + o1 + "' or '" + o2 + "'.");
	}

	/**
	 * Multiplies the left hand side by the right hand side, and returns the
	 * result.
	 *
	 * @param o1 The left hand side of the multiplication.
	 * @param o2 The right hand side of the multiplication.
	 * @return The result of "lhs * rhs".
	 * @throws CarrotException Thrown is either of the values cannot be
	 *         converted to a number.
	 */
	public static Number multiply(Object o1, Object o2) throws CarrotException {
		Number n1 = toNumber(o1);
		Number n2 = toNumber(o2);
		if (n1 instanceof Double || n2 instanceof Double) {
			return n1.doubleValue() * n2.doubleValue();
		} else if (n1 instanceof Float || n2 instanceof Float) {
			return n1.floatValue() * n2.floatValue();
		} else if (n1 instanceof Long || n2 instanceof Long) {
			return n1.longValue() * n2.longValue();
		} else if (n1 instanceof Integer || n2 instanceof Integer) {
			return n1.longValue() * n2.longValue();
		}
		throw new CarrotException("Unknown number type '" + o1 + "' or '" + o2 + "'.");
	}

	/**
	 * Convert the given value to a list of object, as if it were an iterable.
	 * If the value is itself an array or a list then it's just returned
	 * in-place. Otherwise it will be converted to an {@link ArrayList}.
	 *
	 * @param iterable The value to "iterate".
	 * @return A {@link List} that can actually be iterated.
	 * @throws CarrotException If the value is not iterable.
	 */
	public static Collection<?> iterate(Object iterable) throws CarrotException {
		if (iterable == null) {
			return Collections.emptySet();
		}
		if (iterable instanceof Collection) {
			return (Collection<?>) iterable;
		}
		if (iterable instanceof Iterable) {
			return Lists.newArrayList((Iterable<?>) iterable);
		}
		if (iterable instanceof Map) {
			return ((Map<?, ?>) iterable).keySet();
		}
		if (iterable.getClass().isArray()) {
			return Arrays.asList((Object[]) iterable);
		}
		throw new CarrotException("Unable to iterate '" + iterable + "'");
	}

	/**
	 * Tests the equality of the two given values.
	 *
	 * @param lhs The left-hand side you want to test for equality.
	 * @param rhs The right-hand side you want to test for equality.
	 * @return A value to indicate whether the value is true or false.
	 * @throws CarrotException If there's an error evaluating the objects.
	 */
	public static boolean isEqual(Object lhs, Object rhs) throws CarrotException {
		if (lhs instanceof Number || rhs instanceof Number) {
			return compare(lhs, rhs) == 0;
		}
		return Objects.equals(lhs, rhs);
	}

	/**
	 * Performs a numerical comparison on the two operands (assuming they are
	 * both convertible to numbers).
	 *
	 * @param o1 The left hand side to compare.
	 * @param o2 The right hand side to compare.
	 * @return Less than zero if lhs is less than rhs, zero if lhs is equal to
	 *         rhs, and greater than zero if lhs is greater than rhs.
	 * @throws CarrotException if either of the objects cannot be converted to
	 *         numbers.
	 */
	public static int compare(Object o1, Object o2) throws CarrotException {
		Number n1 = toNumber(o1);
		Number n2 = toNumber(o2);
		if (n1 instanceof Double || n2 instanceof Double) {
			return Double.compare(n1.doubleValue(), n2.doubleValue());
		}
		if (n1 instanceof Float || n2 instanceof Float) {
			return Float.compare(n1.floatValue(), n2.floatValue());
		}
		if (n1 instanceof Long || n2 instanceof Long) {
			return Long.compare(n1.longValue(), n2.longValue());
		}
		if (n1 instanceof Integer || n2 instanceof Integer) {
			return Integer.compare(n1.intValue(), n2.intValue());
		}
		throw new CarrotException("Unknown number type.");
	}

	/**
	 * Performs HTML-escaping of the given value.
	 *
	 * @param value The value to escape. If the value is {@link SafeString},
	 *        then no escaping will be done.
	 * @return The HTML-escaped version of the string.
	 */
	// TODO: without pulling in any other deps (e.g. Jakarta Commons) is this as
	// comprehensive as it needs to be?
	public static String escape(@Nullable Object value) {
		if (value == null) {
			return "";
		}
		if (value instanceof SafeString) {
			return value.toString();
		}
		String unescaped = value.toString();
		return unescaped.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	public static Object jsonHelper(Object object) {
		if (object instanceof JsonObject) {
			return new JsonObjectBindings((JsonObject) object);
		}
		if (object instanceof JsonArray) {
			return new JsonArrayBindings((JsonArray) object);
		}
		if (JsonNull.INSTANCE.equals(object)) {
			return null;
		}
		return object;
	}

}