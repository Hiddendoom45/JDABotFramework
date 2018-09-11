package JDABotFramework.listeners;

import java.util.function.BiPredicate;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.wrapper.JDAMessage;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PrefixListener implements BiPredicate<MessageReceivedEvent,BotGlobalConfig>{

	@Override
	public boolean test(MessageReceivedEvent event, BotGlobalConfig u) {
		if(event.isFromType(ChannelType.TEXT)){
			if(event.getMessage().isMentioned(event.getJDA().getSelfUser())){
				if(event.getMessage().getContent().contains("mod prefix")||event.getMessage().getContent().contains("prefix")){
					JDAMessage.sendMessage(event, "mod prefix for guild is:"+u.getModPrefix(event.getGuild()));
					return true;
				}
				else if(event.getMessage().getContent().contains("prefix")){
					JDAMessage.sendMessage(event, "prefix for guild is:"+u.getPrefix(event.getGuild()));
					return true;
				}
				
			}
		}
		return false;
	}
	
}
