package JDABotFramework.util.command.override;

import java.util.ArrayList;
import java.util.HashMap;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.util.command.override.ArgumentParser.ArgContainer;
import JDABotFramework.wrapper.JDAMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
/**
 * Class to handle override commands
 * @author Allen
 *
 */
public class Overrider {
	private final HashMap<Integer,Long> overrides=new HashMap<Integer,Long>();//map of all the current override keys being used
	private final ArrayList<OverrideKey> keys = new ArrayList<OverrideKey>();//array of all still available keys
	private final HashMap<String,OverrideCommand> commands=new HashMap<String,OverrideCommand>();
	private final BotGlobalConfig config;
	private final ArgumentParser parser;
	public Overrider(BotGlobalConfig config){
		this.config=config;
		parser = new ArgumentParser(config);
	}
	/**
	 * Attempts to parse command for an override command
	 * @param event message event
	 * @return if event is an override command event
	 */
	public boolean parseOverride(MessageReceivedEvent event){
		//check if message is one that activates override
		OverrideKey k = isOverride(event.getMessage().getContentRaw());
		if(event.getMessage().isMentioned(event.getJDA().getSelfUser())&&!(k==null)){
			overrides.put(key(event), System.currentTimeMillis());//put it in list
			keys.remove(k);//remove used key
			JDAMessage.sendMessage(event, "Override command activated for "+event.getAuthor().getName()+" 5 minutes");//send message
			return true;
		}
		//if message is sent and override is activated
		else if(overrides.containsKey(key(event))){
			//checks if override is still active
			if((overrides.get(key(event)))<System.currentTimeMillis()-300000||event.getMessage().getContentRaw().equals("exit")){
				overrides.remove(key(event));//if not remove key
				return false;
			}
			else{//otherwise return based on if what's entered is an override command
				return handleOverride(parser.handleArguments(event.getMessage().getContentRaw()),event);
			}
		}//if sender is bot owner cancel other commands only if override command
		else if(event.getAuthor().getId().equals(config.getOwnerID())){
			return handleOverride(parser.handleArguments(event.getMessage().getContentRaw()),event);
		}
		return false;
	}
	public void addOverrideCommand(String name, OverrideCommand command){
		commands.put(name, command);
	}
	private boolean handleOverride(ArgContainer args,MessageReceivedEvent event){
		if(commands.containsKey(args.command)){
			if(args.args.containsKey("help")){
				commands.get(args.command).help(event);
				return true;
			}
			boolean safe=commands.get(args.command).called(args.args, event);
			if(safe){
				commands.get(args.command).action(args.args, event);
			}
			commands.get(args.command).executed(safe, event);
			return true;
		}
		else if(args.command.equalsIgnoreCase("help")){
			if(args.args.containsKey("")&&commands.containsKey(args.args.get("")[0])){
				commands.get(args.args.get("")[0]).help(event);
			}
		}
		System.out.println(args.args);
		return false;
	}
	
	/**
	 * gets the key to track who and where has override
	 * @param e event to get the key for
	 * @return int hash representing the key for the channel and user
	 */
	private static int key(MessageReceivedEvent e){
		return (""+e.getAuthor()+e.getChannel()).hashCode();
	}
	/**
	 * checks if string is a possible override string
	 * @param o String to check
	 * @return override string if true, otherwise null
	 */
	private OverrideKey isOverride(String o){
		for(OverrideKey k:keys){
			if(o.contains("override "+k.value)){
				return k;
			}
		}
		return null;
	}
}
