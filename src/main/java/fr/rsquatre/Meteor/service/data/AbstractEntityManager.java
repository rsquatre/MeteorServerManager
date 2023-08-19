/**
 *
 */
package fr.rsquatre.Meteor.service.data;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fr.rsquatre.Meteor.service.data.schema.AbstractSchema;
import fr.rsquatre.Meteor.system.Service;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */

// TODO add cancelDelete() methods
public abstract class AbstractEntityManager extends Service {

	public abstract @NotNull <E extends AbstractSchema> Collection<E> findAll(@NotNull Class<E> type);

	public abstract @Nullable <E extends AbstractSchema> E find(@NotNull Class<E> type, @NotNull int id);

	public abstract @NotNull <E extends AbstractSchema> Collection<E> find(@NotNull Class<E> type, @NotNull int... ids);

	public abstract @NotNull <E extends AbstractSchema> Collection<E> find(@NotNull Class<E> type, @NotNull Collection<Integer> ids);

	public abstract AbstractEntityManager persist(@NotNull AbstractSchema entity);

	public abstract AbstractEntityManager persist(@NotNull AbstractSchema... entities);

	public abstract AbstractEntityManager persist(@NotNull Collection<? extends AbstractSchema> entities);

	public abstract AbstractEntityManager remove(@NotNull AbstractSchema entity);

	public abstract AbstractEntityManager remove(@NotNull AbstractSchema... entities);

	public abstract AbstractEntityManager remove(@NotNull Collection<? extends AbstractSchema> entities);

	public abstract AbstractEntityManager delete(@NotNull AbstractSchema entity);

	public abstract AbstractEntityManager delete(@NotNull AbstractSchema... entities);

	public abstract AbstractEntityManager delete(@NotNull Collection<? extends AbstractSchema> entities);

	public abstract AbstractEntityManager flush();

	public abstract @NotNull String getSaveType();

	protected abstract boolean isValid();

}
