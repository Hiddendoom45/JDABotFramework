package JDABotFramework.wrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import JDABotFramework.global.GlobalBot;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 * Wrapper for message sending and all stuff related to messages, methods will remains static if any JDA stuff changes
 * @author Allen
 *
 */
public class JDAMessage {
	private static final ArrayList<EmoteData> emotes = new ArrayList<EmoteData>();
	public static Message editMessage(Message message,String msg){
		return message.editMessage(msg).complete();
	}
	public static Message sendFile(MessageReceivedEvent event, Message msg, File file){
			return event.getChannel().sendFile(file, msg).complete();
	}
	public static Message sendFile(MessageReceivedEvent event, String msg, File file){
		Message build=null;
		if(!(msg==null||msg.equals("null"))){
			build=new MessageBuilder().append(msg).build();
		}
		return sendFile(event,build,file);
	}
	/**
	 * Sends a message which will be deleted after a period of time
	 * @param event message recieved
	 * @param msg message to send in response
	 * @param timeout time in seconds after which the message will be deleted
	 */
	public static void sendTempMessage(MessageReceivedEvent event, String msg,long timeout){
		final MessageReceivedEvent FEvent=event;
		final String FMsg=msg;
		final long FTimeout=timeout;
		GlobalBot.executor.execute(new Runnable(){
			public void run(){
				try {
					String id=sendMessageFormated(FEvent, FMsg).getId();
					TimeUnit.SECONDS.sleep(FTimeout);
					FEvent.getChannel().deleteMessageById(id).complete();
				} catch (Exception e) {
				}
				
			}
		});
	}
	/**
	 * 
	 * Formats the message <br/>
	 * Special formatting <br/> 
	 * %userMention% mentions the user that sent the message <br/>
	 * %userName% prints name of user that sent the message <br/>
	 * %selfMention% mentions the bot<br/>
	 * %mentionMention% mentions the first mentioned user in message<br/>
	 * %mentionName% name of the first mentioned user in the message<br/>
	 * @param event message received
	 * @param msg message to send in response
	 * @return message that was sent
	 */
	public static Message sendMessageFormated(MessageReceivedEvent event,String msg){
		return sendMessage(event,FormatMessage(event,msg));
	}
	/**
	 * 
	 * Adds custom emotes to message <br/>
	 * @param event message received
	 * @param msg message to send in response
	 * @return message that was sent
	 */
	public static Message sendMessageEmoted(MessageReceivedEvent event, String msg){
		return sendMessage(event, EmoteMessage(event, msg));
	}
	/**
	 * Formats the message <br/>
	 * Special formatting <br/> 
	 * %userMention% mentions the user that sent the message <br/>
	 * %userName% prints name of user that sent the message <br/>
	 * %selfMention% mentions the bot<br/>
	 * %mentionMention% mentions the first mentioned user in message<br/>
	 * %mentionName% name of the first mentioned user in the message<br/>
	 * <br/>
	 * Adds custom emotes to message <br/>
	 *  @param event message received
	 * @param msg message to send in response
	 * @return message that was sent
	 */
	public static Message sendMessageWithSpecials(MessageReceivedEvent event, String msg){
		return sendMessage(event, EmoteMessage(event, FormatMessage(event, msg)));
	}
	/**
	 * generic send message, will have wrappers to fix some issues and errors in relation to sending messages
	 * @param event message received
	 * @param msg message to send someone
	 * @return message that was sent
	 */
	public static Message sendMessage(MessageReceivedEvent event,String msg){
		if(msg==null) return null;//avoid null message errors
		if(msg.length()>2000){
			Vector<String> toSend=splitMessage(msg);
			for(String s:toSend){
				sendPrivate(event,s);
			}
			if(!event.isFromType(ChannelType.PRIVATE)){
				sendMessage(event,"Message was too long. Check your DMs for response");
			}
			return null;
		}
		Message message=event.getChannel().sendMessage(msg).complete();
		return message;
}
	/**
	 * generic send message, will have wrappers to fix some issues and errors in relation to sending messages
	 * @param event message received
	 * @param msg message to send someone
	 * @return message that was sent
	 */
	public static Message sendMessage(MessageReceivedEvent event, Message msg) {
		Message message=event.getChannel().sendMessage(msg).complete();
		return message;
	}
	public static Message sendPrivate(MessageReceivedEvent event, String msg){
		event.getAuthor().openPrivateChannel().complete();//open private if it's not open
		Message message=event.getAuthor().openPrivateChannel().complete().sendMessage(msg).complete();
		return message;
	}
	private static Vector<String> splitMessage(String msg){
		Vector<String> splitMsg=new Vector<String>();
		String[] lines=msg.split("\n");
		int length=0;
		String prep="";
		for(String s:lines){
			if(s.length()>2000){
				if(!prep.equals("")){
					splitMsg.add(prep);
					prep="";
					length=0;
				}
				splitMsg.add(s.substring(0, 2000));
				splitMsg.addAll(splitMessage(s.substring(2000)));
			}
			else{
				if(s.length()+length<2000){
					prep+="\n"+s;
					length+=s.length()+1;
				}
				else{
					splitMsg.add(prep);
					prep=s;
					length=s.length();
				}
			}
		}
		splitMsg.add(prep);
		return splitMsg;
	}
	/**
	 * Formats the message <br/>
	 * Special formatting <br/> 
	 * %userMention% mentions the user that sent the message <br/>
	 * %userName% prints name of user that sent the message <br/>
	 * %selfMention% mentions the bot<br/>
	 * %mentionMention% mentions the first mentioned user in message<br/>
	 * %mentionName% name of the first mentioned user in the message<br/>
	 * @param event message received
	 * @param msg message to send in response
	 * @return string of formatted message to send
	 */
	public static String FormatMessage(MessageReceivedEvent event,String msg){
		return msg.replace("%userMention%", event.getAuthor().getAsMention()).
				replace("%userName%", event.getAuthor().getName()).
				replace("%selfMention%", event.getJDA().getSelfUser().getAsMention()).
				replace("%mentionMention%", event.getMessage().getMentionedUsers().size()>0?event.getMessage().getMentionedUsers().get(0).getAsMention():event.getAuthor().getAsMention()).
				replace("%mentionName%",event.getMessage().getMentionedUsers().size()>0?event.getMessage().getMentionedUsers().get(0).getName():event.getAuthor().getName());
	}
	/**
	 * Adds custom emotes to message <br/>
	 * @param event
	 * @param msg message to add emotes to 
	 * @return
	 */
	public static String EmoteMessage(MessageReceivedEvent event, String msg){
		if(event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EXT_EMOJI)){
			for(EmoteData e:emotes){
				msg = msg.replace(e.tag, e.emoteValue);
			}
			return msg;
		}
		else{
			for(EmoteData e:emotes){
				msg = msg.replace(e.tag, e.strValue);
			}
			return msg;
		}
	}
	/**
	 * Adds a new emote
	 * @param emote basic data to format emote
	 */
	public static void addEmote(EmoteData emote){
		emotes.add(emote);
	}
	/**
	 * Formats and send message for guild member joining <br/>
	 * Special formatting<br/>
	 * %userMention% mentions the user that joined<br/>
	 * %userName% prints name of the user<br/>
	 * %guildName% prints name of the server
	 * @param event user join event
	 * @param msg message to send in response
	 * @return message sent
	 */
	public static Message sendMessageFormated(GuildMemberJoinEvent event,String msg){
		Message message=event.getGuild().getDefaultChannel().sendMessage(FormatMessage(event,msg)).complete();
		return message; 
	}
	/**
	 * Formats and send message for guild member joining <br/>
	 * Special formatting<br/>
	 * %userMention% mentions the user that joined<br/>
	 * %userName% prints name of the user<br/>
	 * %guildName% prints name of the server
	 * event user join event
	 * @param msg message to send in response
	 * @return string of formatted message to send
	 */
	public static String FormatMessage(GuildMemberJoinEvent event,String msg){
		return msg.replace("%userMention%", event.getMember().getAsMention()).
		replace("%userName%", event.getMember().getNickname()).
		replace("%guildName%",event.getGuild().getName());
	}
}
