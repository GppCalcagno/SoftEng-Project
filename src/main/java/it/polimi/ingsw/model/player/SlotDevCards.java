package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.leadereffect.ExtraProduction;
import it.polimi.ingsw.model.exceptions.GameFinishedException;
import it.polimi.ingsw.model.producible.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlotDevCards {


    /** this is the matrix where the development cards are located
     */
    private DevelopmentCard[][] boardDevCards;

    /** this buffer is for avoid the possibility that resources just producted can be used as resources needed
     * for the production
     */
    private Map<String,Integer> buffer;

    /** this is a list of optional that if the player hasn't the leader card that allows to discount the price
     * of productions aren't initialized
     */
    private List<ExtraProduction> leaderCardEffect;

    /** the constructor initialize the matrix as 3*3 as written in the rules even though the player can't
     * buy more than seven cards
     */
    public SlotDevCards() {
        boardDevCards = new DevelopmentCard[3][3];
        buffer = new HashMap<>();
        leaderCardEffect= new ArrayList<>();
    }

    /** this method check if the player can buy a specific level card from the development card market
     * @return the maximum level of player's cards
     * @param d the card received that the method check if the player can buy it just for the level, not for the resources
     */
    public boolean maxLevelPurchase(DevelopmentCard d){
        boolean b = false;

        for(int j=0; j<boardDevCards.length; j++){
            if(boardDevCards[d.getLevel()-1][j]==null && (d.getLevel()-1==0 || boardDevCards[d.getLevel()-2][j]!=null)) b=true;
        }
        return b;
    }

    /** when player buy a development card this method insert the card in the boardCards
     * @param column the column where player wants put the card just bought in accord with the rules
     * @param card the card just bought.
     */
    public boolean insertCards( int column, DevelopmentCard card) throws GameFinishedException {
        if((card.getLevel()-1)!=0 && (boardDevCards[card.getLevel()-2][column]==null || column>boardDevCards.length || (card.getLevel()-1)>boardDevCards.length)) return false;
        else{
            if(boardDevCards[card.getLevel()-1][column]!=null) return false;
            else {
                boardDevCards[card.getLevel() - 1][column] = card;
                if (countTotalNumberDevCards() == 7)
                    throw new GameFinishedException();
                return true;
            }
        }
    }

    /** this method check if the card selected from the player to be used is on the top of the heap
     * @param card the card that player selected
     */
    public boolean checkUsage(DevelopmentCard card){
        int i=0;
        if(card.getLevel()-1 == boardDevCards.length-1) return true;
        while (boardDevCards[card.getLevel()-1][i] != card){
            i++;
        }
        return boardDevCards[card.getLevel()][i] == null;
    }

    /** constant for each board, is the base production
     * @param wanted the resources wanted, it is added to the buffer
     */
    //le risorse da eliminare le elimina il controller
    public void baseProduction(Resources wanted){
        if(buffer.containsKey(wanted.toString())){
            buffer.put(wanted.toString(),buffer.get(wanted.toString())+1);
        }
        else{
            buffer.put(wanted.toString(),1);
        }

    }

    /** constant for each board, is the base production
     * @param wanted the resources wanted, it is added to the buffer
     */
    //le risorse da eliminare le elimina il controller
    public void baseProduction(String wanted){
        if(buffer.containsKey(wanted)){
            buffer.put(wanted,buffer.get(wanted)+1);
        }
        else{
            buffer.put(wanted.toString(),1);
        }
    }

    /** this method call the card and start the production
     * @param card the card whose production player want to start
     */
    public void cardProduction(DevelopmentCard card){
        for(String Res: card.getProductedResources().keySet()){
            if(buffer.containsKey(Res)){
                buffer.put(Res,buffer.get(Res)+card.getProductedResources().get(Res));
            }
            else{
                buffer.put(Res,card.getProductedResources().get(Res));
            }
        }
    }


    /**
     * this method active the card leader extra production
     * @param resources the resources wanted from the extra production.
     */
    public void addLeaderCardEffect(Resources resources){
        leaderCardEffect.add(new ExtraProduction(resources));
    }

    /**
     * this method is a get from the boardDevCards
     * leave an NullPointerException exception if the slot is empty
     * @param x row
     * @param y column
     * @return the corresponding DevelopmentCard
     */
    public DevelopmentCard getDevCards(int x, int y) {
        return  boardDevCards[x][y];
    }

    public DevelopmentCard getDevCards(int col)throws NullPointerException {
        int i=2;
        while(i>-1 && boardDevCards[i][col]==null)i--;
        if(i<0) throw new NullPointerException();
        return boardDevCards[i][col];
    }

    /**
     * This method counts the total number of victory points from all the Development cards in SlotDevCards
     * @return the total number of victory points from all Development cards
     */
    public int countVictoryPoints(){
        int slotDevCardsVictoryPoints = 0;
        for(DevelopmentCard[] rowCard : boardDevCards){
            for(DevelopmentCard card : rowCard){
                if(card != null)
                    slotDevCardsVictoryPoints += card.getVictoryPoints();
            }
        }
        return slotDevCardsVictoryPoints;
    }

    /**
     * This method counts the total number of Development cards in SlotDevCards
     * @return the number, as an int, of all Development cards in SlotDevCards
     */
    public int countTotalNumberDevCards(){
        int totalNumberDevCards = 0;
        for(DevelopmentCard[] rowCards : this.boardDevCards){
            for (DevelopmentCard developmentCard : rowCards){
                if(developmentCard != null)
                    totalNumberDevCards++;
            }
        }
        return totalNumberDevCards;
    }

    public List<ExtraProduction> getLeaderCardEffect(){
        return leaderCardEffect;
    }

    /**
     * Getter of buffer;
     * @return product of the effect of the cards
     */
    public Map<String, Integer> getBuffer() { return buffer; }

    public DevelopmentCard[][] getBoardDevCards() {
        return boardDevCards;
    }
}
