package com.fuechsl.hostname;

import java.io.IOException;

import net.md_5.bungee.api.plugin.Plugin;

/***
 * Plugin main class
 * @author Ludwig Fuechsl
 */
public class HostnamePlugin extends Plugin {
	// Bungee event hanlder
	private HostnameEvents m_eventHandler = null;
	// Server name provider
	private IServerProvider m_hostnameLookup = null;
	// Message provider 
	private TextProvider m_messages = null;
	
	@Override
	public void onEnable() {
		// Load settings
		try {
			// Load messages
			m_messages = new TextProvider(getDataFolder(), "messages.yml", this);
			
			// Load settings
			m_hostnameLookup = new HostnameConfig(getDataFolder(), "hostnames.yml");
		
			// Register event handler
			m_eventHandler = new HostnameEvents(m_hostnameLookup, m_messages);
			getProxy().getPluginManager().registerListener(this, m_eventHandler);
		} catch (IOException e) {
			// Plugin failed to load
			e.printStackTrace();
		}
	}
}
