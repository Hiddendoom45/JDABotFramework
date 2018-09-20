package JDABotFramework.global;

import java.util.function.BiPredicate;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.util.command.Command;
import JDABotFramework.wrapper.JDAMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help implements BiPredicate<MessageReceivedEvent,BotGlobalConfig> {
	
	public final Command help;
	public final Command modHelp;
	
	public final HelpInterface helpInt;
	public Help(HelpInterface help){
		this.helpInt=help;
		this.help = new Command(){
			@Override
			public boolean called(String[] args, MessageReceivedEvent event) {
				return true;
			}

			@Override
			public void action(String[] args, MessageReceivedEvent event) {
				JDAMessage.sendMessageWithSpecials(event, help.help(event));
			}

			@Override
			public void help(MessageReceivedEvent event) {
				JDAMessage.sendMessage(event, "Prints a generic help message");
			}

			@Override
			public void executed(boolean sucess, MessageReceivedEvent event) {}
		};
		this.modHelp = new Command(){
			@Override
			public boolean called(String[] args, MessageReceivedEvent event) {
				return true;
			}

			@Override
			public void action(String[] args, MessageReceivedEvent event) {
				JDAMessage.sendMessageWithSpecials(event, help.modHelp(event));
			}

			@Override
			public void help(MessageReceivedEvent event) {
				JDAMessage.sendMessage(event, "Prints a generic mod help message");
			}

			@Override
			public void executed(boolean sucess, MessageReceivedEvent event) {}
		};
	}
	

	@Override
	public boolean test(MessageReceivedEvent t, BotGlobalConfig u) {
		if(!t.getAuthor().getAvatarId().equals(t.getJDA().getSelfUser().getId())){
			if(t.getMessage().isMentioned(t.getJDA().getSelfUser())){
				if(t.getMessage().getContent().contains("mod help")){
					helpInt.modHelp(t);
					return true;
				}
				else if(t.getMessage().getContent().contains("help")){
					helpInt.help(t);
					return true;
				}
			}
		}
		return false;
	}

}
