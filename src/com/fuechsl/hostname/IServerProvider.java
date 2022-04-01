package com.fuechsl.hostname;

/***
 * Defines the Hostname --> Servername lookup provider
 * @author Ludwig Fuechsl
 */
public interface IServerProvider {
	/***
	 * Performs the lookup
	 * @param fqdn Fully-Qualified-Domain-Name for the hostname
	 * @return BungeeCord servername or null
	 */
	public String ServerLookup(String fqdn);
}
