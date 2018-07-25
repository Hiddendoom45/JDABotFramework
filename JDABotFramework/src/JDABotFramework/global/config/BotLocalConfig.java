package JDABotFramework.global.config;

import net.dv8tion.jda.core.JDA;

/**
 * Local for each instance of a bot, basic config stuffs
 * @author Allen
 *
 */
public class BotLocalConfig {
	private JDA instance;
	protected BotLocalConfig(JDA jda){
		instance = jda;
	}
	public JDA getInstance(){
		return instance;
	}
}
