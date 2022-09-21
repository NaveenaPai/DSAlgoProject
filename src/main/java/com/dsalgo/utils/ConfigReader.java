package com.dsalgo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

//Utility class created to read values from properties file
public class ConfigReader {

	static Properties prop;
	
	public static String GetConfigValue(String key) {
		String filepath = System.getProperty("user.dir") + "/src/main/resources/configs/config.properties";
		String value = "";
		try {
			FileInputStream propFile = new FileInputStream(new File(filepath));
			prop = new Properties();
			prop.load(propFile);

			value = prop.getProperty(key, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
