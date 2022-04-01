# Hostname Redirect
BungeeCord plugin to assign players to servers by the requested hostname

### Configuration
After the first launch of the plugin a file ```plugins/HostnameRedirect/hostnames.yml``` will be created. Use this file to specify which Hostnames should be redirect to which server.

Example:
```yaml
mydomain@com: server1
mc1@mydomain@com: server1
mc2@mydomain@com: server2
```

***IMPORTANT:*** It is important to replace all dots in the hostnames by an ```@``` 

***IMPORTANT:*** The specified servers must be registered within the bungee proxy. The server name will be looked up on demand.  
 
### Customisation
You can customize common messages in the file ```plugins/HostnameRedirect/messages.yml```
