/**
 *
 */
package fr.rsquatre.Meteor.util;

import java.util.HashSet;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public abstract class Converters {

	/**
	 *
	 * Takes an array of objects and return their respective classes in the same
	 * order
	 *
	 * @param objects
	 * @return the classes
	 */
	public static Class<?>[] convert(@NotNull Object... objects) {

		if (objects == null)
			throw new IllegalArgumentException("Cannot convert null to an array of classes");

		Class<?>[] classes = new Class[objects.length];

		for (int i = 0; i < objects.length; i++) { classes[i] = objects[i].getClass(); }

		return classes;
	}

	/**
	 *
	 * @param <A>
	 * @param type
	 * @param array
	 * @return the first value in this array that matches the the requested type
	 */
	@SuppressWarnings("unchecked")
	public static @Nullable <A> A pullFirst(@NotNull Class<A> type, @Nullable Object[] array) {

		if (array != null) {
			for (Object object : array) {
				if (type == object.getClass())
					return (A) object;
			}
		}
		return null;
	}

	/**
	 *
	 * @param <A>
	 * @param type
	 * @param array
	 * @return all objects that match type
	 */
	@SuppressWarnings("unchecked")
	public static @NotNull <A> HashSet<A> pull(@NotNull Class<A> type, @Nullable Object[] array) {

		HashSet<A> set = new HashSet<>();
		if (array != null) { for (Object object : array) { if (type == object.getClass()) { set.add((A) object); } } }

		return set;
	}

}
