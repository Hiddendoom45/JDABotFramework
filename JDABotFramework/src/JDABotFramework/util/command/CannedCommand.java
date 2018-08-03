package JDABotFramework.util.command;

import java.io.File;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.wrapper.JDAMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CannedCommand implements Command {
	private Object response;
	private Object response2;
	private CanType type;
	public  CannedCommand(String response){
		this.response=response;
		type=CanType.Str;
	}
	public CannedCommand(File file){
		this.response = file;
		type=CanType.File;
	}
	public CannedCommand(String message,File file){
		this.response=message;
		this.response2=file;
		type=CanType.SFile;
	}
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event,BotGlobalConfig con) {
		switch(type){
		case File:
			JDAMessage.sendFile(event, "", (File)response);
			break;
		case Str:
			JDAMessage.sendMessage(event, ""+response);
			break;
		case SFile:
			JDAMessage.sendFile(event, ""+response, (File)response2);
			break;
		default:
			break;
		
		}
		
	}

	@Override
	public void help(MessageReceivedEvent event) {
	}

	@Override
	public void executed(boolean sucess, MessageReceivedEvent event) {
	}
	private enum CanType{
		Str,
		File,
		SFile;
	}
}
