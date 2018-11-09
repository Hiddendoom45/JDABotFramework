package JDABotFramework.storage;

import java.sql.Connection;

public interface DBStorageInt extends SyncStorageInt{
	/**
	 * Get the connection used to interact with the database
	 * @return
	 */
	public Connection getConnection();
	
	/**
	 * Push / export to any external cloud saves if any exists
	 */
	public boolean push();
	/**
	 * Update / pull from any external cloud saves if any exists
	 */
	public boolean pull();
}
