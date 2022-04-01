package com.fuechsl.hostname;

import java.util.concurrent.CountDownLatch;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.config.ServerInfo;

/***
 * Event listener class
 * @author Ludwig Fuechsl
 */
public class HostnameEvents implements Listener{
	// Provider for hostname lookups
	private IServerProvider m_lookupProvider = null;
	// Message provider
	private TextProvider m_messages = null;
	
	/***
	 * New event handler
	 * @param provider Input hostname to servername provider
	 */
	public HostnameEvents(IServerProvider provider, TextProvider messages) {
		m_lookupProvider = provider;
		m_messages = messages;
	}
	
	@EventHandler
	public void onPostLogin(PostLoginEvent e) {
		// Get the connections hostname
		String hostName = e.getPlayer().getPendingConnection().getVirtualHost().getHostString();
		
		// Find server
		String targetServer = m_lookupProvider.ServerLookup(hostName);
		if(targetServer != null && ProxyServer.getInstance().getServers().containsKey(targetServer)){
			// Get a server info object
			ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(targetServer);
			
			// Try to reach server
			serverInfo.ping(new Callback<ServerPing>() { 
	            @Override
	            public void done(ServerPing result, Throwable error) {
	            	if(result != null)
	            		// Connect player to the server
	        			e.getPlayer().connect(serverInfo, ServerConnectEvent.Reason.LOBBY_FALLBACK);
	            	else
	            		// Kick player
	            		e.getPlayer().disconnect(new TextComponent(m_messages.GetMessage("joinFailMessage", "§cHostname not supported")));
	            }
			});			
		}else {
			// Disconnect player
			e.getPlayer().disconnect(new TextComponent(m_messages.GetMessage("joinFailMessage", "§cHostname not supported")));
		}
	}
	
	@EventHandler
	public void onProxyPing(ProxyPingEvent e) {
		// Get the pings hostname
		String hostName = e.getConnection().getVirtualHost().getHostString();
		
		// Find Server
		String targetServer = m_lookupProvider.ServerLookup(hostName);

		if(targetServer != null && ProxyServer.getInstance().getServers().containsKey(targetServer)) {
			// Get a server info object
			ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(targetServer);
			// Ping the info server and redirect to this response
			CountDownLatch l = new CountDownLatch(1);
			serverInfo.ping(new Callback<ServerPing>() { 
	            @Override
	            public void done(ServerPing result, Throwable error) {
	            	if(result != null) {
	            		e.setResponse(result);	
	            	}else {
	            		// Dummy response
	        			ServerPing p = new ServerPing();
	        			p.setPlayers(new Players(0, 0, null));
	        			p.setVersion(new ServerPing.Protocol(m_messages.GetMessage("invalidProtocolName", "§cNo Connection"), 0));
	        			p.setDescriptionComponent(new TextComponent(m_messages.GetMessage("invalidMotd", "§cHostname not supported")));
	        			e.setResponse(p);
	            	}
	            	l.countDown();
	            }
			});
			
			// Wait for response
			try {
				l.await();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		} else {
			// Dummy response
			ServerPing p = new ServerPing();
			p.setPlayers(new Players(0, 0, null));
			p.setVersion(new ServerPing.Protocol(m_messages.GetMessage("invalidProtocolName", "§cNo Connection"), 0));
			p.setDescriptionComponent(new TextComponent(m_messages.GetMessage("invalidMotd", "§cHostname not supported")));
			e.setResponse(p);
		}
	}
}
