package JDABotFramework.launcher;

import javax.security.auth.login.LoginException;

import JDABotFramework.listeners.MainBotListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The main class to launch an instance of a bot
 * 
 * @author Allen
 *
 */
public class Launcher {
	private JDA jda;//jda representing instance
	private ListenerAdapter botListener = new MainBotListener();
	
	/**
	 * Create a new Bot launcher with token. All errors thrown are passed along from JDA. Built blocking by default.
	 * 
	 * @param token token for the bot
	 * @throws RateLimitedException if the object is built too many times in a short period of time
	 * and gets rate limited by Discord
	 * @throws LoginException if there is any issue with the login credentials
	 * @throws InterruptedException if the async thread gets interrupted
	 */
	public Launcher(String token) throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException{
		this(token,false);
	}
	/**
	 * Create a new Bot launcher with token. All errors thrown are passed along from JDA.
	 * 
	 * @param token token for the bot
	 * @param async if to build asynchronous (in a separate thread)
	 * @throws LoginException if there is any issue with the login credentials
	 * @throws RateLimitedException if the object is built too many times in a short period of time
	 * and gets rate limited by Discord
	 * @throws InterruptedException if the async thread gets interrupted
	 */
	public Launcher(String token,boolean async) throws LoginException, IllegalArgumentException, RateLimitedException, InterruptedException{
		JDABuilder jb = new JDABuilder(AccountType.BOT).addEventListener(botListener);
		if(async){
			jda = jb.buildAsync();
		}
		else{
			jda = jb.buildBlocking();
		}
		token = null;
	}
	/**
	 * Add a custom bot listener instead of using the modular based listener
	 * @param botListener
	 * @return
	 */
	public Launcher addListener(ListenerAdapter botListener){
		jda.addEventListener(botListener);
		return this;
	}
	
}
