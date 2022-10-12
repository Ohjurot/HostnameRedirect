package com.fuechsl.hostname.routing;

import com.fuechsl.hostname.TextProvider;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

public class Route {
	// Reference text provider
	TextProvider m_textProvider;
	
	// Route parameters
	private String m_targetServerName;
	private String m_serverOfflineProtocol = null;
	private String m_serverOfflineDescription = null;
	
	/**
	 * 
	 * @param serverName Target Server name (route connection)
	 * @param textProvider Provider for retrieving default messages
	 */
	public Route(String serverName, TextProvider textProvider) {
		m_targetServerName = serverName;
		m_textProvider = textProvider;
	}
	
	/**
	 * Update custom offline Texts
	 * @param offlineProtocol Offline protocol
	 * @param offlineDescription Offline description
	 */
	public void SetCustomOfflineText(String offlineProtocol, String offlineDescription) {
		m_serverOfflineProtocol = offlineProtocol;
		m_serverOfflineDescription = offlineDescription;
	}
	
	/**
	 * Retrieve the routes serverinfo (Server to connect to) 
	 * @return Server as bungee ServerInfo
	 */
	public ServerInfo GetSever() {
		return ProxyServer.getInstance().getServers().get(m_targetServerName);
	}
	
	/**
	 * Creates an offline dummy ping
	 * @return ServerPing
	 */
	public ServerPing CreateOfflinePing() {
		ServerPing ping = new ServerPing();
		ping.setPlayers(new Players(0, 0, null));
		ping.setVersion(new ServerPing.Protocol(GetOfflineProtocol(), 0));
		ping.setDescriptionComponent(new TextComponent(GetOfflineDescription()));
		return ping;
	}
	
	/**
	 * Server offline message 
	 * @return String
	 */
	private String GetOfflineProtocol() {
		if(m_serverOfflineProtocol != null)
			return m_serverOfflineProtocol;
		else 
			return m_textProvider.GetMessage("offlineProtocolName", "§cServer Offline");
	}
	
	/**
	 * Server offline message 
	 * @return String
	 */
	private String GetOfflineDescription() {
		if(m_serverOfflineDescription != null)
			return m_serverOfflineDescription;
		else 
			return m_textProvider.GetMessage("offlineMotd", "§cHostname not supported");
	}
}
