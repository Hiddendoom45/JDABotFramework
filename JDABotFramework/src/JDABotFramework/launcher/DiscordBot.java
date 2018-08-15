package JDABotFramework.launcher;

import java.util.HashMap;

import javax.security.auth.login.LoginException;

import JDABotFramework.commands.Ping;
import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.listeners.MainBotListener;
import JDABotFramework.react.ReactionController;
import JDABotFramework.util.command.CannedCommand;
import JDABotFramework.util.command.CmdControl;
import JDABotFramework.util.counter.CounterPool;
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
	protected final BotGlobalConfig config;//config holding pretty much everything
	protected final CmdControl cmd;//used to control commands
	protected final ReactionController react;//used to add reactions etc.
	protected final ListenerAdapter main;//bot listener
	private BotInit init;//initializer, private as only used for startup
	
	//create JDABot instance 
	public DiscordBot(BotInit init){
		this.init = init;
		//copy over stuff from BotInit
		cmd=new CmdControl(init.config);
		cmd.addCommand("help", new CannedCommand(help()), "core");
		cmd.addCommand("modhelp", new CannedCommand(modHelp()), "core");
		cmd.addCommand("ping", new Ping(init.config), "core");
		react = new ReactionController(init.config);
		main = new MainBotListener(cmd,react,init.config);
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
		init=null;//gc
		CounterPool.getPool().setup();
		setup();
		// change from default do not disturb setting in BotBuilder
		setActivity(OnlineStatus.ONLINE);
	}
	/**
	 * Called after bot startup is called,use to setup anything that is needed after JDA has initialized(if build blocking)
	 */
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
		//init hook
		protected abstract void init() throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException;
	}
}
