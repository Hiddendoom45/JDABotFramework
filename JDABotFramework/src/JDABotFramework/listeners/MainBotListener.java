package JDABotFramework.listeners;

import java.util.function.Predicate;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * A modular based bot listener allows for chaining multiple events stuffs, similar to bash
 * using java's lambda expressions.
 * @author Allen
 *
 */
public class MainBotListener extends ListenerAdapter {
	private Predicate<ReadyEvent> ready = null;//no list, will be chained
	private Predicate<MessageReceivedEvent> messageReceived = null;
	
	
	/**
	 * Effectively a consumer for onReady event, return true to prevent next handler added from being executed. False to 
	 * allow next event added to be executed; 
	 * @param ready
	 */
	public void addReady(Predicate<ReadyEvent> ready){
		if(this.ready==null){
			this.ready=ready;
		}
		else{
			this.ready = event -> {
				if(!this.ready.test(event)){
					return ready.test(event);
				}
				else return true;
			};
		}
	}
	/**
	 * Effectively a consumer for onMessageReceived event, return true to prevent next hndler from being executed, false
	 * to allow next event added to be executed.
	 * @param messageReceived
	 */
	public void addMessageReceived(Predicate<MessageReceivedEvent> messageReceived){
		if(this.messageReceived==null){
			this.messageReceived=messageReceived;
		}
		else{
			this.messageReceived = event -> {
				if(!this.messageReceived.test(event)){
					return messageReceived.test(event);
				}
				else return true;
			};
		}
	}
	//overrides for listeners in the ListenerAdapter
	@Override
	public void onReady(ReadyEvent event){
		if(!(ready==null)){
			ready.test(event);
		}
	}
	
	
}
