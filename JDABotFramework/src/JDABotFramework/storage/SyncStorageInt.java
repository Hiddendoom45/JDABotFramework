package JDABotFramework.storage;
/**
 * Generic interface for anything that needs to be synchronized with an external source
 * @author Allen
 *
 */
public interface SyncStorageInt {
	
	/**
	 * Pulls from source
	 * @return true if success
	 */
	public boolean pull();
	/**
	 * Pushes to source
	 * @return true if success
	 */
	public boolean push();

}
