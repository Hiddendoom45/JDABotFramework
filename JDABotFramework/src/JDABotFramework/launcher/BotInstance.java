package JDABotFramework.launcher;

import net.dv8tion.jda.core.JDA;

/**
 * An instance or shard of a bot
 * 
 * @author Allen
 *
 */
public class BotInstance {
	public final JDA jdaInstance;
	public final int shard;
	public BotInstance(JDA jda){
		this.jdaInstance=jda;
		shard = jda.getShardInfo().getShardId();
	}
	
	
}
