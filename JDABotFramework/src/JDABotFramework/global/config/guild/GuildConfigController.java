package JDABotFramework.global.config.guild;

import java.util.HashMap;

import com.google.gson.Gson;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.storage.KeyStorageInt;

/**
 * Holds all the separate guild configs (if anything changes) and also 
 * @author Allen
 *
 */
public class GuildConfigController {
	private HashMap<String,GuildConfig> guilds = new HashMap<String,GuildConfig>();//holds all the guilds key=G:<guildID>
	private KeyStorageInt store;//place to save things to
	BotGlobalConfig gConfig;
	public GuildConfigController(BotGlobalConfig gConfig){
		this.gConfig=gConfig;
	}
	
	public void setStorageSource(KeyStorageInt store){
		if(!(store==null)){
			this.store=store;
		}
	}
	/**
	 * Load values from storage source
	 * storage source needs to be set using {@link #setStorageSource(KeyStorageInt)}
	 */
	public void load(){
		if(!(store==null)){
			load(store);
		}
	}
	/**
	 * Load values from storage source
	 * @param store
	 */
	public void load(KeyStorageInt store){
		//index containing keys for all the different guilds, stored in this way so that updating is faster
		store.pull();
		String[] index = new Gson().fromJson(store.getString("guildIndex"), String[].class);
		for(String g:index){
			GuildConfig gc = new Gson().fromJson(store.getString(g), GuildConfig.class);
			//hook to push update to source once anything there gets updated
			if(this.store==null){
				gc.setUpdateHook(()->{
					update(gc.id,store);
				});
			}
			else{
				gc.setUpdateHook(()->{
					update(gc.id);
				});
			}
			guilds.put(g, gc);
		}
	}
	/**
	 * update hook to update a guild whenever something about it changes
	 * @param id guildID
	 */
	public void update(String id){
		if(!(store==null)){
			update(id,store);
		}
	}
	/**
	 * update hook to update a guild whenever something about it changes
	 * @param id guildID
	 * @param store place to save it
	 */
	public void update(String id,KeyStorageInt store){
		store.setString("G:"+id, new Gson().toJson(guilds.get("G:"+id)));
	}
	/**
	 * Only update guild index, nothing else
	 * storage source needs to be set using {@link #setStorageSource(KeyStorageInt)}
	 */
	public void quickSave(){
		if(!(store==null)){
			quickSave(store);
		}
	}
	/**
	 * Only update guild index, nothing else
	 * @param store
	 */
	public void quickSave(KeyStorageInt store){
		//update index
		store.setString("guildIndex", new Gson().toJson(guilds.keySet().toArray(new String[]{})));
		store.push();
	}
	/**
	 * Fully update everything pushing to the storage source.
	 * storage source needs to be set using {@link #setStorageSource(KeyStorageInt)} 
	 */
	public void save(){
		if(!(store==null)){
			save(store);
		}
	}
	/**
	 * Fully update everything pushing to storage source
	 * @param store
	 */
	public void save(KeyStorageInt store){
		//update index
		store.setString("guildIndex", new Gson().toJson(guilds.keySet().toArray(new String[]{})));
		for(String s:guilds.keySet()){
			store.setString(s, new Gson().toJson(guilds.get(s)));
		}
		store.push();
	}
	/**
	 * Gets a specific guild
	 * @param id
	 * @return
	 */
	public GuildConfig getGuild(String id){
		if(guilds.containsKey("G:"+id)){
			return guilds.get("G:"+id);
		}
		else{
			GuildConfig g = new GuildConfig(id);
			//set in this manner so if default changes later any existing guilds won't have their prefixes changed
			g.setPrefix(gConfig.getDefaultPrefix());
			g.setModPrefix(gConfig.getDefaultModPrefix());
			if(store!=null){
				g.setUpdateHook(()->{
					update(g.id);
				});
			}
			guilds.put("G:"+id, g);
			return g;
		}
	}
}
