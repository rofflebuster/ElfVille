package elfville.server.model;

import java.util.List;

import elfville.protocol.models.SerializableElf;
import elfville.server.Database;

/*
 * Elf Model.
 */
public class Elf extends Model implements Comparable<Elf> {
	private static final long serialVersionUID = 4948830835289818367L;
	private final String name;
	private String description;

	public Elf(String name) {
		super();
		this.name = name;
	}

	public List<Post> getPosts() {
		return Database.getInstance().postDB.findCentralPostsByElf(this);
	}

	public SerializableElf toSerializableElf() {
		SerializableElf elf = new SerializableElf();
		elf.elfName = name;
		elf.modelID = getEncryptedModelID();
		elf.numSocks = getNumSocks();
		elf.description = description;
		return elf;
	}

	// the number of socks in all posts owned by the elf combined
	public int getNumSocks() {
		int numPost = 0;
		for (Post post : getPosts()) {
			numPost += post.getNumSock();
		}
		return numPost;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public void save() {
		super.save();
		Database.getInstance().elfDB.add(this);
	}

	public static Elf get(int id) {
		return Database.getInstance().elfDB.findByID(id);
	}

	@Override
	public int compareTo(Elf other) {
		return name.compareTo(other.name);
	}
	
	public void setDescription(String description) {
		this.description= description;
	}
}
