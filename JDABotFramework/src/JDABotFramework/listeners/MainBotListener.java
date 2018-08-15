package JDABotFramework.listeners;

import java.util.function.BiPredicate;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.react.ReactionController;
import JDABotFramework.util.command.CmdControl;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * A modular based bot listener allows for chaining multiple events stuffs, similar to bash
 * using java's lambda expressions.
 * @author Allen
 *
 */
public class MainBotListener extends ListenerAdapter {
	private BiPredicate<ReadyEvent,BotGlobalConfig> ready = null;//no list, will be chained
	private BiPredicate<MessageReceivedEvent,BotGlobalConfig> messageReceived = null;
	private BiPredicate<MessageReactionAddEvent,BotGlobalConfig> emoteAdd = null;
	
	private final BotGlobalConfig config;
	
	public MainBotListener(CmdControl cmd,ReactionController react,BotGlobalConfig config){
		this.config=config;
		addMessageReceived((e,con) -> {
			return cmd.parseCommands(e);
		});
	}
	/**
	 * Effectively a consumer for onReady event, return true to prevent next handler added from being executed. False to 
	 * allow next event added to be executed; 
	 * @param ready
	 */
	public void addReady(BiPredicate<ReadyEvent,BotGlobalConfig> ready){
		if(this.ready==null){
			this.ready=ready;
		}
		else{
			this.ready = (event,con) -> {
				if(!this.ready.test(event,con)){
					return ready.test(event,con);
				}
				else return true;
			};
		}
	}
	/**
	 * Effectively a consumer for onMessageReceived event, return true to prevent next handler from being executed, false
	 * to allow next event added to be executed.
	 * @param messageReceived
	 */
	public void addMessageReceived(BiPredicate<MessageReceivedEvent,BotGlobalConfig> messageReceived){
		if(this.messageReceived==null){
			this.messageReceived=messageReceived;
		}
		else{
			this.messageReceived = (event,con) -> {
				if(!this.messageReceived.test(event,con)){
					return messageReceived.test(event,con);
				}
				else return true;
			};
		}
	}
	public void addMessageReactionAdd(BiPredicate<MessageReactionAddEvent,BotGlobalConfig> emoteAdd){
		if(this.emoteAdd==null){
			this.emoteAdd=emoteAdd;
		}
		else{
			this.emoteAdd = (event,con) -> {
				if(!this.emoteAdd.test(event, con)){
					return emoteAdd.test(event, con);
				}
				else return true;
			};
		}
	}
	//overrides for listeners in the ListenerAdapter
	@Override
	public void onReady(ReadyEvent event){
		if(!(ready==null)){
			ready.test(event,config);
		}
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		if(!(messageReceived==null)){
			messageReceived.test(event,config);
		}
		
	}
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if(!(emoteAdd==null)){
			emoteAdd.test(event, config);
		}
	}
}
