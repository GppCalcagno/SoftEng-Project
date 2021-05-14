package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.ClientController;
import it.polimi.ingsw.Client.PlayerBoard;
import it.polimi.ingsw.Network.message.MessageChooseResourcesFirstTurn;
import it.polimi.ingsw.Network.message.MessageGeneric;
import it.polimi.ingsw.Network.message.MessageType;

import java.util.ArrayList;
import java.util.List;

public class ViewThread implements Runnable {
    private MessageType currentState;
    private ViewInterface view;
    private PlayerBoard board;
    private boolean active;

    public ViewThread(MessageType currentState, ViewInterface view, PlayerBoard board, ClientController clientController) {
        this.currentState = currentState;
        this.view = view;
        active=false;
        this.board = board;
    }

    @Override
    public void run(){

        while (!active) {
            try {

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                //da vedede
            }
        }
        switch (currentState){
            case CONNECT:  view.askServerInfo(); break;
            case LOGIN: view.askNickname(); break;
            case NUMPLAYERS: view.askNumPlayer(); break;
            case WAITINGOTHERPLAYERS: break;

            case CHOOSELEADERCARDS: view.askChooseLeaderCards(); break;
            case CHOOSERESOURCESFIRSTTURN: askChooseFirstResurces(); break;

            case CHOOSETURN: view.askChooseTurn(); break;

            case EXTRACTIONMARBLES:  view.askExtractMarble(); break;
            case CHOOSEAFTERTAKEMARBLE: view.askAfterTakeMarble(); break;
            case EXCHANGEWAREHOUSE:  view.askExchange(); break;
            case ADDDISCARDMARBLES: view.askAddDiscardMarble(); break;
            case SELECTTRANSFORMATIONWHITEMARBLE: view.askSelectTrasformationWhiteMarble(); break;

            case SELECTDEVCARD: view.askSelectDevCard(); break;
            case CHOOSERESOURCESPURCHASEDEVCARD: view.askChooseResourcesPurchaseDevCard(); break;
            case INSERTCARD: view.askInsertCard(); break;

            case CHOOSEPRODUCTIONTYPE: view.askProductionType(); break;
            case ACTIVESBASEPRODUCTION: view.askActiveBaseProduction(); break;
            case CHOSENRESOURCEBASEPRODUCTION: view.askChosenResourceBaseProduction(); break;
            case ACTIVEPRODUCTIONDEVCARD: view.askActiveProductionDevCard(); break;
            case CHOOSERESOURCESDEVCARDPRODUCTION: view.askChooseResourcesDevCardProduction(); break;
            case ACTIVELEADERCARDPRODUCTION: view.askActiveLeaderCardProduction(); break;
            case ENDPRODUCTION: view.sendEndProduction();

            case UPDATESTATELEADERACTION: view.askUpdateStateLeaderAction(); break;


            case ENDTURNACTIVELEADERCARD: view.askEndTurnActiveLeaderCard();  break;
            case ENDTURN:  view.endturn(); break;

            case UPDATEWINNER: view.showWinnerandVictoryPoint(); break;
        }
    }

    private void askChooseFirstResurces() {
        int position= board.getPlayerList().indexOf(board.getNickname());
        int num=1;
        if(position==0) num=0;
        if(position==3) num=2;
        view.askChooseResourcesFirstTurn(num);
    }

    public void setCurrentState(MessageType currentState) {
        this.currentState = currentState;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
