package it.polimi.ingsw.Network.Message.UpdateMesssage;

import it.polimi.ingsw.Client.PlayerBoard;
import it.polimi.ingsw.Network.Message.Message;
import it.polimi.ingsw.Network.Message.MessageType;
import it.polimi.ingsw.View.ViewInterface;

import java.util.List;

public class MessageUpdateInitialLeaderCards extends Message {
    private static final long serialVersionUID = -8636343470594316197L;

    private List<String> leaderCardsID;

    public MessageUpdateInitialLeaderCards(String nickname, List<String> leaderCardsID) {
        super(nickname, MessageType.UPDATEINITIALLEADERCARDS);
        this.leaderCardsID = leaderCardsID;
    }

    @Override
    public void update(PlayerBoard playerBoard, ViewInterface view) {
        playerBoard.setLeaderCard(leaderCardsID);
        view.onUpdateInitialLeaderCards(leaderCardsID);
    }
}