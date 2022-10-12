package com.fuechsl.hostname.routing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public final class RouteDeserializer {
	/**
	 * Parses a file or directory
	 * @param router Target router
	 * @param dirOrFile
	 */
	public static void Parse(Router router, File dirOrFile) {
		if (dirOrFile.isDirectory()) {
        	ParseDirectory(router, dirOrFile);
        } else {
        	// Try parsing the file
        	try {
				ParseFile(router, dirOrFile);
			} catch (IOException | JsonSyntaxException e) {
				e.printStackTrace();
			}
        }
	}
	
	/**
	 * Parses all json files from a directory
	 * @param router Target router
	 * @param dir Directory to read json from
	 */
	private static void ParseDirectory(Router router, File dir) {
		for (final File file : dir.listFiles()) {
			Parse(router, file);
	    }
	}
	
	/***
	 * Parse one single json file
	 * @param router
	 * @param file
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	private static void ParseFile(Router router, File file) throws JsonSyntaxException, IOException {
		// Validate ending
		String fileName = file.getName().toLowerCase();
		if(fileName.endsWith(".json") || fileName.endsWith(".jsonc")) {
			JsonObject srcObject = new Gson().fromJson(ReadFile(file), JsonObject.class);
			ParseRawJsonObject(router, srcObject);
		}
	}
	
	/**
	 * Parses a raw json object
	 * @param router
	 * @param json
	 */
	private static void ParseRawJsonObject(Router router, JsonObject json) {
		if(json.isJsonArray()) {
			for(int i = 0; i < json.getAsJsonArray().size(); i++) {
				ParseJsonObject(router, json.getAsJsonArray().get(i).getAsJsonObject());
			}
		}
		else
		{
			ParseJsonObject(router, json);
		}
	}
	
	/**
	 * Parses a single object
	 * @param router 
	 * @param json
	 */
	private static void ParseJsonObject(Router router, JsonObject json) {
		System.out.println(json.toString());
	}
	
	/**
	 * Reads a file into a string
	 * @param file File to read from
	 * @return File as string
	 * @throws IOException
	 */
	private static String ReadFile(File file) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			StringBuilder sb = new StringBuilder();
			String line = null;
			String ls = System.getProperty("line.separator");
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(ls);
			}
			
			return sb.toString();
		}
	}
}
