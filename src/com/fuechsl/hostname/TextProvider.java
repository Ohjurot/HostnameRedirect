package com.fuechsl.hostname;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/***
 * Class that will read and keep all static text
 * @author Ludwig Fuechsl
 */
public class TextProvider {
	// Configured messages
	private Configuration m_cgfMessages = null;
	
	/***
	 * 
	 * @param dataFolder Base directory where the config file will be located
	 * @param configFilename Name of the text file AND template file in the package
	 * @param plugin the plugin to retrive the stream
	 * @throws IOException 
	 */
	public TextProvider(File dataFolder, String configFilename, Plugin plugin) throws IOException {
		// Create folder
		if (!dataFolder.exists())
			dataFolder.mkdir();
		
		// Create file
		File configFile = new File(dataFolder, configFilename);
		if(!configFile.exists()) {
			// Open input stream
			InputStream in = plugin.getResourceAsStream(configFilename);
			// Open output stream
			configFile.createNewFile();
			OutputStream out = new FileOutputStream(configFile);
			
			// Stream data
			int size = in.available();
			byte buffer[] = new byte[size];
			in.read(buffer, 0, size);
			out.write(buffer, 0, size);
			
			// Close streams
			in.close();
			out.close();
		}
		
		// Open bungee setting object
		m_cgfMessages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
	}
	
	/***
	 * Retrieve an message string 
	 * @param key Name of the string to retrieve
	 * @param fallback String used in case the message was not found
	 * @return Always a valid text
	 */
	public String GetMessage(String key, String fallback) {
		String msg = fallback;
		if(m_cgfMessages.contains(key)) {
			msg = m_cgfMessages.getString(key);
			if(msg == null)
				msg = fallback;
		}
		
		return msg;
	}
}
