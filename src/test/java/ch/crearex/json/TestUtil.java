package ch.crearex.json;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import ch.crearex.json.test.schema.TestJsonSchema;

public class TestUtil {
	public static String readResource(String resourceName) throws Exception {
		StringBuilder builder = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(TestJsonSchema.class.getResourceAsStream(resourceName), "UTF-8"))) { 
			String line = "";
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
		}
		return builder.toString();
	}
}
