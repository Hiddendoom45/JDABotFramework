package JDABotFramework.util.command.override;

/**
 * Details of each override key
 * @author Allen
 *
 */
public class OverrideKey {
	protected final String value;
	protected final long duration;//in miliseconds
	protected OverrideKey(String value, long duration){
		this.value = value;
		this.duration = duration;
	}
	//override to make sure equals matches properly
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OverrideKey other = (OverrideKey) obj;
		if (duration != other.duration)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
}
