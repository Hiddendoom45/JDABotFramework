package JDABotFramework.launcher;

import java.util.HashMap;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.listeners.MainBotListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * A JDA Bot, holding all instances/shards of the bot
 * @author Allen
 *
 */
public class JDABot {
	private HashMap<Integer,BotInstance> instances = new HashMap<Integer,BotInstance>();
	private final BotGlobalConfig config;
	private ListenerAdapter main;
	//create JDABot instance 
	protected JDABot(BotInstance instance,BotGlobalConfig config){
		instances.put(instance.shard, instance);
		this.config=config;
		main = new MainBotListener(config);
		instance.jdaInstance.addEventListener(main);
	}
	protected void addInstance(BotInstance instance){
		instances.put(instance.shard, instance);
		instance.jdaInstance.addEventListener(main);
		config.addLocal(instance.jdaInstance);
	}
	public BotGlobalConfig getConfig(){
		return config;
	}
}
