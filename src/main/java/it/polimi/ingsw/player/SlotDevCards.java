package it.polimi.ingsw.player;

import it.polimi.ingsw.card.DevelopmentCard;
import it.polimi.ingsw.card.leadereffect.ExtraProduction;
import it.polimi.ingsw.producible.Producible;
import it.polimi.ingsw.producible.Resources;

import java.util.ArrayList;
import java.util.List;

public class SlotDevCards {


    /** this is the matrix where the development cards are located
     */
    private DevelopmentCard[][] boardDevCards;

    /** this buffer is for avoid the possibility that resources just producted can be used as resources needed
     * for the production
     */
    private List<Producible> buffer;

    /** this is a list of optional that if the player hasn't the leader card that allows to discount the price
     * of productions aren't initialized
     */
    private ExtraProduction leaderCardEffect;

    /** the constructor initialize the matrix as 3*3 as written in the rules even though the player can't
     * buy more than seven cards
     */
    public SlotDevCards() {
        boardDevCards = new DevelopmentCard[3][3];
        buffer = new ArrayList<Producible>();
    }

    /** this method check if the player can buy a specific level card from the development card market
     * @return the maximum level of player's cards
     * @param d the card received that the method check if the player can buy it just for the level, not for the resources
     */
    public boolean maxLevelPurchase(DevelopmentCard d){
        boolean b = false;

        for(int j=0; j<boardDevCards.length; j++){
            if(boardDevCards[d.getLevel()][j]==null && (d.getLevel()==0 || boardDevCards[d.getLevel()-1][j]!=null)) b=true;
        }
        return b;
    }

    /** when player buy a development card this method insert the card in the boardCards
     * @param column the column where player wants put the card just bought in accord with the rules
     * @param card the card just bought
     */
    public boolean insertCards( int column, DevelopmentCard card){
        if(card.getLevel()!=0 && boardDevCards[card.getLevel()-1][column]==null || column>boardDevCards.length || card.getLevel()>boardDevCards.length) return false;
        else{
            boardDevCards[card.getLevel()][column] = card;
            return true;
        }
    }

    /** this method check if the card selected from the player to be used is on the top of the heap
     * @param card the card that player selected
     */
    public boolean checkUsage(DevelopmentCard card){
        int i=0;
        boolean c = false;
        if(card.getLevel() == boardDevCards.length-1) return true;
        while (boardDevCards[card.getLevel()][i] != card){
            i++;
        }
        if(boardDevCards[card.getLevel()+1][i]==null) return true;
        else return false;
    }

    /** constant for each board, is the base production
     * @param wanted the resources wanted, it is added to the buffer
     */
    //le risorse da eliminare le elimina il controller
    public void baseProduction(Resources wanted){
        buffer.add(wanted);
    }

    /** this method call the card and start the production
     * @param card the card whose production player want to start
     */
    public void cardProduction(DevelopmentCard card){
        buffer.add((Producible) card.getProductedResources());
    }

    /** this method empty the buffer into the strongbox
     */
    //da vedere
    public Resources[] emptyBuffer(){
        Resources[] t = new Resources[buffer.size()];
        buffer.toArray(t);
        buffer.clear();
        return t;
    }

    /**
     * this method active the card leader extra production
     * @param r the resources wanted from the extra production.
     */
    public void addLeaderCardEffect(Resources r){

    }

    /**
     * a get from the boardDevCards
     * @param x row
     * @param y column
     * @return
     */
    public DevelopmentCard getDevCards(int x, int y){
        return  boardDevCards[x][y];

    }

    public void addleaderCardEffect(Resources resources) {
    }
}