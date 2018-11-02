package JDABotFramework.wrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class EmoteData {
	final String tag;
	final String strValue;
	final String emoteValue;
	private static Pattern valPattern = Pattern.compile("<:(.*?):(\\d{18})>");
	private transient Matcher m;
	/**
	 * Basic data to format emotes {@link JDAMessage}
	 * @param tag unique string that will be replaced by either strAlt or emoteValue when formatted with {@link JDAMessage#EmoteMessage(MessageReceivedEvent, String)}
	 * @param strAlt string describing emote, used if bot does not have permissions to use emotes
	 * @param emoteValue string for emote
	 */
	public EmoteData(String tag,String strAlt,String emoteValue){
		this.tag=tag;
		this.strValue=strAlt;
		m = valPattern.matcher(emoteValue);
		if(!m.matches()){
			throw new IllegalArgumentException("Emote value is not in the proper format for a custom emote <:[name]:[id]>");
		}
		this.emoteValue=emoteValue;
	}
	/**
	 * Basic data to format emotes {@link JDAMessage}
	 * @param tag unique string that will be replaced by either strAlt or the emote when formatted with {@link JDAMessage}
	 * @param strAlt alternate Name for the emote, used if the server does not allow emotes
	 * @param id id for the emote
	 * @param emoteName name of the emote
	 */
	public EmoteData(String tag,String strAlt,String id,String emoteName){
		this.tag=tag;
		this.strValue=strAlt;
		emoteValue = "<:"+emoteName+":"+id+">";
		m = valPattern.matcher(emoteValue);
		if(!m.matches()){
			throw new IllegalArgumentException("Emote value is not in the proper format for a custom emote <:[name]:[id]>");
		}
	}
	/**
	 * Get the id of the emote
	 * @return
	 */
	public String getID(){
		if(m==null){
			m = valPattern.matcher(emoteValue);
			m.matches();
		}
		return m.group(2);
	}
	/**
	 * Get the name of the emote
	 * @return
	 */
	public String getEmoteName(){
		if(m==null){
			m = valPattern.matcher(emoteValue);
			m.matches();
		}
		return m.group(1);
	}
}
