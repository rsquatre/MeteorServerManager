/**
 *
 */
package fr.rsquatre.Meteor.util.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * @author <a href="https://github.com/rsquatre">rsquatre</a>
 *
 *         © All rights reserved, unless specified otherwise
 *
 */
public class TransientPostProcessorFactory implements TypeAdapterFactory {

	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

		final TypeAdapter<T> adapter = gson.getDelegateAdapter(this, type);

		return new TypeAdapter<>() {

			@Override
			public void write(JsonWriter out, T value) throws IOException {
				adapter.write(out, value);
			}

			@Override
			public T read(JsonReader in) throws IOException {
				T obj = adapter.read(in);
				if (obj instanceof ITransientPostProcessable) { ((ITransientPostProcessable) obj).postProcess(); }
				return obj;
			}
		};

	}

	public interface ITransientPostProcessable {

		public void postProcess();
	}

}
