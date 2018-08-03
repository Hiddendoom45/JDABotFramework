package JDABotFramework.global.config;

import java.util.HashMap;

import net.dv8tion.jda.core.JDA;

/**
 * Local for each instance of a bot, basic config stuffs.
 * Extends a basic hashmap to allow user to store temporary data in each instance.
 * @author Allen
 *
 */
public class BotLocalConfig extends HashMap<String,Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 362260950741092809L;
	
	private JDA instance;
	protected BotLocalConfig(JDA jda){
		instance = jda;
	}
	public JDA getInstance(){
		return instance;
	}
}
