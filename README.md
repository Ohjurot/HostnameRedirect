# Hostname Redirect
BungeeCord plugin to assign players to servers by the requested hostname

### Configuration
After the first launch of the plugin a file ```plugins/HostnameRedirect/hostnames.yml``` will be created. Use this file to specify which Hostnames should be redirect to which server.

Example:
```yaml
mydomain.com: server1
mc1.mydomain.com: server1
mc2.mydomain.com: server2
```

***IMPORTANT:*** the specified servers must be registred within the bungee proxy. The servername will be looked up on demand.  
 
### Customisation
Coming soon TM
