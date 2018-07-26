package JDABotFramework.global.config.guild;

import java.util.HashMap;

/**
 * Holds all the separate guild configs (if anything changes) and also 
 * @author Allen
 *
 */
public class GuildConfigController {
	private HashMap<String,GuildConfig> guilds = new HashMap<String,GuildConfig>();
	public GuildConfigController(){
		//TODO add code to load in all guilds
	}
	public GuildConfig getGuild(String id){
		if(guilds.containsKey(id)){
			return guilds.get(id);
		}
		else{
			GuildConfig g = new GuildConfig(id);
			guilds.put(id, g);
			return g;
		}
	}
}
