package JDABotFramework.global.config.guild;

import java.util.Vector;
/**
 * used to control whether a module is enabled or disabled per guild
 * @author Allen
 * 
 *
 */
public class ModuleController {
	private String name;//name of module
	private Vector<String> channels=new Vector<String>();//channels where setting is opposite of globalDisable value
	private boolean globalDisable=false;//if module is disabled globally across server or not
	public ModuleController(String moduleName){
		name=moduleName;
	}
	public String getName(){
		return name;
	}
	/**
	 * toggles whether module is enabled or not in a channel
	 * @param channel
	 * @return
	 */
	public ModuleController toggle(String channel){
		if(channels.contains(channel)){
			channels.remove(channel);
		}
		else{
			channels.add(channel);
		}
		return this;
	}
	/**
	 * toggle command globally clearing any channel specific settings
	 * @return
	 */
	public ModuleController toggleGlobalDisable(){
		globalDisable=!globalDisable;
		channels.clear();
		return this;
	}
	/**
	 * Check if a command is enabled for a specific channel
	 * @param channel
	 * @return
	 */
	public boolean enabled(String channel){
		System.out.println(globalDisable);
		if(channels.contains(channel)){
			return globalDisable;
		}else{
			return !globalDisable;
		}
	}
	/**
	 * Check if a command is enabled globally
	 * @return
	 */
	public boolean enabledGlobal(){
		return !globalDisable;
	}
	/**
	 * If in default state (global disable is false and no channel specific setting)
	 * @return
	 */
	public boolean isDefault(){
		if(!globalDisable&&channels.isEmpty()){
			return true;
		}
		else return false;
	}
}
