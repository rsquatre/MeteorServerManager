/**
 *
 */
package fr.rsquatre.Meteor.service.data.schema;

import fr.rsquatre.Meteor.util.json.Json;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public abstract class AbstractSchema {

	public abstract int getId();

	public abstract void setId(int id);

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof AbstractSchema))
			return false;

		return obj != null && obj.getClass() == getClass() && ((AbstractSchema) obj).getId() == getId();
	}

	@Override
	public String toString() {
		return getClass().getName() + " :  ".concat(Json.get().toJson(this));
	}

}
