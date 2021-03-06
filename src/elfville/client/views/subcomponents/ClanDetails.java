package elfville.client.views.subcomponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import elfville.client.SocketController;
import elfville.client.views.ClanBoard;
import elfville.client.views.ClanDirectory;
import elfville.protocol.*;
import elfville.protocol.ModifyClanRequest.ModClan;
import elfville.protocol.models.SerializableClan;

/**
 * Provides a summary of the clan at the top of a clan page, including the name
 * of the clan, a link to the leader's page, the description of a clan, and a
 * button taht does different things depending on the user's relationship with
 * the clan. If the user is an outsider it allows them to ask to join. If the
 * user is an applicant (asked to join) it's greyed out and indicates that
 * they're still waiting for the leader's decision. If the user is the leader it
 * allows the leader to disband/delete the clan. if the user is a member it
 * allows the member to leave the clan.
 * 
 */
public class ClanDetails extends JPanel implements ActionListener,
		SocketController.SuccessFunction {
	private static final long serialVersionUID = 1L;
	// private final String clanModelID;
	private final SerializableClan clan;
	// private final String elfModelID;
	private final ModClan action;
	ClanBoard board;

	public ClanDetails(ClanBoard board, ClanBoardResponse response) {
		super();
		this.board = board;
		// elfModelID = response.
		// clanModelID = response.clan.modelID;
		clan = response.clan;

		JLabel title = new JLabel("Board of the " + response.clan.clanName);
		add(title);

		JButton leaderName = new JButton(response.clan.leader.elfName);
		leaderName.addActionListener(new ElfHandler(board,
				response.clan.leader.modelID));
		add(leaderName);

		String buttonLabel;
		if (response.elfStatus == ClanBoardResponse.ElfClanRelationship.OUTSIDER) {
			buttonLabel = "Request to Join";
			action = ModClan.APPLY;
		} else if (response.elfStatus == ClanBoardResponse.ElfClanRelationship.APPLICANT) {
			buttonLabel = "Application Processing";
			action = null;
		} else if (response.elfStatus == ClanBoardResponse.ElfClanRelationship.MEMBER) {
			buttonLabel = "Leave Clan";
			action = ModClan.LEAVE;
		} else if (response.elfStatus == ClanBoardResponse.ElfClanRelationship.LEADER) {
			buttonLabel = "Disband Clan";
			action = ModClan.DELETE;
		} else {
			buttonLabel = "Error";
			action = null;
		}

		JButton clanAction = new JButton(buttonLabel);

		if (buttonLabel.equals("Error")
				|| buttonLabel.equals("Application Processing")) {
			clanAction.setEnabled(false);
		} else {
			clanAction.addActionListener(this);
		}
		add(clanAction);

		JTextArea description = new JTextArea(response.clan.clanDescription);
		description.setEditable(false);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(description);
		JLabel label = new JLabel("Description");
		label.setLabelFor(scroll);
		add(label);
		add(scroll);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ModifyClanRequest req = new ModifyClanRequest(clan, action);
		board.getSocketController().sendRequest(req, board, "Action failed.",
				this);
	}

	@Override
	public void handleRequestSuccess(Response resp) {
		if (action == ModClan.LEAVE || action == ModClan.DELETE) {
			new ClanDirectory(board.getClientWindow(),
					board.getSocketController());
		} else {
			board.refresh();
		}
	}
}
