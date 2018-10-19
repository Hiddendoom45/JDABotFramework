package JDABotFramework.global.config.guild;

import java.util.ArrayList;
import java.util.HashMap;

import JDABotFramework.global.GlobalBot;

/**
 * Special configuration stuffs for a guild.
 * @author Allen
 *
 */
public class GuildConfig {
	private HashMap<String,String> config = new HashMap<String,String>();
	private String prefix = null;
	private String modPrefix = null;
	//private list of all users that have been explicitly given mod permissions
	private ArrayList<String> modded = new ArrayList<String>();
	private Runnable update = () -> {};//update this guild in w/e stuff is stored called whenever a value changes
	//module controllers, if added, likely something disabled
	private final HashMap<String,ModuleController> disabled=new HashMap<String,ModuleController>();
	public final String id;
	public GuildConfig(String id){
		this.id=id;
	}
	public String getSetting(String key){
		return config.get(key);
	}
	public <T> T getSetting(String key, Class<T> itemClass){
		return GlobalBot.gson.fromJson(getSetting(key), itemClass);
	}
	public void setSetting(String setting,String value){
		config.put(setting, value);
		update.run();
	}
	public void setSetting(String key,Object item){
		setSetting(key, GlobalBot.gson.toJson(item));
	}
	public ArrayList<String> getModded(){
		return modded;
	}
	public void setPrefix(String prefix){
		this.prefix=prefix;
		update.run();
	}
	public void setModPrefix(String modPrefix){
		this.modPrefix=modPrefix;
		update.run();
	}
	public String getPrefix(){
		return prefix;
	}
	public String getModPrefix(){
		return modPrefix;
	}
	//stuff in relation to the module controller
	public boolean isEnabled(String module,String channelID){
		ModuleController m = disabled.get(module);
		if(m==null) return true;
		else return m.enabled(channelID);
	}
	public boolean isEnabled(String module){
		ModuleController m = disabled.get(module);
		if(m==null) return true;
		else return m.enabledGlobal();
	}
	public void moduleToggle(String module){
		if(disabled.containsKey(module)){
			disabled.get(module).toggleGlobalDisable();
			if(disabled.get(module).isDefault()){
				disabled.remove(module);
			}
		}
		else{
			ModuleController m = new ModuleController(module);
			m.toggleGlobalDisable();
			disabled.put(module, m);
		}
		update.run();
	}
	public void moduleToggle(String module,String channelID){
		if(disabled.containsKey(module)){
			ModuleController m = disabled.get(module);
			m.toggle(channelID);
			if(m.isDefault()){
				disabled.remove(module);
			}
		}
		else{
			ModuleController m = new ModuleController(module);
			m.toggle(channelID);
			disabled.put(module, m);
		}
		update.run();
	}
	//sets a hook that is called whenever some value is updated
	public void setUpdateHook(Runnable r){
		update=r;
		if(update==null){
			update = () -> {};
		}
	}
}
