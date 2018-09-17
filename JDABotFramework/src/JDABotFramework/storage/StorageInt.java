package JDABotFramework.storage;

import java.util.Optional;

/**
 * Interface with w/e is storing data
 * Semi based on databases but with a few extra methods to account for cloud saving the data etc.
 * @author Allen
 *
 */
public interface StorageInt {
	
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
	 * Push / export to any external cloud saves if any exists
	 */
	public void push();
	/**
	 * Update / pull from any external cloud saves if any exists
	 */
	public void pull();

}
