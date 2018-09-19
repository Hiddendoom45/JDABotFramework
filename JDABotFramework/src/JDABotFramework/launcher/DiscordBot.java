package JDABotFramework.launcher;

import java.util.HashMap;

import javax.security.auth.login.LoginException;

import JDABotFramework.commands.Ping;
import JDABotFramework.global.Help;
import JDABotFramework.global.HelpInterface;
import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.listeners.MainBotListener;
import JDABotFramework.react.ReactionController;
import JDABotFramework.util.command.CmdControl;
import JDABotFramework.util.counter.CounterPool;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * A Discord Bot, holding all instances/shards of the bot.
 * Extend to create a new bot. Use {@link BotBuilder} to create the {@link BotInit} object needed to initialize bot.
 * @author Allen
 *
 */
public abstract class DiscordBot implements HelpInterface{
	private final HashMap<Integer,BotInstance> instances = new HashMap<Integer,BotInstance>();
	private boolean shutdown = false;
	protected final BotGlobalConfig config;//config holding pretty much everything
	protected final CmdControl cmd;//used to control commands
	protected final ReactionController react;//used to add reactions etc.
	protected final MainBotListener main;//bot listener
	private BotInit init;//initializer, private as only used for startup
	
	//create JDABot instance 
	/**
	 * Creates a new DiscordBot Object. Generally one per separate bot. 
	 * @param init
	 */
	public DiscordBot(BotInit init){
		this.init = init;
		//copy over stuff from BotInit
		cmd=new CmdControl(init.config);
		cmd.addCommand("ping", new Ping(init.config), "core");
		Help h = new Help(this);
		cmd.addCommand("help", h.help, "core");
		//add mod command
		cmd.addModCommand("help", h.modHelp);
		
		//init other stuff
		react = new ReactionController(init.config);
		main = new MainBotListener(cmd,react,init.config);
		config = init.config;
		Runtime.getRuntime().addShutdownHook(new Thread(){ 
			public void run(){
				shutdownBot();
			}
		});
	}
	/**
	 * Gets the global config object, contains most of the settings for the bot
	 * @return
	 */
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
		shutdownCheck();
		init.init();
		init.instances.forEach((i,inst)->{
			instances.put(i, inst);
			inst.jdaInstance.addEventListener(main);
		});
		init=null;//gc
		CounterPool.getPool().setup();
		react.setup();
		setup();
		// change from default do not disturb setting in BotBuilder
		setActivity(OnlineStatus.ONLINE);
	}
	/**
	 * Called after bot startup is called,use to setup anything that is needed after JDA has initialized(if build blocking)
	 */
	protected abstract void setup();
	
	/**
	 * Shutdown handler
	 */
	protected void shutdownBot(){
		//avoid duplicate calls to shutdown;
		if(!shutdown){
			shutdown();
			shutdown = true;
		}
	}
	
	protected void shutdownCheck(){
		if(shutdown) throw new IllegalStateException("Bot has been shut down");
	}
	
	/**
	 * Called to shutdown the bot or when the shutdown hook triggers
	 */
	protected abstract void shutdown();
	
	/**
	 * Generic help message for the bot
	 * </p>
	 * To override add a command to {@link #cmd} with the key "help"
	 * @param prefix the prefix for the current guild
	 * @return
	 */
	public abstract String help(MessageReceivedEvent event);
	
	/**
	 * Generic mod help message for the bot.
	 * </p>
	 * To override add a command to {@link #cmd} with the key "modhelp"
	 * @param prefix the mod prefix for the current guild
	 * @return
	 */
	public abstract String modHelp(MessageReceivedEvent event);
	
	/**
	 * Set the game for all instances
	 * @param name
	 */
	public void setGame(String name){
		shutdownCheck();
		instances.forEach((i,inst)->{
			inst.jdaInstance.getPresence().setGame(Game.of(name));
		});
	}
	/**
	 * Set activity for all instances, online, do not disturb, invisible
	 * @param status
	 */
	public void setActivity(OnlineStatus status){
		shutdownCheck();
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
		//init hook
		protected abstract void init() throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException;
	}
}
