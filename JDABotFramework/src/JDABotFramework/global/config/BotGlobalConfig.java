package JDABotFramework.global.config;

import java.util.HashMap;

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
	private final boolean sharded;
	public BotGlobalConfig(BotConfigStatics stat,JDA jda){
		//map variables from configstatics to final vars
		this.defaultPrefix=stat.prefix;
		this.defaultModPrefix=stat.modPrefix;
		this.ownerID=stat.ownerID;
		//put first/only shard into locals list
		locals.put(jda.getShardInfo().getShardId(), new BotLocalConfig(jda));
		//TODO test shard detection
		if(jda.getShardInfo().getShardTotal()>0) sharded = true;
		else sharded = false;
		
	}
	public void addLocal(JDA jda){
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
	public boolean isSharded(){
		return sharded;
	}
}
