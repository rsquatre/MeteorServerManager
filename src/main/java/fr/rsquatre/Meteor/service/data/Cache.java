/**
 *
 */
package fr.rsquatre.Meteor.service.data;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import fr.rsquatre.Meteor.Meteor;
import fr.rsquatre.Meteor.service.data.schema.CachedSchema;
import fr.rsquatre.Meteor.system.Service;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class Cache extends Service {

	private HashMap<Class<? extends CachedSchema>, HashMap<Integer, CachedSchema>> cache = new HashMap<>();

	@Override
	public Class<? extends JavaPlugin> getOwner() {

		return Meteor.class;
	}

	@SuppressWarnings("unchecked")
	public <X extends CachedSchema> X fetch(Class<X> type, int id) {

		return cache.containsKey(type) ? (X) cache.get(type).get(id) : null;
	}

	@SuppressWarnings("unchecked")
	public <X extends CachedSchema> HashMap<Integer, X> fetchAll(Class<X> type) {

		return (HashMap<Integer, X>) cache.get(type);
	}

	public Cache cache(CachedSchema entity) {

		cache.putIfAbsent(entity.getClass(), new HashMap<>());
		cache.get(entity.getClass()).put(entity.getId(), entity);

		return this;
	}

	public boolean isCached(CachedSchema entity) {

		return cache.containsKey(entity.getClass()) && cache.get(entity.getClass()).containsKey(entity.getId());
	}

	public Cache clean(CachedSchema entity) {

		if (cache.containsKey(entity.getClass())) {

			cache.get(entity.getClass()).remove(entity.getId());
		}

		return this;
	}

	public Cache clean(Class<? extends CachedSchema> type) {

		cache.remove(type);

		return this;
	}

}
