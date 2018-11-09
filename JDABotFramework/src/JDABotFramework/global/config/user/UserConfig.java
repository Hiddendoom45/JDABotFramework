package JDABotFramework.global.config.user;

import java.util.HashMap;

import JDABotFramework.global.GlobalBot;

/**
 * Container for some basic user config stuff, uses standard key-value storage system like guildConfig
 * Very basic/barebones, designed to be more friendly for persisting user-created classes
 * @author Allen
 */

public class UserConfig {
	//hash map that stores everything
	private HashMap<String,String> config = new HashMap<String,String>();
	
	//basic stuff to set/get from the config with autoserialization methods using Gson
	public String getSetting(String key){
		return config.get(key);
	}
	public <T> T  getSetting(String key,Class<T> itemClass){
		return GlobalBot.gson.fromJson(config.get(key), itemClass);
	}
	public void setSetting(String key,String value){
		config.put(key, value);
	}
	public <T> void setSetting(String key,T item){
		config.put(key, GlobalBot.gson.toJson(item));
	}
}
