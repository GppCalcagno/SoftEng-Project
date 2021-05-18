package it.polimi.ingsw.View.Cli;

import it.polimi.ingsw.Client.ClientController;
import it.polimi.ingsw.Client.PlayerBoard;
import it.polimi.ingsw.Network.Message.ClientMessage.*;
import it.polimi.ingsw.View.Cli.Structure.*;
import it.polimi.ingsw.View.ViewInterface;

import java.io.PrintStream;


public class Cli implements ViewInterface {
    private PlayerBoard playerBoard;
    private final PrintStream out;
    private ClientController controller;
    private InputReader input;
    private String line;

    /**
     * @param playerBoard playerìs board
     * @param controller client controller
     */
    public Cli(PlayerBoard playerBoard, ClientController controller) {
        this.playerBoard = playerBoard;
        this.controller = controller;
        out = System.out;
        input = new InputReader();
        gameStart();
    }

    /**
     * this method calls InputReader and read the input inserted by the player
     */
    @Override
    public void inputFromPlayer() {
        input.run();
        line = input.getLine();
    }

    /**
     * when a player connect, the server ip and the port are requested in order to set the connection with the server
     */
    @Override
    public void askServerInfo() {
        out.println("\n");
        out.println("Please enter server IP [default: 127.0.0.1]: ");
        inputFromPlayer();
        String serverAddress = line;
        out.println("\n");
        out.println("Please enter server port [default : 1234]: ");
        inputFromPlayer();
        int serverPort = Integer.parseInt(line);
        controller.sendMessage(new MessageConnect(serverAddress, serverPort));
    }

    /**
     * the nickname of the player
     */
    @Override
    public void askNickname() {
        out.println("\n");
        out.println("Please enter your nickname: ");
        inputFromPlayer();
        String nickname = line;
        out.println("Welcome : " + nickname);
        controller.sendMessage(new MessageLogin(nickname));
    }

    /**
     * how many players want to play. this is decided by the first player that connects himself to the server
     */
    @Override
    public void askNumPlayer() {
        out.println("\n");
        out.println("Please enter the number of players: ");
        inputFromPlayer();
        int numPlayers = Integer.parseInt(line);
        controller.sendMessage(new MessageNumPlayers(playerBoard.getNickname(), numPlayers));
    }

    /**
     * show the start of the game
     */
    @Override
    public void gameStart(){
        ViewStart viewstart = new ViewStart();
    }

    /**
     * before starting the game every player choose two cards through four
     */
    @Override
    public void askChooseLeaderCards() {
        showLeaderActionBox();
        out.println("Please enter which two cards you want keep during the game: [0-3]");
        inputFromPlayer();
        int i1 = Integer.parseInt(line);
        inputFromPlayer();
        int i2 = Integer.parseInt(line);
        controller.sendMessage(new MessageChooseLeaderCards(playerBoard.getNickname(), i1, i2));
    }

    /**
     * this method received the input from the player and parse it to ActionParser in order to call the right method for
     * the turn that player chose
     */
    @Override
    public void doAction(){
        while (true){
            inputFromPlayer();
            ActionParser actionParser = new ActionParser(this, controller, playerBoard, line);
        }
    }

    /**
     * this method print the message it receives
     * @param message
     */
    @Override
    public void showMessage(String message) {
        out.println("\n");
        out.println(message);
    }

    @Override
    public void showLeaderActionBox() {
        out.println("\n");
        ViewLeaderActionBox viewLeaderActionBox = new ViewLeaderActionBox(playerBoard);
        viewLeaderActionBox.plot();
    }

    @Override
    public void showSlotDevCard() {
        out.println("\n");
        ViewSlotDevCard viewSlotDevCard = new ViewSlotDevCard(playerBoard);
        viewSlotDevCard.plot();
    }

    @Override
    public void showWarehouse() {
        out.println("\n");
        ViewWarehouse viewWarehouse = new ViewWarehouse(playerBoard);
        viewWarehouse.plot();
    }

    @Override
    public void showStrongbox() {
        out.println("\n");
        ViewStrongbox viewStrongbox = new ViewStrongbox(playerBoard);
        viewStrongbox.plot();
    }

    @Override
    public void showFaithTrack() {
        out.println("\n");
        ViewFaithTrack viewFaithTrack = new ViewFaithTrack(playerBoard);
        viewFaithTrack.plot();
    }

    @Override
    public void showMarketTray() {
        out.println("\n");
        ViewMarketTray viewMarketTray = new ViewMarketTray(playerBoard);
        viewMarketTray.plot();
    }

    @Override
    public void showMarbleBuffer() {

    }

    @Override
    public void showDevCardDeck() {
        out.println("\n");
        ViewDevCardDeck viewDevCardDeck = new ViewDevCardDeck(playerBoard);
        viewDevCardDeck.plot();
    }

    @Override
    public void showExtraChest(){
        out.println("\n");
        ViewExtraChest viewExtraChest = new ViewExtraChest(playerBoard);
        viewExtraChest.plot();
    }

    @Override
    public void showDevCard(String ID) {
        playerBoard.searchDevCard(ID);
    }

    @Override
    public void showLeaderAction(String ID) {
        System.out.println(playerBoard.searchLeaderCard(ID));
    }

    @Override
    public void showWinnerandVictoryPoint() {
        System.out.println("The winner is : " + playerBoard.getPlayerWinner() + "with : " + playerBoard.getPlayersPoints().get(playerBoard.getPlayerWinner()) + " points!");
    }

    @Override
    public void showLorenzoTrun() {
        System.out.println(playerBoard.getLastTokenUsed());
    }

    /**
     * this method print the allowed command that the player can write from his cli
     */
    public void help(){
        System.out.println("Here the commands you can insert during the game: " +
                "\n extractionmarble <row or column (r/c)> < index of r/c>" +
                "\n managemarble <W/E/D (warehouse,extrachest,discard)> (optional)<index of W> <resources wanted if changeWitheMarble is activated>" +
                "\n exchangewarehouse <row1> <row2>" +
                "\n buydevcard <id card> <position where store the card in slotdevcard>" +
                "\n payresources <W/S/E (warehouse/strongbox/extrachest)> <resource> <how many>" +
                "\n activebaseproduction <resource wanted> <W/S/E> <resource paied> <W/S/E> <resource paied>" +
                "\n activeleaderaction <id card> <W/S/E> <resource>" +
                "\n activedevcardproduction <id card>" +
                "\n updateleadercard <id card> <0 discard 1 active>" +
                "\n endturn" +
                "\n endproduction" +
                "\n show <structure to show>" +
                "\n help ");
    }
}
