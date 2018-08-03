package JDABotFramework.global.config.guild;

import java.util.HashMap;

import com.google.gson.Gson;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.storage.StorageInt;

/**
 * Holds all the separate guild configs (if anything changes) and also 
 * @author Allen
 *
 */
public class GuildConfigController {
	private HashMap<String,GuildConfig> guilds = new HashMap<String,GuildConfig>();
	private StorageInt store;
	BotGlobalConfig gConfig;
	public GuildConfigController(BotGlobalConfig gConfig){
		this.gConfig=gConfig;
	}
	
	public void setStorageSource(StorageInt store){
		this.store=store;
	}
	
	public void load(){
		if(!(store==null)){
			load(store);
		}
	}
	public void load(StorageInt store){
		//index containing keys for all the different guilds, stored in this way so that updating is faster
		store.pull();
		String[] index = new Gson().fromJson(store.getString("guildIndex"), String[].class);
		for(String g:index){
			GuildConfig gc = new Gson().fromJson(store.getString(g), GuildConfig.class);
			//hook to push update to source once anything there gets updated
			gc.setUpdateHook(()->{
				update(gc.id);
			});
			guilds.put(g, gc);
		}
	}
	public void update(String id){
		if(!(store==null)){
			update(id,store);
		}
	}
	public void update(String id,StorageInt store){
		store.setString("G:"+id, new Gson().toJson(guilds.get("G:"+id)));
	}
	public void quickSave(){
		if(!(store==null)){
			quickSave(store);
		}
	}
	public void quickSave(StorageInt store){
		//update index
		store.setString("guildIndex", new Gson().toJson(guilds.keySet().toArray(new String[]{})));
		store.push();
	}
	public void save(){
		if(!(store==null)){
			save(store);
		}
	}
	public void save(StorageInt store){
		//update index
		store.setString("guildIndex", new Gson().toJson(guilds.keySet().toArray(new String[]{})));
		for(String s:guilds.keySet()){
			store.setString(s, new Gson().toJson(guilds.get(s)));
		}
		store.push();
	}
	public GuildConfig getGuild(String id){
		if(guilds.containsKey("G:"+id)){
			return guilds.get("G:"+id);
		}
		else{
			GuildConfig g = new GuildConfig(id);
			//set in this manner so if default changes later any existing guilds won't have their prefixes changed
			g.setPrefix(gConfig.getDefaultPrefix());
			g.setModPrefix(gConfig.getDefaultModPrefix());
			g.setUpdateHook(()->{
				update(g.id);
			});
			guilds.put("G:"+id, g);
			return g;
		}
	}
}
