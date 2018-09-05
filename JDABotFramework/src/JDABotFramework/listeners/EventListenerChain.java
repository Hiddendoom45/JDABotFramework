package JDABotFramework.listeners;

import java.util.ArrayList;
import java.util.function.BiPredicate;

import JDABotFramework.global.config.BotGlobalConfig;
import net.dv8tion.jda.core.events.Event;

/**
 * Class to handle storing all the listeners for a specific event
 * @author Allen
 *
 * @param <T>
 */
public class EventListenerChain<T extends Event> {
	private Class<T> typeClass;
	private ArrayList<BiPredicate<T,BotGlobalConfig>> listeners = new ArrayList<BiPredicate<T,BotGlobalConfig>>();
	private ArrayList<String> names = new ArrayList<String>();
	public EventListenerChain(Class<T> typeClass){
		this.typeClass = typeClass;
	}
	public void addListener(String name, BiPredicate<T,BotGlobalConfig> listener){
		listeners.add(listener);
		names.add(name);
	}
	public void insertListener(String name, BiPredicate<T,BotGlobalConfig> listener,int index){
		if(index>listeners.size()){
			addListener(name,listener);
		}
		else if(index<0){
			throw new IllegalArgumentException("index cannot be less than 0");
		}
	}
	/**
	 * Execute all handlers for this event
	 * @param event the event to be handled
	 * @param config global config
	 * @return
	 */
	public boolean executeChain(T event,BotGlobalConfig config){
		//if true then break all remaining handlers in event chain
		for(BiPredicate<T,BotGlobalConfig> e:listeners){
			if(e.test(event, config)){//test for each event
				return true;
			}
		}
		return false;
	}
	public Class<T> getType(){
		return typeClass;
	}
}
