package com.fuechsl.hostname;

import java.util.concurrent.CountDownLatch;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
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
	
	/***
	 * New event handler
	 * @param provider Input FQDN to servername provider
	 */
	public HostnameEvents(IServerProvider provider) {
		m_lookupProvider = provider;
	}
	
	@EventHandler
	public void onPostLogin(PostLoginEvent e) {
		// Get the connections hostname
		String hostName = e.getPlayer().getPendingConnection().getVirtualHost().getHostString();
		
		// Find server
		String targetServer = m_lookupProvider.ServerLookup(hostName);
		if(targetServer != null){
			// Get a server info object
			ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(targetServer);
			
			// Connect player to the server
			e.getPlayer().connect(serverInfo);
		}else {
			// Disconnect player
			e.getPlayer().disconnect(new TextComponent("§cInvalid Hostname!\n§rCould not connect you to a server! Please conntact the server admin if you think this is an issue."));
		}
	}
	
	@EventHandler
	public void onProxyPing(ProxyPingEvent e) {
		// Get the pings hostname
		String hostName = e.getConnection().getVirtualHost().getHostString();
		
		// Find Server
		String targetServer = m_lookupProvider.ServerLookup(hostName);
		if(targetServer != null) {
			// Get a server info object
			ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(targetServer);
			
			// Ping the info server and redirect to this response
			CountDownLatch l = new CountDownLatch(1);
			serverInfo.ping(new Callback<ServerPing>() { 
	            @Override
	            public void done(ServerPing result, Throwable error) {
	            	e.setResponse(result);
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
			p.setVersion(new ServerPing.Protocol("§cInvalid Hostname!", 0));
			p.setDescriptionComponent(new TextComponent("§cInvalid Hostname!"));
			e.setResponse(p);
		}
	}
}
