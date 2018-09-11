package JDABotFramework.global;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
/**
 * Basic interface for help strings
 * @author Allen
 *
 */
public interface HelpInterface {
	public String help(MessageReceivedEvent event);
	
	public String modHelp(MessageReceivedEvent event);
}
