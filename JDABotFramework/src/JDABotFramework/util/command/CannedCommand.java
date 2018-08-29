package JDABotFramework.util.command;

import java.io.File;

import JDABotFramework.wrapper.JDAMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
/**
 * Generic and very simple commands that responds with the same thing
 * @author Allen
 *
 */
public class CannedCommand implements Command {
	//objects and such for each of the types
	private Object response;
	private Object response2;
	private CanType type;
	/**
	 * Command that responds with the given string
	 * @param response
	 */
	public  CannedCommand(String response){
		this.response=response;
		type=CanType.Str;
	}
	/**
	 * Command that responds by uploading given file
	 * @param file
	 */
	public CannedCommand(File file){
		this.response = file;
		type=CanType.File;
	}
	/**
	 * Command that responds with given message and also uploads file after it
	 * @param message
	 * @param file
	 */
	public CannedCommand(String message,File file){
		this.response=message;
		this.response2=file;
		type=CanType.SFile;
	}
	//override Command interface
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
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
	//enum for the types to make things simpler
	private enum CanType{
		Str,
		File,
		SFile;
	}
}
