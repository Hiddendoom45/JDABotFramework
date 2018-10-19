package JDABotFramework.global;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.google.gson.Gson;

/**
 * Handles various things that are global to machine instance i.e. executor
 * @author Allen
 *
 */
public class GlobalBot {
	/**
	 * Executor used by anything that needs multithreading
	 */
	public static ScheduledExecutorService executor = Executors.newScheduledThreadPool(30);
	/**
	 * Global Gson object, with default parameters, to avoid constant object initalization on serialization stuffs
	 */
	public static Gson gson = new Gson();
}
