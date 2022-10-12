package com.fuechsl.hostname.routing;

import java.util.HashMap;

public class Router {
	private HashMap<String, Route> m_routes;
	
	/**
	 *  Creates a new empty router
	 */
	public Router() {
		m_routes = new HashMap<String, Route>();
	}
	
	/**
	 * Adds a new route to the router
	 * @param hostname Hostname for the route
	 * @param route Route to append
	 */
	public void AddRoute(String hostname, Route route) {
		m_routes.put(hostname, route);
	}
	
	/**
	 * Removes a route from the router
	 * @param route Route to remove
	 */
	public void RemoveRoute(Route route) {
		for(String key : m_routes.keySet()) {
			if(m_routes.get(key) == route) {
				m_routes.remove(key);
				return;
			}
		}
	}
	
	/**
	 * Invokes the router and return the best matching route
	 * @param hostname Hostename as string
	 * @return Found route or NULL
	 */
	public Route Invoke(String hostname) {
		Route finalRoute = null;
		
		// Split route into parts and try find all possible routes
		String[] routeParts = hostname.split(".");
		for(int i = 0; i < routeParts.length && finalRoute == null; i++) {
			// Build hostname
			StringBuilder currentHostname = new StringBuilder();
			if(i != 0) currentHostname.append("*.");
			for(int j = i; j < routeParts.length; j++) {
				currentHostname.append(routeParts[j]);
				if(j != routeParts.length - 1) currentHostname.append(".");
			}
			
			// Find route
			finalRoute = FindRoute(currentHostname.toString());
		}
		
		return finalRoute;
	}
	
	/**
	 * Will try to find a fully matching hostname
	 * @param hostname Input hostname
	 * @return Matching route or NULL
	 */
	public Route FindRoute(String hostname) {
		Route foundRoute = null;
		if(m_routes.containsKey(hostname)) {
			foundRoute = m_routes.get(hostname);
		}
		
		return foundRoute;
	}
}
