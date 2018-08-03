package JDABotFramework.launcher;

import java.util.HashMap;

import javax.security.auth.login.LoginException;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.listeners.MainBotListener;
import JDABotFramework.util.command.CannedCommand;
import JDABotFramework.util.command.CmdControl;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
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
	private BotInit init;
	
	//create JDABot instance 
	public DiscordBot(BotInit init){
		this.init = init;
		//copy over stuff from BotInit
		cmd=new CmdControl(init.config);
		cmd.addCommand("help", new CannedCommand(help()), "core");
		cmd.addCommand("modhelp", new CannedCommand(modHelp()), "core");
		main = new MainBotListener(cmd);
		config = init.config;
	}
	public BotGlobalConfig getConfig(){
		return config;
	}
	/**
	 * Initializes all the bot instances and runs the {@link #setup()} method afterwards
	 * @throws LoginException
	 * @throws IllegalArgumentException
	 * @throws RateLimitedException
	 * @throws InterruptedException
	 */
	public void startup() throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException{
		init.init();
		init.instances.forEach((i,inst)->{
			instances.put(i, inst);
			inst.jdaInstance.addEventListener(main);
		});
		setup();
		// change from default do not disturb setting in BotBuilder
		setActivity(OnlineStatus.ONLINE);
	}
	
	protected abstract void setup();
	
	/**
	 * Generic help message for the bot
	 * </p>
	 * To override add a command to {@link #cmd} with the key "help"
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
	
	/**
	 * Set the game for all instances
	 * @param name
	 */
	public void setGame(String name){
		instances.forEach((i,inst)->{
			inst.jdaInstance.getPresence().setGame(Game.of(name));
		});
	}
	/**
	 * Set activity for all instances, online, do not disturb, invisible
	 * @param status
	 */
	public void setActivity(OnlineStatus status){
		instances.forEach((i,inst)->{
			inst.jdaInstance.getPresence().setStatus(status);
		});
	}
	//class that holds the build JDABot initialization stuffs and all the instances
	public abstract static class BotInit{
		/**
		 * Temporarily holds the instances for the bot while stuff is being built
		 */
		private final HashMap<Integer,BotInstance> instances = new HashMap<Integer,BotInstance>();
		private final BotGlobalConfig config;
		protected BotInit(BotGlobalConfig config){
			this.config=config;
		}
		protected void addInstance(BotInstance instance){
			instances.put(instance.shard, instance);
			config.addLocal(instance.jdaInstance);
		}
		protected abstract void init() throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException;
	}
}
