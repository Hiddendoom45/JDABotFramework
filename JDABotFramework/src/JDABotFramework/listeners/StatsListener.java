package JDABotFramework.listeners;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
/**
 * Listener that records the stats of certain events
 * @author Allen
 *
 */
public class StatsListener extends ListenerAdapter{
	public final Incrementor messageReceived = new Incrementor();
	public final Incrementor privateMessageReceived = new Incrementor();
	public final Incrementor messageDeleted = new Incrementor();
	public final Incrementor messageEdited = new Incrementor();
	public final Incrementor membersJoined = new Incrementor();
	public final Incrementor membersLeave = new Incrementor();
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		messageReceived.increment();
		if(event.getChannelType().equals(ChannelType.PRIVATE)){
			privateMessageReceived.increment();
		}
	}
	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		messageEdited.increment();
	}
	@Override
    public void onMessageDelete(MessageDeleteEvent event) {
		messageDeleted.increment();
	}
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		membersJoined.increment();
	}
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event){
		membersLeave.increment();
	}
	
	
	public class Incrementor{
		private int i;
		public void increment(){
			i++;
		}
		public int get(){
			return i;
		}
	}
}
