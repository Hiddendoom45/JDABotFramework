package JDABotFramework.util.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.global.config.guild.ModuleController;
import JDABotFramework.wrapper.JDAMessage;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Use to hold and handle commands
 * @author Allen
 *
 */
public class CmdControl {
	private final ArrayList<Permission> modPermissions = new ArrayList<Permission>();//generic permisisons that probably indicate mod
	public final CommandParser parser=new CommandParser(null,null);//parse most commands
	//maps for commands
	private HashMap<String,Command> commands=new HashMap<String,Command>();
	private HashMap<String,Command> modCommands=new HashMap<String,Command>();
	private HashMap<String,String> modules=new HashMap<String,String>();//used to get which module the command is from
	private BotGlobalConfig config;
	public CmdControl(BotGlobalConfig config){
		this.config = config;
	}
	public boolean parseCommands(MessageReceivedEvent event){
		if(event.getAuthor().getId().equals(config.getSelfID()))return false;//avoid responding to self
		if(event.getAuthor().isBot())return false;//avoid responding to other bots
		//extract content
		String content=event.getMessage().getContent();
		//checks for prefix, if found handle command
		if(content.startsWith(config.getPrefix(event.getGuild()))){
			CommandParser.CommandContainer cmd=parser.parse(content, event);
			if(CommandEnabled(event,cmd.invoke)){
				return handleCommand(parser.parse(content, event));
			}
		}
		if(content.startsWith(config.getModPrefix(event.getGuild()))){
			return handleCommand(parser.parse(content, event));
		}
		return false;
	}
	public void commandAction(MessageReceivedEvent event, String command, String[] args){
		//action if enabled by modulecontroller
		if(CommandEnabled(event,command)){
			commands.get(command).action(args, event);
		}
	}
	public void addCommand(String commandName, Command command, String Module){
		commands.put(commandName, command);
		modules.put(commandName, Module);
	}
	public void addCommand(String[] commandNames,Command command,String Module){
		for(String name:commandNames){
			commands.put(name, command);
			modules.put(name, Module);
		}
	}
	public void addModCommand(String commandName,Command command){
		modCommands.put(commandName, command);
	}
	public void removeCommand(String commandName){
		commands.remove(commandName);
		modules.remove(commandName);
		
	}
	private  boolean handleCommand(CommandParser.CommandContainer cmd){
		//test if map contains command and that prefix is not for mod commands
		if(commands.containsKey(cmd.invoke)&&!cmd.isModCmd){
			//generic command call stuff, first invoke call, if true, also invoke action and finally invoke executed
			boolean safe=commands.get(cmd.invoke).called(cmd.args, cmd.e);
			if(safe){
				commands.get(cmd.invoke).action(cmd.args, cmd.e);
				commands.get(cmd.invoke).executed(safe, cmd.e);
			}
			else{
				commands.get(cmd.invoke).executed(safe, cmd.e);
			}
			return true;
		}
		//test if map contains modcommand and that prefix is one for mod commands
		else if(modCommands.containsKey(cmd.invoke)&&cmd.isModCmd){
			//test for if person is mod
			if(!isMod(cmd.e)){
				JDAMessage.sendTempMessage(cmd.e, ":no_entry_sign: You are not authorized to use mod commands here",20);
			}
			//generic command call stuff, first invoke call, if true, also invoke action and finally invoke executed
			boolean safe=modCommands.get(cmd.invoke).called(cmd.args, cmd.e);
			if(safe){
				modCommands.get(cmd.invoke).action(cmd.args, cmd.e);
				modCommands.get(cmd.invoke).executed(safe, cmd.e);
			}
			else{
				modCommands.get(cmd.invoke).executed(safe, cmd.e);
			}
			return true;
		}
		//specialized help stuff
		else if(cmd.invoke.equals("help")){
			//default print mod/regular help and if command in arg, print help for that command
			if(cmd.isModCmd){
				if(cmd.args.length>0&&modCommands.containsKey(cmd.args[0])){
					modCommands.get(cmd.args[0]).help(cmd.e);
				}
				else{
					commands.get("modhelp").action(cmd.args, cmd.e);
				}
			}
			else{
				if(cmd.args.length>0&&commands.containsKey(cmd.args[0])){
					commands.get(cmd.args[0]).help(cmd.e);
				}
				else{
					commands.get("help").action(cmd.args, cmd.e);
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * checks if user is mod and has the admin privilege, or has been set to bot mod through overrides
	 * @param e message received from user
	 * @return whether or not user is a mod or not
	 */
	private boolean isMod(MessageReceivedEvent e){
		//owner is always a mod
		if(e.getAuthor().getId().equals(config.getOwnerID()))return true;
		try{
			//test for generic permissions that probably indicate a mod
			List<Role> roles=e.getMember().getRoles();
			for(Role r:roles){
				if(r.hasPermission(modPermissions)){
					return true;
				}
			}
			//test if someone has specifically set person as a mod on a guild
			for(String s:config.getGuildConfig(e.getGuild().getId()).getModded()){
				if(e.getAuthor().getId().equals(s)){
					return true;
				}
			}
		}
		catch(Exception e1){
		}
		return false;
	}
	private boolean CommandEnabled(MessageReceivedEvent event, String command){
		if(event.getChannelType()==ChannelType.PRIVATE)return true;
		ModuleController ctrl=config.getGuildConfig(event.getGuild()).getController(modules.get(command));
		if(ctrl==null)return true;
		return ctrl.enabled(event.getChannel().getId());
	}
}
