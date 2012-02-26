package elfville.client.views.subcomponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;

import elfville.client.ClientWindow;
import elfville.client.SocketController;
import elfville.client.views.Refreshable;
import elfville.protocol.Response;
import elfville.protocol.VoteRequest;
import elfville.protocol.models.SerializablePost;

public class VotablePost extends Post {
	private static final long serialVersionUID = 1L;
	private final Refreshable board;
	
	private class VoteHandler implements ActionListener {
		private boolean upsock;
		private String postID;

		public VoteHandler(String postID, boolean upsock, Post component) {
			this.upsock = upsock;
			this.postID = postID;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			VoteRequest req = new VoteRequest(postID, upsock);
			try {
				Response resp = SocketController.send(req);
				if (!resp.isOK()) {
					System.err.println("Did not vote!");
				} else {
					board.refresh();
				}
			} catch (IOException e1) {
				ClientWindow.showConnectionError();
			}
		}
	}

	public VotablePost(SerializablePost p, Refreshable board) {
		super(p);
		// TODO have some indication of whether (and how) the user has voted on
		// this post.
		this.board = board;
		JButton upvote = new JButton("Likes: " + Integer.toString(p.upvotes));
		JButton downvote = new JButton("Dislikes: " + Integer.toString(p.downvotes));
		upvote.addActionListener(new VoteHandler(p.modelID, true, this));
		downvote.addActionListener(new VoteHandler(p.modelID, false, this));
		add(upvote);
		add(downvote);
	}

}
