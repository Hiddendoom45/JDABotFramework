package JDABotFramework.react;

import java.util.HashMap;

import JDABotFramework.global.GlobalBot;
import JDABotFramework.global.config.BotGlobalConfig;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

/**
 * Handles any reaction events
 * @author Allen
 *
 */
public class ReactionController implements Runnable {
	private HashMap<String,Reaction> storedReactions=new HashMap<String,Reaction>();//holds the reaction to invoke for each message with one
	private HashMap<String,Message> storedMessages=new HashMap<String,Message>();//hold message to avoid calling api again to get message
	private HashMap<Long, String> killTimes=new HashMap<Long, String>();// holds long for kill time and string for
	private BotGlobalConfig config;//config for some global stuff
	public ReactionController(BotGlobalConfig config){
		this.config=config;
		setup();
	}
	/**
	 * Parses a reactionAddEvent checking if it's registered by any Reaction
	 * @param event
	 * @return true if matching Reaction found
	 */
	public boolean parseReaction(MessageReactionAddEvent event){
		if(!event.getUser().getId().equals(config.getSelfID())){// don't trigger if self
			String ID=event.getMessageId();
			if(storedReactions.containsKey(ID)){
				//same as commands, check if valid, then execute it
				boolean safe=storedReactions.get(ID).called(event, event.getReaction().getEmote());
				if(safe){//if reaction triggers something
					Message msg=storedReactions.get(ID).action(event, event.getReaction().getEmote(), storedMessages.get(ID));
					storedMessages.put(ID, msg);//update the stored message
					try{
						event.getReaction().removeReaction(event.getUser()).queue();//remove it so it can be retriggered
					}catch(net.dv8tion.jda.core.exceptions.PermissionException e){
						
					}
				}
				storedReactions.get(ID).executed(event);
				return safe;//safe basically determines if trigger
			}
		}
		return false;
	}
	/**
	 * Parses a reactionRemoveEvent checking if it's registered by any Reaction
	 * @param event
	 * @return true if matching Reaction found
	 */
	public boolean parseReaction(MessageReactionRemoveEvent event){
		if(!event.getUser().getId().equals(config.getSelfID())){// don't trigger if self
			String ID=event.getMessageId();
			if(storedReactions.containsKey(ID)){
				//same as commands, check if valid, then execute it
				boolean safe=storedReactions.get(ID).called(event, event.getReaction().getEmote());
				if(safe){//if reaction triggers something
					Message msg=storedReactions.get(ID).action(event, event.getReaction().getEmote(), storedMessages.get(ID));
					storedMessages.put(ID, msg);//update the stored message
					try{
						event.getReaction().removeReaction(event.getUser()).queue();//remove it so it can be retriggered
					}catch(net.dv8tion.jda.core.exceptions.PermissionException e){
						
					}
				}
				storedReactions.get(ID).executed(event);
				return safe;//safe basically determines if trigger
			}
		}
		return false;
	}
	/**
	 * 
	 * @param msg message for the bot to react to 
	 * @param reaction reaction for the bot to use when something occurs
	 */
	public synchronized void addReaction(Message msg,Reaction reaction){
		storedReactions.put(msg.getId(), reaction);
		storedMessages.put(msg.getId(), msg);
		if(reaction.getTimeout()>0){
			Long killTime=System.currentTimeMillis()+reaction.getTimeout();
			killTimes.put(killTime, msg.getId());
			this.notify();
		}
	}
	/**
	 * start thread as a part of setup
	 */
	public void setup(){
		GlobalBot.executor.execute(this);
	}
	/**
	 * Loop that kills reaction event once they go over timeout
	 */
	public void run(){
		synchronized(this){
			while(true){//constant loop
				Long soonest=Long.MAX_VALUE;
				for(Long l:killTimes.keySet()){
					if(l<System.currentTimeMillis()){//remove as expired
						String ID=killTimes.get(l);
						storedReactions.remove(ID);
						storedMessages.remove(ID);
						killTimes.remove(l);
					}
					else if(l<soonest){//determine next one to expire
						soonest=l;
					}
				}
				try {
					if(soonest==Long.MAX_VALUE){//nothing in list wait indefinitely until notified
						this.wait();
					}
					else{//wait for the exact amount of time till the next one
						this.wait(soonest-System.currentTimeMillis());
					}
				}
				catch (InterruptedException e) {
					break;//if interrupted by executor break out of loop and end thread
				}
			}
		}
		
	}
}
