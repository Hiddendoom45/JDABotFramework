package JDABotFramework.global;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
}
