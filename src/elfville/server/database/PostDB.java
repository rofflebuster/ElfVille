package elfville.server.database;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import elfville.server.SecurityUtils;
import elfville.server.model.*;

public class PostDB extends DB {
	private final ConcurrentHashMap<Integer, Post> idMap = new ConcurrentHashMap<Integer, Post>();
	private final ConcurrentHashMap<Integer, Post> centralPosts = new ConcurrentHashMap<Integer, Post>();

	// private final List<Post> centralPosts = Collections.synchronizedList(new
	// ArrayList<Post>());

	public void insert(Post post) {
		// if (!hasModel(post)) {
		idMap.put(post.getModelID(), post);
		if (post.clanID == 0)
			centralPosts.put(post.getModelID(), post);
		// }
	}

	public void delete(int i) {
		Post p = idMap.get(i);
		idMap.remove(i);
		if (p.clanID == 0) {
			centralPosts.remove(p.getModelID());
		}
	}

	public boolean hasModel(Post post) {
		return idMap.containsKey(post.getModelID());
	}

	public Post findByModelID(int modelID) {
		return idMap.get(modelID);
	}

	public Post findByEncryptedModelID(String encID) {
		int modelID = SecurityUtils.decryptStringToInt(encID);
		return findByModelID(modelID);
	}

	// Only the elf's posts on the central board will be returned
	public List<Post> findCentralPostsByElf(Elf elf) {
		List<Post> posts = new ArrayList<Post>();
		for (Post post : centralPosts.values()) {
			if (post.getElf().equals(elf)) {
				posts.add(post);
			}
		}
		return posts;
	}

	/**
	 * Returns a list of central board posts sorted by socks.
	 * 
	 * @return
	 */
	public List<Post> getCentralPosts() {
		List<Post> posts = new ArrayList<Post>();
		for (Post p : centralPosts.values()) {
			posts.add(p);
		}
		Collections.sort(posts);
		return posts;
	}

}
