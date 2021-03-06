package JDABotFramework.storage;

import java.util.List;
import java.util.Optional;

/**
 * Interface with w/e is storing data
 * A Key-Value based storage source, can simply be a map that is serialized or a K-V database.
 * @author Allen
 *
 */
public interface KeyStorageInt extends SyncStorageInt{
	
	/**
	 * Gets the list of all keys in the storageInt
	 * @return
	 */
	public List<String> getKeySet();
	
	/**
	 * Gets a string value from the storage source
	 * @param key
	 * @return
	 */
	public String getString(String key);
	
	/**
	 * Puts a string value in the storage source
	 * @param key
	 * @param value
	 */
	public void setString(String key,String value);
	/**
	 * Gets a long value from the storage source
	 * @param key
	 * @return
	 */
	public Optional<Long> getLong(String key);
	/**
	 * Puts a long value in the storage source
	 * @param key
	 * @param value
	 */
	public void setLong(String key,long value);
	/**
	 * Unsets the value associated with the key
	 * @param key
	 */
	public void unsetKey(String key);
	/**
	 * If the keystorage already contains the key or not
	 * @param key
	 * @return
	 */
	default boolean containsKey(String key){
		return getString(key)==null;
	}
	/**
	 * Push / export to any external cloud saves if any exists
	 */
	public boolean push();
	/**
	 * Update / pull from any external cloud saves if any exists
	 */
	public boolean pull();

}
