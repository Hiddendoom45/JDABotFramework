package JDABotFramework.global.config.guild;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Special configuration stuffs for a guild.
 * @author Allen
 *
 */
public class GuildConfig {
	private HashMap<String,String> config = new HashMap<String,String>();
	private String prefix = null;
	private String modPrefix = null;
	private ArrayList<String> modded = new ArrayList<String>();
	public final String id;
	public GuildConfig(String id){
		this.id=id;
	}
	public String getSetting(String setting){
		return config.get(setting);
	}
	public void setSetting(String setting,String value){
		config.put(setting, value);
	}
	public ArrayList<String> getModded(){
		return modded;
	}
	public String getPrefix(){
		return prefix;
	}
	public String getModPrefix(){
		return modPrefix;
	}
}
