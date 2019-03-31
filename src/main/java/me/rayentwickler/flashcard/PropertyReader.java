package me.rayentwickler.flashcard;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
	private Properties prop = new Properties();

	public String getProperty(String key){
		return prop.getProperty(key);
	}

	public PropertyReader() {
		InputStream input = null;
		try {

			String filename = "project.properties";
			input = PropertyReader.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return;
			}

			// load a properties file from class path, inside static method
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
