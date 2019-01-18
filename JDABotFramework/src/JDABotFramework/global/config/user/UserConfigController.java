package JDABotFramework.global.config.user;

import java.util.HashMap;

import com.google.gson.Gson;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.storage.KeyStorageInt;

/**
 * Used to automatically handle the loading of users etc.
 * Lazy loading atm, unlike guildconfig values are not necessary immediately
 * @author Allen
 *
 */
public class UserConfigController {
	private KeyStorageInt store;
	private HashMap<String,UserConfig> users = new HashMap<String,UserConfig>();
	private transient final Gson gson = new Gson();
	final BotGlobalConfig gConfig;
	public UserConfigController(BotGlobalConfig config){
		this.gConfig = config;
	}
	/**
	 * Sets the storage source to be used when saving/loading without storage source arty.
	 * Silently ignores null.
	 * @param store
	 */
	public void setStorageSource(KeyStorageInt store){
		if(!(store==null)){
			this.store=store;
		}
		store.pull();
	}
	
	public void save(){
		for(String s:users.keySet()){
			store.setString(s, gson.toJson(users.get(s)));
		}
		store.push();
	}
	/**
	 * Loads all users from database
	 */
	public void load(){
		for(String s:store.getKeySet()){
			String u = store.getString(s);
			users.put(s,gson.fromJson(u, UserConfig.class));
		}
	}
	
	public UserConfig getUser(String id){
		if(users.containsKey("U:"+id)){
			return users.get("U:"+id);
		}
		else{
			if(!(store==null)){
				String u = store.getString("U:"+id);
				if(!(u==null)){
					return gson.fromJson(u, UserConfig.class);
				}
			}
			UserConfig user = new UserConfig();
			users.put("U:"+id, user);
			return user;
		}
	}
	
}
