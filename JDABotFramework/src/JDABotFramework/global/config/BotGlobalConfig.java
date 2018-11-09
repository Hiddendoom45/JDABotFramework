package JDABotFramework.global.config;

import java.util.HashMap;

import JDABotFramework.global.config.guild.GuildConfig;
import JDABotFramework.global.config.guild.GuildConfigController;
import JDABotFramework.global.config.user.UserConfig;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;

/**
 * Global for config for each unique bot, shared across instances
 * @author Allen
 *
 */
public class BotGlobalConfig {
	//container for local configs, reinitialized each time
	private HashMap<Integer,BotLocalConfig> locals = new HashMap<Integer,BotLocalConfig>();
	private HashMap<Long,UserConfig> users = new HashMap<Long,UserConfig>();
	private final String defaultPrefix;//default prefix set by BotConfigStatics
	private final String defaultModPrefix;//default modprefix set by BotConfigStatics
	private final String ownerID;//Owner ID
	private final String overrideArg;//argument for override commands
	private final String overridePrefix;
	private String selfID;//ID of the bot istself, automatically set
	private boolean sharded;//autodetection of if bot is sharded
	private GuildConfigController guilds = new GuildConfigController(this);
	public BotGlobalConfig(BotConfigStatics stat){
		//map variables from configstatics to final vars
		this.defaultPrefix=stat.prefix;
		this.defaultModPrefix=stat.modPrefix;
		this.ownerID=stat.ownerID;
		this.overrideArg=stat.overrideArg;
		this.overridePrefix=stat.overridePrefix;
	}
	/**
	 * Adds an instnace of JDA, creating the local config for it based on its shardID
	 * @param jda
	 */
	public void addLocal(JDA jda){
		if(locals.isEmpty()){
			sharded = !(jda.getShardInfo()==null);
			selfID = jda.getSelfUser().getId();
		}
		locals.put(jda.getShardInfo()==null?0:jda.getShardInfo().getShardId(), new BotLocalConfig(jda));
	}
	/**
	 * Gets the local config for a specific shard
	 * @param shard shard ID
	 * @return
	 */
	public BotLocalConfig getLocal(int shard){
		return locals.get(shard);
	}
	//getters for static config variables
	/**
	 * Gets the default prefix for the bot, set when joining a new guild
	 * @return
	 */
	public String getDefaultPrefix(){
		return defaultPrefix;
	}
	/**
	 * Gets the default modprefix for the bot, set when joining a new guild
	 * @return
	 */
	public String getDefaultModPrefix(){
		return defaultModPrefix;
	}
	/**
	 * Get the prefix for a specific guild
	 * @param g
	 * @return
	 */
	public String getPrefix(Guild g){
		if(g==null){
			return defaultPrefix;
		}
		else{
			return guilds.getGuild(g.getId()).getPrefix();
		}
	}
	/**
	 * Gets the mod prefix for a specific guild
	 * @param g
	 * @return
	 */
	public String getModPrefix(Guild g){
		if(g==null){
			return defaultModPrefix;
		}
		else{
			return guilds.getGuild(g.getId()).getModPrefix();
		}
	}
	//getters
	public String getOwnerID(){
		return ownerID;
	}
	public String getOverrideArg(){
		return overrideArg;
	}
	public String getOverridePrefix(){
		return overridePrefix;
	}
	public String getSelfID(){
		return selfID;
	}
	public boolean isSharded(){
		return sharded;
	}
	/**
	 * Gets the guild config for a specific guild
	 * @param id
	 * @return
	 */
	public GuildConfig getGuildConfig(String id){
		return guilds.getGuild(id);
	}
	/**
	 * Gets the guild config for a specific guild
	 * @param g guild object
	 * @return
	 */
	public GuildConfig getGuildConfig(Guild g){
		return getGuildConfig(g.getId());
	}
	public UserConfig getUser(String id){
		return getUser(Long.parseLong(id));
	}
	public UserConfig getUser(long id){
		return users.get(id);
	}
}
