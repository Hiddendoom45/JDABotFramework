package JDABotFramework.launcher;

import java.util.HashMap;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.listeners.MainBotListener;
import JDABotFramework.util.command.CmdControl;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * A Discord Bot, holding all instances/shards of the bot.
 * Extend to create a new bot. Use {@link BotBuilder} to create the BotInit object needed to initialize bot.
 * @author Allen
 *
 */
public abstract class DiscordBot {
	private final HashMap<Integer,BotInstance> instances = new HashMap<Integer,BotInstance>();
	protected final BotGlobalConfig config;
	protected final CmdControl cmd;
	protected final ListenerAdapter main;
	//create JDABot instance 
	public DiscordBot(BotInit init){
		//copy over stuff from BotInit
		cmd=init.cmd;
		main = new MainBotListener(cmd);
		init.instances.forEach((i,inst)->{
			instances.put(i, inst);
			inst.jdaInstance.addEventListener(main);
		});
		config = init.config;
		startup();
	}
	public BotGlobalConfig getConfig(){
		return config;
	}
	
	private void startup(){
		setup();
	}
	
	protected abstract void setup();
	
	/**
	 * Generic help message for the bot
	 * </p>
	 * To override add a comand to {@link #cmd} with the key "help"
	 * @return
	 */
	protected abstract String help();
	
	/**
	 * Generic mod help message for the bot.
	 * </p>
	 * To override add a command to {@link #cmd} with the key "modhelp"
	 * @return
	 */
	protected abstract String modHelp();
	
	public void setGame(String name){
		instances.forEach((i,inst)->{
			inst.jdaInstance.getPresence().setGame(Game.of(name));
		});
	}
	public void setActivity(OnlineStatus status){
		instances.forEach((i,inst)->{
			inst.jdaInstance.getPresence().setStatus(status);
		});
	}
	//class that holds the build JDABot initialization stuffs and all the instances
	public static class BotInit{
		private final HashMap<Integer,BotInstance> instances = new HashMap<Integer,BotInstance>();
		private final BotGlobalConfig config;
		private final CmdControl cmd;
		protected BotInit(BotInstance instance,BotGlobalConfig config){
			instances.put(instance.shard, instance);
			this.config=config;
			cmd = new CmdControl(config);
		}
		protected void addInstance(BotInstance instance){
			instances.put(instance.shard, instance);
			config.addLocal(instance.jdaInstance);
		}
	}
}
