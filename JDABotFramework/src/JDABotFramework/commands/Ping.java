package JDABotFramework.commands;

import java.time.OffsetDateTime;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.util.command.Command;
import JDABotFramework.wrapper.JDAMessage;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping implements Command {
	private final BotGlobalConfig config;
	public Ping(BotGlobalConfig config){
		this.config=config;
	}
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		Message pingMsg=JDAMessage.sendMessage(event, "pong...");//send precursor message
		//calculate time it took for response message to be sent and recieved
		OffsetDateTime message=event.getMessage().getCreationTime();
		OffsetDateTime now=pingMsg.getCreationTime();
		int messageTime=message.getNano()/1000000;//mili message
		int currentTime=now.getNano()/1000000;//mili current
		long messageS=message.toEpochSecond();//sec message
		long currentS=now.toEpochSecond();//sec current
		int responseS;//delta sec
		responseS=(int) Math.abs(currentS-messageS);//diff in sec
		if(OffsetDateTime.timeLineOrder().compare(message, now)==1){//invert signage based on which one's less
			responseS=responseS*-1;
		}
		int response;
		//System.out.println(nH+" "+mH+" "+currentS+" "+messageS+" "+currentTime+" "+messageTime);
		if(responseS==0){
			response=currentTime-messageTime;
		}
		else{
			response=currentTime-messageTime+(1000*(responseS));
		}
		JDAMessage.editMessage(pingMsg, "pong! - response in "+response+"ms");
	}

	@Override
	public void help(MessageReceivedEvent event) {
		String s=config.getPrefix(event.getGuild())+"ping\n"
				+ "\tget the bot to say PONG\n"
				+ "\tto test the bot's response speed";
		JDAMessage.sendMessage(event, s);
	}

	@Override
	public void executed(boolean sucess, MessageReceivedEvent event) {
		// TODO Auto-generated method stub

	}

}
