package JDABotFramework.util.command;

import java.util.ArrayList;

import JDABotFramework.global.config.BotGlobalConfig;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandParser {
	private final BotGlobalConfig config;
	public CommandParser(BotGlobalConfig config){
		this.config=config;
	}
	public CommandContainer parse(String rw,MessageReceivedEvent e){
		ArrayList<String> split=new ArrayList<String>();
		String raw= rw;
		String modPrefix = config.getModPrefix(e.getGuild());
		String prefix = config.getPrefix(e.getGuild());
		boolean isModCmd=raw.startsWith(modPrefix);
		String beheaded;
		if(isModCmd){
			beheaded=raw.replaceFirst(replaceMeta(modPrefix), "");
		}
		else{
			beheaded=raw.replaceFirst(replaceMeta(prefix), "");
		}
		String[] splitbeheaded=beheaded.split(" ");
		for(String s:splitbeheaded){
			split.add(s);
		}
		String invoke=split.get(0);
		String[] args=new String[split.size()-1];
		split.subList(1, split.size()).toArray(args);
		
		return new CommandContainer(raw,beheaded,splitbeheaded, invoke.toLowerCase(), args, e,isModCmd);
	}
	private static String replaceMeta(String s){
		s="\\Q"+s+"\\E";//in case some meta characters are in the prefix string
		return s;
	}
	public class CommandContainer{
		public final String raw;
		public final String beheaded;
		public final String[] splitbeheaded;
		public final String invoke;
		public final String[] args;
		public final boolean isModCmd;
		public final MessageReceivedEvent e;
		
		public CommandContainer(String rw,String beheaded,String[] splitbeheaded, String invoke,String[] args,MessageReceivedEvent e,boolean isModCmd){
			this.raw=rw;
			this.beheaded=beheaded;
			this.splitbeheaded=splitbeheaded;
			this.invoke=invoke;
			this.args=args;
			this.e=e;
			this.isModCmd=isModCmd;
		}
	}
}
