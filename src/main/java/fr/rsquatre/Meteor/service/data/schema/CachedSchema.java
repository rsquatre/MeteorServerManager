/**
 *
 */
package fr.rsquatre.Meteor.service.data.schema;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public abstract class CachedSchema extends AbstractSchema {

	public abstract CacheType getCacheType();

	public enum CacheType {

		MEMORY, LOCAL_FILE;
	}

}
