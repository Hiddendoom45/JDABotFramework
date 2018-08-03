package JDABotFramework.global.config.guild;

import java.util.Vector;

import com.google.gson.Gson;

/**
 * used to control whether a module is enabled or disabled per guild
 * @author Allen
 *
 */
public class ModuleController {
	private String name;
	private Vector<String> channels=new Vector<String>();
	private boolean globalDisable=false;
	public ModuleController(String moduleName){
		name=moduleName;
	}
	public String getName(){
		return name;
	}
	public ModuleController toggle(String channel){
		if(channels.contains(channel)){
			channels.remove(channel);
		}
		else{
			channels.add(channel);
		}
		return this;
	}
	public ModuleController toggleGlobalDisable(){
		globalDisable=!globalDisable;
		channels.clear();
		return this;
	}
	public boolean enabled(String channel){
		System.out.println(globalDisable);
		if(channels.contains(channel)){
			return globalDisable;
		}else{
			return !globalDisable;
		}
	}
	public boolean enabledGlobal(){
		return !globalDisable;
	}
	public String serializeToJSON(){
		return new Gson().toJson(this);
	}
}
