package JDABotFramework.launcher;

import javax.security.auth.login.LoginException;

import JDABotFramework.global.config.BotConfigStatics;
import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.launcher.DiscordBot.BotInit;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Use to build a generic bot instance
 * @author Allen
 *
 */
public class BotBuilder {
	private String token;
	private int shards=0;
	private int[] shardIDs=null;
	private BotConfigStatics stat = new BotConfigStatics();
	public BotBuilder(String token){
		this.token=token;
	}
	/**
	 * Sets the total shards for the bot, if set, turns sharding on when building the Bot.
	 * By default this builds all shards based on the value set, use {@link #setShardIds(int...)} to
	 * specify the specific shards if bot is running across multiple devices/processes
	 * @param shards
	 * @return
	 */
	public BotBuilder setShards(int shards){
		this.shards=shards;
		return this;
	}
	/**
	 * Use to set specifically what shards to build, use if bot is running across multiple devices/processes.
	 * Overrides any ids set using previous calls to this method.
	 * @param shards the Ids for the shards to build
	 * @return
	 */
	public BotBuilder setShardIds(int... shards){
		this.shardIDs=shards;
		return this;
	}
	/**
	 * Use to set the static settings for the bot that cannot be changed after launch
	 * @param stat config statics, edit by setting fields to different value
	 */
	public void setConfigStatics(BotConfigStatics stat){
		this.stat=stat;
	}
	/**
	 * Creates the initializer for the bot that will start it up async
	 * @return BotInit that is used by {@link DiscordBot} to start up bot
	 * @throws LoginException
	 * @throws IllegalArgumentException
	 * @throws RateLimitedException
	 * @throws InterruptedException
	 */
	public BotInit buildAysnc() throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException{
		return buildMain(true);
	}
	/**
	 * Creates the initializer for the bot that will start it up blocking
	 * @return BotInit that is used by {@link DiscordBot} to start up bot
	 * @throws LoginException
	 * @throws IllegalArgumentException
	 * @throws RateLimitedException
	 * @throws InterruptedException
	 */
	public BotInit buildBlocking() throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException {
		return buildMain(false);
	}
	//build stuff with things for both async and blocking
	private BotInit buildMain(boolean async) throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException{
		//new bot init
		BotInit bot = new BotInit(new BotGlobalConfig(stat)){
			public void init() throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException{
				//different build method if sharding
				if(shards>0){
					//if specific shards or just all shards in range
					if(shardIDs==null){
						for(int i = 0;i<shards;i++){
							addInstance(new BotInstance(build(async,i)));
						}
					}
					else{
						for(int i = 0;i<shardIDs.length;i++){
							addInstance(new BotInstance(build(async,shardIDs[i])));
						}
					}
				}
				else{//just add instance shard 0
					addInstance(new BotInstance(build(async,0)));
				}
			}
		};
		return bot;
	}
	//build jda, option for async/blocking
	private JDA build(boolean async,int Shardnum) throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException{
		JDABuilder j = new JDABuilder(AccountType.BOT).setToken(token).setAutoReconnect(true).setStatus(OnlineStatus.DO_NOT_DISTURB);
		if(shards>0){
			j.useSharding(Shardnum, shards);
		}
		if(async){
			return j.buildAsync();
		}
		else{
			return j.buildBlocking();
		}
	}
}
