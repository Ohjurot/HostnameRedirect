package com.fuechsl.hostname;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/***
 * Hostname configuration provider. Will assign a FQDN with an Bungee Server name
 * @author Ludwig Fuechsl
 */
public class HostnameConfig implements IServerProvider {
	// String to String map for assignments
	private HashMap<String, String> m_config = new HashMap<String, String>();
	
	/***
	 * Reads the config fom file
	 * @param configFilename Name of the file from which the configuration shall be loaded
	 * @throws IOException On file issues
	 */
	public HostnameConfig(File dataFolder, String configFilename) throws IOException {
		// Create folder
		if (!dataFolder.exists())
			dataFolder.mkdir();
		
		// Create file
		File configFile = new File(dataFolder, configFilename);
		if(!configFile.exists())
			configFile.createNewFile();
		
		// Load bungee config
		Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
		
		// Iterate over keys / hostnames
		for(String hostName : configuration.getKeys()) {
			// Get servername
			String serverName = configuration.getString(hostName);
			if(serverName != null) {
				// Store in map
				m_config.put(hostName.toLowerCase(), serverName);
			}
		}
	}
	
	@Override
	public String ServerLookup(String fqdn) {
		// Output string
		String out = null;
		if(m_config.containsKey(fqdn.toLowerCase())) {
			out = m_config.get(fqdn.toLowerCase());
		}
		
		return out;
	}
}
