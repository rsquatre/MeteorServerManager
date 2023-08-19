/**
 *
 */
package fr.rsquatre.Meteor.util;

import java.lang.reflect.Array;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

/**
 * Asserts that an object meets one or more conditions<br>
 * Some methods may lead to a different outcome depending on the object's type
 *
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public final class Constraints {

	private boolean valid;
	private Object object;
	private CompatibilityMode cMode = CompatibilityMode.FAIL;
	private NullMode nMode = NullMode.FAIL;

	public Constraints(@Nullable Object object) {

		this.object = object;
	}

	/**
	 * Asserts that the object is not null
	 *
	 * @return this
	 */
	public Constraints notNull() {

		if (object == null) { valid = false; }
		return this;
	}

	/**
	 * Asserts that the object is null
	 */
	public Constraints requireNull() {

		if (object != null) { valid = false; }
		return this;
	}

	/**
	 * Asserts that the object is not null, not empty if it is a {@link String}, has
	 * a length greater than 0 if it is an array<br>
	 * See {@link #notBlank()} to assert a String is not blank (empty or spaces
	 * only) instead of not blank <br>
	 * <br>
	 * Affected by {@link CompatibilityMode} and {@link NullMode}
	 *
	 * @return this
	 */
	public Constraints notEmpty() {

		if (object == null && nMode == NullMode.IGNORE)
			return this;

		if (object == null || cMode == CompatibilityMode.FAIL && !(object instanceof String || object != null && !object.getClass().isArray())) {
			valid = false;
			return this;
		}

		if (object instanceof String s && s.isEmpty()) { valid = false; }
		if (!object.getClass().isArray() || Array.getLength(object) == 0) { valid = false; }
		return this;
	}

	/**
	 * Asserts that the object is not null, not blank (empty or spaces only) if it
	 * is a {@link String}, has a length greater than 0 if it is an array<br>
	 * See {@link #notEmpty()} to assert a String is not empty instead of not
	 * blank<br>
	 * <br>
	 * Affected by {@link CompatibilityMode} and {@link NullMode}
	 *
	 * @return this
	 */
	public Constraints notBlank() {

		if (object == null && nMode == NullMode.IGNORE)
			return this;

		if (object == null || cMode == CompatibilityMode.FAIL && !(object instanceof String || object != null && !object.getClass().isArray())) {
			valid = false;
			return this;
		}

		if (object instanceof String s && s.isBlank()) { valid = false; }
		if (!object.getClass().isArray() || Array.getLength(object) == 0) { valid = false; }
		return this;
	}

	/**
	 * Asserts that object matches the the pattern<br>
	 * <br>
	 * Affected by {@link CompatibilityMode} and {@link NullMode}
	 *
	 * @param pattern
	 */
	public Constraints regex(String pattern) {

		if (object == null && nMode == NullMode.FAIL) { valid = false; }

		if (!(object instanceof String) && cMode == CompatibilityMode.FAIL) { valid = false; }

		if (object instanceof String s) { valid = s.matches(pattern); }
		return this;
	}

	public Constraints requireAssert(Function<Object, Boolean> function) {

		valid = function.apply(object);
		return this;
	}

	/**
	 * Defines if assertions should fail or be skipped when it cannot be performed
	 * on the object<br>
	 * The mode may be changed multiple times before calling {@link #isValid()} :
	 * <u>call order does matter</u> <br>
	 * <br>
	 * Default: {@link CompatibilityMode#FAIL}
	 *
	 * @param mode
	 * @return this
	 */
	public Constraints compatibilityMode(CompatibilityMode mode) {

		cMode = mode;
		return this;
	}

	/**
	 * Defines if null values should be ignored or if the assertion should fail<br>
	 * The mode may be changed multiple times before calling {@link #isValid()} :
	 * <u>call order does matter</u> <br>
	 * <br>
	 * Default: {@link NullMode#FAIL}
	 *
	 * @param mode
	 * @return this
	 */
	public Constraints nullMode(NullMode mode) {

		nMode = mode;
		return this;
	}

	/**
	 * Sets {@link #compatibilityMode(CompatibilityMode)} and
	 * {@link #nullMode(NullMode)}
	 *
	 * @param mode
	 * @return this
	 */
	public Constraints modes(CompatibilityMode c, NullMode n) {

		cMode = c;
		nMode = n;
		return this;
	}

	/**
	 *
	 * @return true if there was no failure during constraints validation, false
	 *         otherwise
	 */
	public boolean isValid() {
		return valid;
	}

	public static final class NotNullOrDefault<A> {

		private final A value;
		private final A defaultValue;

		public NotNullOrDefault(@Nullable A value, @NotNull A defaultValue) {

			if (defaultValue == null)
				throw new IllegalArgumentException("C'mon mate what's the point?! Parameter defaultValue cannot be null");

			this.value = value;
			this.defaultValue = defaultValue;
		}

		public boolean isNull() {
			return value == null;
		}

		public A value() {
			return isNull() ? defaultValue : value;
		}

	}

	public static enum CompatibilityMode {

		FAIL, SKIP;
	}

	public static enum NullMode {
		FAIL, IGNORE;
	}

}
