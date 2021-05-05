package it.polimi.ingsw.Network.message;

public class MessageAddDiscardLeaderCard extends Message {
    private static final long serialVersionUID = 834631015175905858L;

    /**
     * This attribute is 0 if the player wants to discard the leader card, 1 to add
     */
    private boolean addOrDiscard;

    private int pos;

    public MessageAddDiscardLeaderCard(String nickname, MessageType messageType, boolean addOrDiscard, int pos) {
        super(nickname, messageType);
        this.addOrDiscard = addOrDiscard;
        this.pos = pos;
    }

    public boolean isAddOrDiscard() {
        return addOrDiscard;
    }

    public int getPos() {
        return pos;
    }
}