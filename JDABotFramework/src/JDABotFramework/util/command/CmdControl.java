package JDABotFramework.util.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import JDABotFramework.global.config.BotGlobalConfig;
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
	private final ArrayList<Permission> modPermissions = new ArrayList<Permission>();
	public final CommandParser parser=new CommandParser(null,null);//parse most commands
	private HashMap<String,Command> commands=new HashMap<String,Command>();
	private HashMap<String,Command> modCommands=new HashMap<String,Command>();
	private HashMap<String,String> modules=new HashMap<String,String>();//used to get which module the command is from
	private BotGlobalConfig config;
	public CmdControl(BotGlobalConfig config){
		this.config = config;
	}
	public boolean parseCommands(MessageReceivedEvent event){
		if(event.getAuthor().getId().equals(config.getSelfID()))return false;
		String content=event.getMessage().getContent();
		if(content.startsWith(config.getPrefix())){
			CommandParser.CommandContainer cmd=parser.parse(content, event);
			if(CommandEnabled(event,cmd.invoke)){
				return handleCommand(parser.parse(content, event));
			}
		}
		if(content.startsWith(config.getModPrefix())){
			return handleCommand(parser.parse(content, event));
		}
		return false;
	}
	public void commandAction(MessageReceivedEvent event, String command, String[] args){
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
	private  boolean handleCommand(CommandParser.CommandContainer cmd){
		System.out.println(cmd.invoke);
		if(commands.containsKey(cmd.invoke)&&!cmd.isModCmd){
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
		else if(modCommands.containsKey(cmd.invoke)&&cmd.isModCmd){
			if(!isMod(cmd.e)){
				JDAMessage.sendMessage(cmd.e, ":no_entry_sign: You are not authorized to use mod commands here");
			}
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
		else if(cmd.invoke.equals("help")){
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
		try{
			List<Role> roles=e.getMember().getRoles();
			for(Role r:roles){
				if(r.hasPermission(modPermissions)){
					return true;
				}
			}
			for(String s:config.getGuildConfig(e.getGuild().getId()).getModded()){
				if(e.getAuthor().getId().equals(s)){
					return true;
				}
			}
		}
		catch(Exception e1){
			e1.printStackTrace();
		}
		
		if(e.getAuthor().getId().equals(config.getOwnerID()))return true;
		return false;
	}
	private boolean CommandEnabled(MessageReceivedEvent event, String command){
		if(event.getChannelType()==ChannelType.PRIVATE)return true;
		return true;
		//TODO temp
//		Settings guild=SaveSystem.getGuild(event.getGuild().getId());
//		ModuleController ctrl=guild.disabled.get(modules.get(command));
//		if(ctrl==null)return true;
//		return ctrl.enabled(event.getChannel().getId());
	}
}
