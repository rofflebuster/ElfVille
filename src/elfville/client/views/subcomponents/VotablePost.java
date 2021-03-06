package elfville.client.views.subcomponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import elfville.client.views.Board;
import elfville.protocol.VoteRequest;
import elfville.protocol.models.SerializablePost;

/**
 * Not only shows a post, but also shows two buttons that allow the user to vote
 * on the merits of the post.
 * 
 */
public class VotablePost extends Post {
	private static final long serialVersionUID = 1L;
	private final Board board;

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
			board.getSocketController().sendRequest(req, board,
					"Did not vote!", null);
		}
	}

	public VotablePost(Board board, SerializablePost p) {
		super(board, p, null);
		// TODO have some indication of whether (and how) the user has voted on
		// this post.
		this.board = board;
		JButton upvote = new JButton("Upsocks: " + Integer.toString(p.upvotes));
		JButton downvote = new JButton("Downsocks: "
				+ Integer.toString(p.downvotes));
		upvote.addActionListener(new VoteHandler(p.modelID, true, this));
		downvote.addActionListener(new VoteHandler(p.modelID, false, this));
		add(upvote);
		add(downvote);
	}

}
