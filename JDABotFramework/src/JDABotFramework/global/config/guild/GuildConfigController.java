package JDABotFramework.global.config.guild;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;

import com.google.gson.Gson;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.storage.KeyStorageInt;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Holds all the separate guild configs (if anything changes) and also 
 * @author Allen
 *
 */
public class GuildConfigController {
	private HashMap<String,GuildConfig> guilds = new HashMap<String,GuildConfig>();//holds all the guilds key=G:<guildID>
	private KeyStorageInt store;//place to save things to
	private transient final Gson gson = new Gson();
	BotGlobalConfig gConfig;
	public GuildConfigController(BotGlobalConfig gConfig){
		this.gConfig=gConfig;
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
	}
	/**
	 * Load values from storage source
	 * storage source needs to be set using {@link #setStorageSource(KeyStorageInt)}
	 * @throws UncheckedIOException if storage source not set using {@link #setStorageSource(KeyStorageInt)}
	 */
	public void load(){
		if(!(store==null)){
			load(store);
		}
		else{
			throw new UncheckedIOException(new IOException("load:Storage source not set"));
		}
	}
	/**
	 * Load values from storage source
	 * @param store
	 */
	public void load(KeyStorageInt store){
		//index containing keys for all the different guilds, stored in this way so that updating is faster
		store.pull();
		String[] index = gson.fromJson(store.getString("guildIndex"), String[].class);
		for(String g:index){
			GuildConfig gc = gson.fromJson(store.getString(g), GuildConfig.class);
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
	 * @throws UncheckedIOException if storage source not set using {@link #setStorageSource(KeyStorageInt)}
	 */
	public void update(String id){
		if(!(store==null)){
			update(id,store);
		}
		else{
			throw new UncheckedIOException(new IOException("update:Storage source not set"));
		}
	}
	/**
	 * update hook to update a guild whenever something about it changes
	 * @param id guildID
	 * @param store place to save it
	 */
	public void update(String id,KeyStorageInt store){
		store.setString("G:"+id, gson.toJson(guilds.get("G:"+id)));
	}
	/**
	 * Fully update everything pushing to the storage source.
	 * storage source needs to be set using {@link #setStorageSource(KeyStorageInt)}
	 * @throws UncheckedIOException if storage source not set using {@link #setStorageSource(KeyStorageInt)} 
	 */
	public void save(){
		if(!(store==null)){
			save(store);
		}
		else{
			throw new UncheckedIOException(new IOException("save:Storage source not set"));
		}
	}
	/**
	 * Fully update everything pushing to storage source
	 * @param store
	 */
	public void save(KeyStorageInt store){
		//update index
		store.setString("guildIndex", gson.toJson(guilds.keySet().toArray(new String[]{})));
		for(String s:guilds.keySet()){
			store.setString(s, gson.toJson(guilds.get(s)));
		}
		store.push();
	}
	/**
	 * Gets a specific guild
	 * @param id
	 * @return the guild config for the guild, automatically creates a new guildConfig if it is not found
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
	/**
	 * Gets a specic guild
	 * @param event event to extract the guild from
	 * @return
	 */
	public GuildConfig getGuild(MessageReceivedEvent event){
		return getGuild(event.getGuild().getId());
	}
}
