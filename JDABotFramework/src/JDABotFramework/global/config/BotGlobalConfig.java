package JDABotFramework.global.config;

import java.util.HashMap;

import JDABotFramework.global.config.guild.GuildConfig;
import JDABotFramework.global.config.guild.GuildConfigController;
import net.dv8tion.jda.core.JDA;

/**
 * Global for config for each unique bot, shared across instances
 * @author Allen
 *
 */
public class BotGlobalConfig {
	//container for local configs
	private HashMap<Integer,BotLocalConfig> locals = new HashMap<Integer,BotLocalConfig>();
	private final String defaultPrefix;
	private final String defaultModPrefix;
	private final String ownerID;
	private String selfID;
	private boolean sharded;
	private GuildConfigController guilds = new GuildConfigController();
	public BotGlobalConfig(BotConfigStatics stat){
		//map variables from configstatics to final vars
		this.defaultPrefix=stat.prefix;
		this.defaultModPrefix=stat.modPrefix;
		this.ownerID=stat.ownerID;
	}
	public void addLocal(JDA jda){
		if(locals.isEmpty()){
			if(jda.getShardInfo().getShardTotal()>0) sharded = true;
			else sharded = false;
			selfID = jda.getSelfUser().getId();
		}
		locals.put(jda.getShardInfo().getShardId(), new BotLocalConfig(jda));
	}
	public BotLocalConfig getLocal(int shard){
		return locals.get(shard);
	}
	//getters for static config variables
	public String getPrefix(){
		return defaultPrefix;
	}
	public String getModPrefix(){
		return defaultModPrefix;
	}
	public String getOwnerID(){
		return ownerID;
	}
	public String getSelfID(){
		return selfID;
	}
	public boolean isSharded(){
		return sharded;
	}
	public GuildConfig getGuildConfig(String id){
		return guilds.getGuild(id);
	}
}
