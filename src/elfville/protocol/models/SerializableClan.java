package elfville.protocol.models;

import java.util.ArrayList;
import java.util.List;

public class SerializableClan extends SerializableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String clanName;
	public String clanDescription;
	public int numSocks;
	public SerializableElf leader;
	public List<SerializableElf> members;
	public List<SerializablePost> posts;
	public String clanID;

	public SerializableClan() {
		super();
		members = new ArrayList<SerializableElf>();
		posts = new ArrayList<SerializablePost>();
	}

}