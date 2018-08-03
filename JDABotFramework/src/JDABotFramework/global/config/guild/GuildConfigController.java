package JDABotFramework.global.config.guild;

import java.util.HashMap;

import JDABotFramework.global.config.BotGlobalConfig;

/**
 * Holds all the separate guild configs (if anything changes) and also 
 * @author Allen
 *
 */
public class GuildConfigController {
	private HashMap<String,GuildConfig> guilds = new HashMap<String,GuildConfig>();
	BotGlobalConfig gConfig;
	public GuildConfigController(BotGlobalConfig gConfig){
		this.gConfig=gConfig;
		//TODO add code to load in all guilds
	}
	public GuildConfig getGuild(String id){
		if(guilds.containsKey(id)){
			return guilds.get(id);
		}
		else{
			GuildConfig g = new GuildConfig(id);
			//set in this manner so if default changes later any existing guilds won't have their prefixes changed
			g.setPrefix(gConfig.getDefaultPrefix());
			g.setModPrefix(gConfig.getDefaultModPrefix());
			guilds.put(id, g);
			return g;
		}
	}
}
