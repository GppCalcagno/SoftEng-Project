package it.polimi.ingsw.producible;

import it.polimi.ingsw.player.Player;

public class FaithMarker implements Producible {

    /**
     * this method activates the effect of the resource: increases the player's faithMarker
     * @param p is the player who uses the Producible!
     */
    public void effect(Player p) {
        p.increasefaithMarker();
    }

    @Override
    public String tostring() {
        return "FaithMarker";
    }
}
