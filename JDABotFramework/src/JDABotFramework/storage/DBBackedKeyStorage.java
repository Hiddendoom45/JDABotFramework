package JDABotFramework.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;

/**
 * Something that uses a database backend acting as a key storage.
 * Multithread safe.
 * @author Allen
 *
 */
public class DBBackedKeyStorage implements KeyStorageInt{
	//connection to the database
	private Connection dbConnection;
	private String table;
	private final PreparedStatement get;
	private final PreparedStatement set;
	private final PreparedStatement del;
	private final PreparedStatement iter;
	private BooleanSupplier push = () -> {return false;};
	private BooleanSupplier pull = () -> {return false;};
	
	/**
	 * Creates a KeyStorageInt backed by a database
	 * @param conn connection to the database
	 * @param tableName name for the table to use
	 */
	public DBBackedKeyStorage(Connection conn,String tableName){
		this.dbConnection = conn;
		if(validateTableName(tableName.trim())){
			this.table = tableName.trim();
		}
		else{
			throw new IllegalArgumentException("invalid tablename");
		}
		PreparedStatement get;
		PreparedStatement set;
		PreparedStatement del;
		PreparedStatement iter;
		try{
			get = dbConnection.prepareStatement("SELECT (KEY,VALUE) FROM "+table+" WHERE KEY = ?");
			set = dbConnection.prepareStatement("MERGE INTO "+table+" (KEY, VALUE) KEY(KEY) VALUES(?,?) ");
			del = dbConnection.prepareStatement("DELETE FROM "+table+" WHERE KEY = ?");
			iter = dbConnection.prepareStatement("SELECT KEY FROM "+table);
			dbConnection.createStatement().execute("CREATE TABLE IF NOT EXISTS "+table+" (KEY VARCHAR PRIMARY KEY, VALUE VARCHAR)");
		}catch(SQLException e){
			throw new IllegalArgumentException(e);
		}
		this.get = get;
		this.set = set;
		this.del = del;
		this.iter = iter;
	}
	/**
	 * Creates a KeyStorageInt backed by a database
	 * @param db database behind this
	 * @param tableName name for the table to use
	 */
	public DBBackedKeyStorage(DBStorageInt db, String tableName){
		this(db.getConnection(),tableName);
		push = () ->{
			return db.push();
		};
		pull = () ->{
			return db.pull();
		};
	}
	private boolean validateTableName(String name){
		//alphanumeric with dot portion in the case of schema
		return Pattern.compile("[A-Za-z0-9][A-Za-z0-9_]*($|\\.[A-Za-z0-9][A-Za-z0-9_]*)").matcher(name).matches();
	}
	@Override
	public List<String> getKeySet(){
		ArrayList<String> keys = new ArrayList<String>();
		try{
			ResultSet keySet = iter.executeQuery();
			while(keySet.next()){
				keys.add(keySet.getString(1));
			}
		}catch(SQLException e){
		}
		return keys;
	}
	@Override
	public String getString(String key) {
		try{
			get.setString(1, key);
			ResultSet rs = get.executeQuery();
			if(rs.next()){
				return rs.getString(2);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setString(String key, String value) {
		try{
			set.setString(1, key);
			set.setString(2, value);
			set.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public Optional<Long> getLong(String key) {
		try{
			get.setString(1, key);
			ResultSet rs = get.executeQuery();
			if(rs.next()){
				return Optional.of(Long.parseLong(rs.getString(2)));
			}
		}catch(SQLException|NumberFormatException e){
			e.printStackTrace();
		}
		return Optional.empty();
	}

	@Override
	public void setLong(String key, long value) {
		try{
			set.setString(1, key);
			set.setLong(2, value);
			set.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public void unsetKey(String key) {
		try{
			del.setString(1, key);
			del.execute();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean push() {
		return push.getAsBoolean();
	}

	@Override
	public boolean pull() {
		return pull.getAsBoolean();
	}
}
