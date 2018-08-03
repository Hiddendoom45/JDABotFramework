package JDABotFramework.launcher;

import net.dv8tion.jda.core.JDA;

/**
 * An instance or shard of a bot
 * 
 * @author Allen
 *
 */
public class BotInstance {
	/**
	 * JDA for this instance
	 */
	public final JDA jdaInstance;
	/**
	 * Shard ID for this instance
	 */
	public final int shard;
	public BotInstance(JDA jda){
		this.jdaInstance=jda;
		shard = jda.getShardInfo().getShardId();
	}
	
	
}
