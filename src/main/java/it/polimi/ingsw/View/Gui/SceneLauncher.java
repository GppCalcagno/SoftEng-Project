package it.polimi.ingsw.View.Gui;

import it.polimi.ingsw.Client.ClientController;
import it.polimi.ingsw.Client.PlayerBoard;
import it.polimi.ingsw.Network.Message.ClientMessage.*;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.*;

public class SceneLauncher {
    private ClientController controller;
    private PlayerBoard playerBoard;
    private Stage stage;

    public SceneLauncher(ClientController controller, PlayerBoard playerBoard) {
        this.controller = controller;
        this.playerBoard = playerBoard;
        this.stage=null;
    }

    public void setStage() {
        this.stage = new Stage();
    }

    public Stage getStage() {
        return stage;
    }

    public Scene askServerInfo(){
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(10);
        grid.setVgap(8);

        Label address = new Label("Server address: ");
        GridPane.setConstraints(address, 0, 0);

        TextField addressIn = new TextField("127.0.0.1");
        GridPane.setConstraints(addressIn, 1,0);

        Label port = new Label("Port: ");
        GridPane.setConstraints(port, 0,1);

        TextField portIn = new TextField("1234");
        GridPane.setConstraints(portIn, 1,1);

        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 2);
        loginButton.setOnAction(e->{

            Thread guiThread= new Thread(()->controller.connect(addressIn.getText(), Integer.parseInt(portIn.getText())));
            guiThread.start();
            });

        grid.getChildren().addAll(address, addressIn, port, portIn, loginButton);
        //stage.setMaximized(true);

        return new Scene(grid, 600, 400);
    }

    public Scene askLogin(){
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(10);
        grid.setVgap(8);

        Label nickLabel = new Label("Nickname: ");
        GridPane.setConstraints(nickLabel, 0, 0);

        TextField nickname = new TextField();
        GridPane.setConstraints(nickname, 1,0);

        Button nickButton = new Button("Go");
        GridPane.setConstraints(nickButton, 1, 2);

        nickButton.setOnAction(e->{
            playerBoard.setNickname(nickname.getText());
            controller.sendMessage(new MessageLogin(nickname.getText()));
        });

        grid.getChildren().addAll(nickLabel, nickname, nickButton);
        Scene scene = new Scene(grid);
        //stage.setMaximized(true);

        return scene;
    }

    public Scene askNumPlayer() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(10);
        grid.setVgap(8);

        Label numPlayersLabel = new Label("Number of players: ");
        GridPane.setConstraints(numPlayersLabel, 0, 0);

        TextField numPlayersField = new TextField();
        GridPane.setConstraints(numPlayersField, 1,0);


        Button numButton = new Button("Go");
        GridPane.setConstraints(numButton, 1, 2);
        numButton.setOnAction(e->{
            try{
                controller.sendMessage(new MessageNumPlayers(playerBoard.getNickname(), Integer.parseInt(numPlayersField.getText())));
            }catch (NumberFormatException ignore){}
        });

        grid.getChildren().addAll(numPlayersLabel, numPlayersField, numButton);
        Scene scene = new Scene(grid);
        //stage.sizeToScene();
        //stage.setMaximized(true);
        return scene;
    }

    public Scene mainboard(){

        Pane left = new Pane();
        left.setPrefWidth(150);
        left.getChildren().addAll(buttons());
        left.getChildren().addAll(leaderCardBoard());

        Image imageBoardMain = new Image("board/Masters of Renaissance_PlayerBoard (11_2020)-1.png");
        ImageView imageBoardView = new ImageView(imageBoardMain);
        imageBoardView.setFitWidth(747);
        imageBoardView.setFitHeight(550);

        Pane boardMain = new Pane();
        boardMain.getChildren().addAll(imageBoardView);
        boardMain.getChildren().addAll(faithMArkerBoard());

        //POPE SPACE
        Node[] popeSpace = new Node[3];
        Image popeSpace1 = new Image("punchboard/quadrato giallo.png");
        ImageView popeSpace1View = new ImageView(popeSpace1);
        popeSpace1View.setFitHeight(60);
        popeSpace1View.setFitWidth(60);
        popeSpace[0] = popeSpace1View;
        popeSpace[0].setLayoutX(179);
        popeSpace[0].setLayoutY(79);
        if(playerBoard.getPlayerList().size()>1){
            for(String name : playerBoard.getPlayerList()) {
                if (playerBoard.getPlayersPopFavoriteTile().get(name)[0]) {
                    if (playerBoard.getPlayersPopFavoriteTile().get(playerBoard.getNickname())[0]){
                        boardMain.getChildren().add(popeSpace[0]);
                    }
                }
            }
        }
        else{
            if(playerBoard.getBlackCrossToken()>=8 && playerBoard.getPlayersPopFavoriteTile().get(playerBoard.getNickname())[0]){
                boardMain.getChildren().add(popeSpace[0]);
            }
        }


        Image popeSpace2 = new Image("punchboard/quadrato arancione.png");
        ImageView popeSpace2View = new ImageView(popeSpace2);
        popeSpace2View.setFitHeight(60);
        popeSpace2View.setFitWidth(60);
        popeSpace[1] = popeSpace2View;
        popeSpace[1].setLayoutX(363);
        popeSpace[1].setLayoutY(35);
        if(playerBoard.getPlayerList().size()>1){
            for(String name : playerBoard.getPlayerList()) {
                if (playerBoard.getPlayersPopFavoriteTile().get(name)[1]) {
                    if (playerBoard.getPlayersPopFavoriteTile().get(playerBoard.getNickname())[1]){
                        boardMain.getChildren().add(popeSpace[1]);
                    }
                }
            }
        }
        else{
            if(playerBoard.getBlackCrossToken()>=16 && playerBoard.getPlayersPopFavoriteTile().get(playerBoard.getNickname())[1]){
                boardMain.getChildren().add(popeSpace[0]);
            }
        }


        Image popeSpace3 = new Image("punchboard/quadrato rosso.png");
        ImageView popeSpace3View = new ImageView(popeSpace3);
        popeSpace3View.setFitHeight(60);
        popeSpace3View.setFitWidth(60);
        popeSpace[2] = popeSpace3View;
        popeSpace[2].setLayoutX(582);
        popeSpace[2].setLayoutY(78);
        if(playerBoard.getPlayerList().size()>1){
            for(String name : playerBoard.getPlayerList()) {
                if (playerBoard.getPlayersPopFavoriteTile().get(name)[2]) {
                    if (playerBoard.getPlayersPopFavoriteTile().get(playerBoard.getNickname())[2]){
                        boardMain.getChildren().add(popeSpace[2]);
                    }
                }
            }
        }
        else{
            if(playerBoard.getBlackCrossToken()>=24 && playerBoard.getPlayersPopFavoriteTile().get(playerBoard.getNickname())[2]){
                boardMain.getChildren().add(popeSpace[2]);
            }
        }

        //WAREHOUSE
        boardMain.getChildren().addAll(exchangeButton());
        if(playerBoard.getWarehouse()[0][0]!=null){
            Image resource = new Image("punchboard/Coins.png");
            ImageView resourceView = new ImageView(resource);
            resourceView.setFitWidth(30);
            resourceView.setFitHeight(30);
            resourceView.setLayoutX(83);
            resourceView.setLayoutY(235);
            boardMain.getChildren().add(resourceView);
        }

        if(playerBoard.getWarehouse()[1][0]!=null){
            Image resource1 = new Image("punchboard/"+playerBoard.getWarehouse()[1][0]+".png");
            ImageView resource1View = new ImageView(resource1);
            resource1View.setFitWidth(40);
            resource1View.setFitHeight(40);
            resource1View.setLayoutX(66);
            resource1View.setLayoutX(285);
            boardMain.getChildren().add(resource1View);
        }

        if(playerBoard.getWarehouse()[1][1]!=null){
            Image resource2 = new Image("punchboard/"+playerBoard.getWarehouse()[1][1]+".png");
            ImageView resource2View = new ImageView(resource2);
            resource2View.setFitWidth(40);
            resource2View.setFitHeight(40);
            resource2View.setLayoutX(99);
            resource2View.setLayoutX(285);
            boardMain.getChildren().add(resource2View);
        }

        if(playerBoard.getWarehouse()[2][0]!=null){
            Image resource3 = new Image("punchboard/"+playerBoard.getWarehouse()[2][0]+".png");
            ImageView resource3View = new ImageView(resource3);
            resource3View.setFitWidth(40);
            resource3View.setFitHeight(40);
            resource3View.setLayoutX(50);
            resource3View.setLayoutX(355);
            boardMain.getChildren().add(resource3View);
        }

        if(playerBoard.getWarehouse()[2][1]!=null){
            Image resource4 = new Image("punchboard/"+playerBoard.getWarehouse()[2][1]+".png");
            ImageView resource4View = new ImageView(resource4);
            resource4View.setFitWidth(40);
            resource4View.setFitHeight(40);
            resource4View.setLayoutX(81);
            resource4View.setLayoutX(355);
            boardMain.getChildren().add(resource4View);
        }

        if(playerBoard.getWarehouse()[2][2]!=null){
            Image resource5 = new Image("punchboard/"+playerBoard.getWarehouse()[2][2]+".png");
            ImageView resource5View = new ImageView(resource5);
            resource5View.setFitWidth(40);
            resource5View.setFitHeight(40);
            resource5View.setLayoutX(112);
            resource5View.setLayoutX(355);
            boardMain.getChildren().add(resource5View);
        }

        //STRONGBOX
        boardMain.getChildren().addAll(strongboxPane());
        //DEVCARD
        boardMain.getChildren().addAll(devCardBoard());

        Button fake = new Button("USTIII");
        fake.setOnAction(e-> controller.sendMessage(new MessageFake(playerBoard.getNickname())));
        boardMain.getChildren().add(fake);

        HBox allBoard = new HBox();
        allBoard.getChildren().addAll(left, boardMain);

        Scene scene = new Scene(allBoard);

        return scene;
    }
    public Node[] exchangeButton(){
        Node[] exchange = new Node[4];

        CheckBox row1 = new CheckBox();
        CheckBox row2 = new CheckBox();
        CheckBox row3 = new CheckBox();
        Button exch = new Button("Exchange");
        exchange[0] = row1;
        exchange[0].setLayoutY(242);
        exchange[0].setLayoutX(17);
        exchange[1] = row2;
        exchange[1].setLayoutY(288);
        exchange[1].setLayoutX(17);
        exchange[2] = row3;
        exchange[2].setLayoutY(337);
        exchange[2].setLayoutX(17);
        exchange[3]= exch;
        exchange[3].setLayoutY(370);
        exchange[3].setLayoutX(17);

        return  exchange;
    }

    public Node[] strongboxPane(){
        Node[] resources = new Node[8];

        Image coins = new Image("punchboard/Coins.png");
        ImageView coinsView = new ImageView(coins);
        coinsView.setFitWidth(30);
        coinsView.setFitHeight(30);
        coinsView.setLayoutX(20);
        coinsView.setLayoutY(430);
        resources[0] = coinsView;

        Image shields = new Image("punchboard/Shields.png");
        ImageView shieldsView = new ImageView(shields);
        shieldsView.setFitWidth(30);
        shieldsView.setFitHeight(30);
        shieldsView.setLayoutX(20);
        shieldsView.setLayoutY(470);
        resources[1] = shieldsView;

        Image stones = new Image("punchboard/Stones.png");
        ImageView stonesView = new ImageView(stones);
        stonesView.setFitWidth(30);
        stonesView.setFitHeight(30);
        stonesView.setLayoutX(90);
        stonesView.setLayoutY(430);
        resources[2] = stonesView;

        Image servants = new Image("punchboard/Servants.png");
        ImageView servantsView = new ImageView(servants);
        servantsView.setFitWidth(30);
        servantsView.setFitHeight(30);
        servantsView.setLayoutX(90);
        servantsView.setLayoutY(470);
        resources[3] = servantsView;


        Label numberofCoins = new Label();
        if(playerBoard.getStrongbox().get("Coins")!=null)
        numberofCoins.setText(String.valueOf(playerBoard.getStrongbox().get("Coins")));
        else numberofCoins.setText(String.valueOf(0));
        numberofCoins.setFont(new Font("Arial", 20));
        numberofCoins.setTextFill(Color.WHITE);
        numberofCoins.setLayoutX(50);
        numberofCoins.setLayoutY(435);
        resources[4] = numberofCoins;

        Label numberofShields = new Label();
        if(playerBoard.getStrongbox().get("Shields")!=null)
        numberofShields.setText(String.valueOf(playerBoard.getStrongbox().get("Shields")));
        else numberofShields.setText(String.valueOf(0));
        numberofShields.setFont(new Font("Arial", 20));
        numberofShields.setTextFill(Color.WHITE);
        numberofShields.setLayoutX(50);
        numberofShields.setLayoutY(475);
        resources[5] = numberofShields;

        Label numberofStones = new Label();
        if(playerBoard.getStrongbox().get("Stones")!=null)
        numberofStones.setText(String.valueOf(playerBoard.getStrongbox().get("Stones")));
        else numberofStones.setText(String.valueOf(0));
        numberofStones.setFont(new Font("Arial", 20));
        numberofStones.setTextFill(Color.WHITE);
        numberofStones.setLayoutX(120);
        numberofStones.setLayoutY(435);
        resources[6] = numberofStones;

        Label numberofServants = new Label();
        if(playerBoard.getStrongbox().get("Servants")!=null)
        numberofServants.setText(String.valueOf(playerBoard.getStrongbox().get("Servants")));
        else numberofServants.setText(String.valueOf(0));
        numberofServants.setFont(new Font("Arial", 20));
        numberofServants.setTextFill(Color.WHITE);
        numberofServants.setLayoutX(120);
        numberofServants.setLayoutY(475);
        resources[7] = numberofServants;

        return resources;

    }

    public List<Node> devCardBoard(){
        List<Node> devcards = new ArrayList<>();

        if(playerBoard.getSlotDevCard()[0][0]!=null) {
            Image leadercard1 = new Image("front/"+playerBoard.getSlotDevCard()[0][0]+".png");
            ImageView leadercard1View = new ImageView(leadercard1);
            leadercard1View.setFitHeight(250);
            leadercard1View.setFitWidth(145);
            leadercard1View.setLayoutX(280);
            leadercard1View.setLayoutY(200);
            devcards.add(leadercard1View);
        }
        if(playerBoard.getSlotDevCard()[0][1]!=null) {
            Image leadercard2 = new Image("front/"+playerBoard.getSlotDevCard()[0][1]+".png");
            ImageView leadercard2View = new ImageView(leadercard2);
            leadercard2View.setFitHeight(250);
            leadercard2View.setFitWidth(145);
            leadercard2View.setLayoutX(427);
            leadercard2View.setLayoutY(200);
            devcards.add(leadercard2View);
        }
        if(playerBoard.getSlotDevCard()[0][2]!=null) {
            Image leadercard3 = new Image("front/"+playerBoard.getSlotDevCard()[0][2]+".png");
            ImageView leadercard3View = new ImageView(leadercard3);
            leadercard3View.setFitHeight(250);
            leadercard3View.setFitWidth(145);
            leadercard3View.setLayoutX(574);
            leadercard3View.setLayoutY(200);
            devcards.add(leadercard3View);
        }
        if(playerBoard.getSlotDevCard()[1][0]!=null) {
            Image leadercard4 = new Image("front/"+playerBoard.getSlotDevCard()[1][0]+".png");
            ImageView leadercard4View = new ImageView(leadercard4);
            leadercard4View.setFitHeight(250);
            leadercard4View.setFitWidth(145);
            leadercard4View.setLayoutX(280);
            leadercard4View.setLayoutY(220);
            devcards.add(leadercard4View);
        }
        if(playerBoard.getSlotDevCard()[1][1]!=null) {
            Image leadercard5 = new Image("front/"+playerBoard.getSlotDevCard()[1][1]+".png");
            ImageView leadercard5View = new ImageView(leadercard5);
            leadercard5View.setFitHeight(250);
            leadercard5View.setFitWidth(145);
            leadercard5View.setLayoutX(427);
            leadercard5View.setLayoutY(220);
            devcards.add(leadercard5View);
        }
        if(playerBoard.getSlotDevCard()[1][2]!=null) {
            Image leadercard6 = new Image("front/"+playerBoard.getSlotDevCard()[1][2]+".png");
            ImageView leadercard6View = new ImageView(leadercard6);
            leadercard6View.setFitHeight(250);
            leadercard6View.setFitWidth(145);
            leadercard6View.setLayoutX(574);
            leadercard6View.setLayoutY(220);
            devcards.add(leadercard6View);
        }
        if(playerBoard.getSlotDevCard()[2][0]!=null) {
            Image leadercard7 = new Image("front/"+playerBoard.getSlotDevCard()[2][0]+".png");
            ImageView leadercard7View = new ImageView(leadercard7);
            leadercard7View.setFitHeight(250);
            leadercard7View.setFitWidth(145);
            leadercard7View.setLayoutX(280);
            leadercard7View.setLayoutY(240);
            devcards.add(leadercard7View);
        }
        if(playerBoard.getSlotDevCard()[2][1]!=null) {
            Image leadercard8 = new Image("front/"+playerBoard.getSlotDevCard()[2][1]+".png");
            ImageView leadercard8View = new ImageView(leadercard8);
            leadercard8View.setFitHeight(250);
            leadercard8View.setFitWidth(145);
            leadercard8View.setLayoutX(427);
            leadercard8View.setLayoutY(240);
            devcards.add(leadercard8View);
        }
        if(playerBoard.getSlotDevCard()[2][2]!=null) {
            Image leadercard9 = new Image("front/"+playerBoard.getSlotDevCard()[2][2]+".png");
            ImageView leadercard9View = new ImageView(leadercard9);
            leadercard9View.setFitHeight(250);
            leadercard9View.setFitWidth(145);
            leadercard9View.setLayoutX(427);
            leadercard9View.setLayoutY(240);
            devcards.add(leadercard9View);
        }

        return devcards;

    }

    public Node[] faithMArkerBoard(){
        Node[] faithMarker;
        if(playerBoard.getPlayerList().size()==1) faithMarker = new Node[2];
        else faithMarker = new Node[1];

        Image imageFaith = new Image("punchboard/faithMarker.png");
        ImageView imageFaithView = new ImageView(imageFaith);
        imageFaithView.setFitWidth(30);
        imageFaithView.setFitHeight(37);
        faithMarker[0] = imageFaithView;
        faithMarker[0].setLayoutY(getFaithMarkerY(playerBoard.getFaithMarker()));
        faithMarker[0].setLayoutX(getFaithMarkerX(playerBoard.getFaithMarker()));

        if(playerBoard.getPlayerList().size()==1){
            Image imageLorenzo = new Image("punchboard/croce.png");
            ImageView imageLorenzoView = new ImageView(imageLorenzo);
            imageLorenzoView.setFitWidth(28);
            imageLorenzoView.setFitHeight(35);
            faithMarker[1] = imageLorenzoView;
            faithMarker[1].setLayoutY(getFaithMarkerY(playerBoard.getBlackCrossToken()));
            faithMarker[1].setLayoutX(getFaithMarkerX(playerBoard.getBlackCrossToken())-2);
        }

        return faithMarker;
    }

    public Node[] leaderCardBoard(){

        Image leadercard1, leadercard2;

        if(playerBoard.getLeaderCards().get(0)!=null){
            leadercard1 = new Image("front/"+playerBoard.getLeaderCards().get(0)+".png");
        }
        else leadercard1 = new Image("back/Masters of Renaissance__Cards_BACK_3mmBleed-49-1.png");

        if(playerBoard.getLeaderCards().get(1)!=null){
            leadercard2 = new Image("front/"+playerBoard.getLeaderCards().get(1)+".png");
        }
        else leadercard2 = new Image("back/Masters of Renaissance__Cards_BACK_3mmBleed-49-1.png");

        ImageView leadercard1View = new ImageView(leadercard1);
        leadercard1View.setFitHeight(195);
        leadercard1View.setFitWidth(150);

        ImageView leadercard2View = new ImageView(leadercard2);
        leadercard2View.setFitHeight(195);
        leadercard2View.setFitWidth(150);

        Node[] leaderCards = new Node[2];
        leaderCards[0] = leadercard1View;
        leaderCards[0].setLayoutY(160);
        leaderCards[1] = leadercard2View;
        leaderCards[1].setLayoutY(355);

        return leaderCards;
    }

    public Node[] buttons(){

        Button marketTray = new Button();
        marketTray.setText("Extract");
        marketTray.setLayoutY(5);
        marketTray.setOnAction(e->{
            activeExtraction();
        });

        Button buyDevCard = new Button();
        buyDevCard.setText("Buy DevCard");
        buyDevCard.setLayoutY(31);
        buyDevCard.setOnAction(e->{
            activebuy();
        });

        Button production = new Button();
        production.setText("Production");
        production.setLayoutY(56);
        production.setOnAction(e-> activeProductions());

        Button showMarket = new Button();
        showMarket.setText("Show market");
        showMarket.setLayoutY(81);
        showMarket.setOnAction(e-> showMarket());

        Button showDevDeck = new Button();
        showDevDeck.setText("Show DevDeck");
        showDevDeck.setLayoutY(106);
        showDevDeck.setOnAction(e-> showDevDeck());

        Button players = new Button();
        players.setText("Show players");
        players.setLayoutY(131);
        players.setOnAction(e-> showOtherPlayers());

        //non si vede perché è coperto
        Button endTurn = new Button("End Turn");
        endTurn.setLayoutY(156);
        endTurn.setOnAction(e-> controller.sendMessage(new MessageEndTurn(playerBoard.getNickname())));

        Button[] buttons = new Button[7];
        buttons[0] = marketTray;
        buttons[1] = showMarket;
        buttons[2] = buyDevCard;
        buttons[3] = showDevDeck;
        buttons[4] = production;
        buttons[5] = players;
        buttons[6] = endTurn;

        for(Button button : buttons){
            button.setMinWidth(150);
            button.setMinHeight(15);
        }

        return buttons;
    }

    public double getFaithMarkerX(int pos){
        switch (pos){
            case 0: return 30;
            case 1: return 67;
            case 2:
            case 3:
            case 4:
                return 103;
            case 5: return 140;
            case 6: return 177;
            case 7: return 213;
            case 8: return 250;
            case 9:
            case 10:
            case 11:
                return 287;
            case 12: return 324;
            case 13: return 360;
            case 14: return 397;
            case 15: return 434;
            case 16:
            case 17:
            case 18:
                return 470;
            case 19: return 507;
            case 20: return 543;
            case 21: return 580;
            case 22: return 616;
            case 23: return 653;
            case 24: return 689;
            default: return 0;
        }
    }
    public double getFaithMarkerY(int pos){
        switch (pos){
            case 0:
            case 1:
            case 2:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16: return 105;
            case 3:
            case 10:
            case 17: return 68;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24: return 31;
            default: return 0;
        }
    }


    public Scene chooseInitialLeaderCards() {
        HBox box1 = new HBox();
        ImageView[] leaderCardImgView = new ImageView[4];
        int i = 0;

        for (String leaderCard : playerBoard.getLeaderCards()) {
            Image leaderCardImage = new Image("front/" + leaderCard + ".png");
            leaderCardImgView[i] = new ImageView(leaderCardImage);
            leaderCardImgView[i].setX(50 + i*10);
            leaderCardImgView[i].setY(20);
            leaderCardImgView[i].setFitHeight(200);
            leaderCardImgView[i].setFitWidth(200);
            leaderCardImgView[i].setPreserveRatio(true);
            i++;
        }
        box1.getChildren().addAll(leaderCardImgView);

        HBox box2 = new HBox();
        int[] chosenLeaderCards = new int[2];

        Label labelLeaderCard1 = new Label("First Leader card: ");
        ChoiceBox cb1 = new ChoiceBox(FXCollections.observableArrayList(
                "1", "2", "3", "4")
        );

        Label labelLeaderCard2 = new Label("Second Leader card: ");
        ChoiceBox cb2 = new ChoiceBox(FXCollections.observableArrayList(
                "1", "2", "3", "4")
        );
        cb1.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                    Number old_value, Number new_val) -> {
                        chosenLeaderCards[0] = new_val.intValue();
                });

        cb2.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_value, Number new_val) -> {
                    chosenLeaderCards[1] = new_val.intValue();
                });
        Button enterCards = new Button("Enter");
        enterCards.setOnAction(e->{
            controller.sendMessage(new MessageChooseLeaderCards(playerBoard.getNickname(), chosenLeaderCards[0], chosenLeaderCards[1]));
        });
        box2.getChildren().addAll(labelLeaderCard1, cb1, labelLeaderCard2, cb2, enterCards);

        BorderPane total =new BorderPane();
        total.setTop(box1);
        total.setCenter(box2);
        return new Scene(total);
    }

    public Scene chooseInitialResources(){
        HBox boxResources = new HBox();
        List<ImageView> resourcesView = new ArrayList<>();
        String[] resourcePath = {"coin.png", "servant.png", "shield.png", "stone.png"};
        List<String> resourcesToSend = new ArrayList<>();

        for (String r : resourcePath){
            ImageView resourceView = new ImageView(new Image("punchboard/" + r));
            resourceView.setFitWidth(100);
            resourceView.setFitHeight(100);
            resourcesView.add(resourceView);
        }

        boxResources.getChildren().addAll(resourcesView);

        HBox boxChoice = new HBox();
        String[] resourcesVett = {"Coins", "Servants", "Shields", "Stones"};

        Label labelResources1 = new Label("First Resource: ");
        ChoiceBox cb1 = new ChoiceBox(FXCollections.observableArrayList(
                "Coin", "Servant", "Shield", "Stone")
        );

        Label labelResources2 = new Label("Second Resource: ");
        ChoiceBox cb2 = new ChoiceBox(FXCollections.observableArrayList(
                "Coin", "Servant", "Shield", "Stone")
        );
        cb1.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_value, Number new_val) -> {
                    resourcesToSend.add(resourcesVett[new_val.intValue()]);
                });

        cb2.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_value, Number new_val) -> {
                    resourcesToSend.add(resourcesVett[new_val.intValue()]);
                });
        Button enterCards = new Button("Enter");
        enterCards.setOnAction(e->{
            controller.sendMessage(new MessageChooseResourcesFirstTurn(playerBoard.getNickname(), resourcesToSend));
        });
        if (playerBoard.getPlayerList().indexOf(playerBoard.getNickname()) == 1 || playerBoard.getPlayerList().indexOf(playerBoard.getNickname()) == 2)
            boxChoice.getChildren().addAll(labelResources1, cb1, enterCards);
        else if (playerBoard.getPlayerList().indexOf(playerBoard.getNickname()) == 4)
            boxChoice.getChildren().addAll(labelResources1, cb1, labelResources2, cb2, enterCards);

        BorderPane total =new BorderPane();
        total.setTop(boxResources);
        total.setCenter(boxChoice);
        return new Scene(total, 1000, 1000);
    }

    public Scene showMessage(String message) {
        TextFlow textFlow = new TextFlow();
        Text text = new Text(message);
        textFlow.getChildren().addAll(text);
        Group group = new Group(textFlow);
        return new Scene(group, 650, 150);
    }

    public void showErrorMessage(String errorMessage) {
        Stage stage1 = new Stage();
        TextFlow textFlow = new TextFlow();
        Text text = new Text(errorMessage);
        textFlow.getChildren().addAll(text);
        Group group = new Group(textFlow);
        stage1.setTitle("Error");
        stage1.setScene(new Scene(group));
        stage1.show();
    }

    public void activeProductions() {
        Stage stage1 = new Stage();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(10);
        grid.setVgap(8);

        Label labelChoice = new Label("Choose the type of production: ");
        GridPane.setConstraints(labelChoice, 0, 0);

        Button baseProductionButton = new Button("Base\nProduction");
        GridPane.setConstraints(baseProductionButton, 0, 1);
        baseProductionButton.setOnAction(e->{
            baseProductionScene(stage1);
        });
        Button devCardProductionButton = new Button("Development Card\nProduction");
        /*
        devCardProductionButton.setOnAction(e->{

        });

         */
        GridPane.setConstraints(devCardProductionButton, 1, 1);
        Button leaderCardProductionButton = new Button("Leader Card\nProduction");
        /*
        leaderCardProductionButton.setOnAction(e->{

        });

         */
        GridPane.setConstraints(leaderCardProductionButton, 2, 1);
        grid.getChildren().addAll(labelChoice, baseProductionButton, devCardProductionButton, leaderCardProductionButton);
        Scene scene = new Scene(grid);
        stage1.setScene(scene);
        stage1.setTitle("Active Production");
        stage1.show();
    }

    public void baseProductionScene(Stage stage1) {
        char[] chosenStructures = new char[2];
        String[] resourcesVett = {"Coins", "Servants", "Shields", "Stones"};
        String[] chosenResources = new String[3];

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(10);
        grid.setVgap(8);

        Label explanationLabel1 = new Label("Choose from which do you want to take Resources: ");
        GridPane.setConstraints(explanationLabel1, 0, 0);

        ChoiceBox cbStructure1 = new ChoiceBox(FXCollections.observableArrayList(
                "Warehouse", "Strongbox", "Extra chest")
        );
        GridPane.setConstraints(cbStructure1, 0, 3);
        cbStructure1.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_value, Number new_val) -> {
                    switch (new_val.intValue()) {
                        case 0 :
                            chosenStructures[0] = 'W';
                            break;
                        case 1 :
                            chosenStructures[0] = 'S';
                            break;
                        case 2 :
                            chosenStructures[0] = 'E';
                            break;
                        default:
                            break;
                    }
                });

        ChoiceBox cbResources1 = new ChoiceBox(FXCollections.observableArrayList(
                "Coin", "Servant", "Shield", "Stone")
        );
        GridPane.setConstraints(cbResources1, 1, 3);
        cbResources1.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_value, Number new_val) -> {
                    chosenResources[0] = resourcesVett[new_val.intValue()];
                });

        ChoiceBox cbStructure2 = new ChoiceBox(FXCollections.observableArrayList(
                "Warehouse", "Strongbox", "Extra chest")
        );
        GridPane.setConstraints(cbStructure2, 0, 5);
        cbStructure2.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_value, Number new_val) -> {
                    switch (new_val.intValue()) {
                        case 0 :
                            chosenStructures[1] = 'W';
                            break;
                        case 1 :
                            chosenStructures[1] = 'S';
                            break;
                        case 2 :
                            chosenStructures[1] = 'E';
                            break;
                        default:
                            break;
                    }
                });

        ChoiceBox cbResources2 = new ChoiceBox(FXCollections.observableArrayList(
                "Coin", "Servant", "Shield", "Stone")
        );
        GridPane.setConstraints(cbResources2, 1, 5);
        cbResources1.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_value, Number new_val) -> {
                    chosenResources[1] = resourcesVett[new_val.intValue()];
                });

        Label explanationLabel2 = new Label("Choose the produced Resources: ");
        GridPane.setConstraints(explanationLabel2, 0, 7);

        ChoiceBox cbResources3 = new ChoiceBox(FXCollections.observableArrayList(
                "Coin", "Servant", "Shield", "Stone")
        );
        GridPane.setConstraints(cbResources3, 0, 9);
        cbResources3.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov,
                 Number old_value, Number new_val) -> {
                    chosenResources[2] = resourcesVett[new_val.intValue()];
                });

        Button enterButton = new Button("Enter");
        GridPane.setConstraints(enterButton, 0, 11);
        enterButton.setOnAction(e->{
            controller.sendMessage(new MessageActiveBaseProduction(playerBoard.getNickname(), chosenResources[2], chosenStructures[0], chosenResources[0], chosenStructures[1], chosenResources[1]));
        });
        grid.getChildren().addAll(explanationLabel1, cbStructure1, cbResources1, cbStructure2, cbResources2, explanationLabel2, cbResources3, enterButton);

        Scene scene = new Scene(grid, 1000, 1000);
        stage1.setScene(scene);
        stage1.setMaximized(true);
        stage1.show();
    }

    public Scene payResourcesScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(10);
        grid.setVgap(8);

        Label labelWarehouse = new Label("Warehouse: ");
        GridPane.setConstraints(labelWarehouse, 0, 0);

        Label labelCoinW = new Label("Coins: ");
        GridPane.setConstraints(labelCoinW, 0, 1);
        TextField numCoinsWarehouse = new TextField();
        GridPane.setConstraints(numCoinsWarehouse, 1, 1);
        Label labelServantW = new Label("Servants: ");
        GridPane.setConstraints(labelServantW, 2, 1);
        TextField numServantsWarehouse = new TextField();
        GridPane.setConstraints(numServantsWarehouse, 3, 1);
        Label labelShieldW = new Label("Shields: ");
        GridPane.setConstraints(labelShieldW, 4, 1);
        TextField numShieldsWarehouse = new TextField();
        GridPane.setConstraints(numShieldsWarehouse, 5, 1);
        Label labelStonesW = new Label("Stones: ");
        GridPane.setConstraints(labelStonesW, 6, 1);
        TextField numStonesWarehouse = new TextField();
        GridPane.setConstraints(numStonesWarehouse, 7, 1);

        Label labelStrongbox = new Label("Strongbox: ");
        GridPane.setConstraints(labelStrongbox, 0, 2);

        Label labelCoinS = new Label("Coins: ");
        GridPane.setConstraints(labelCoinS, 0, 3);
        TextField numCoinsStrongbox = new TextField();
        GridPane.setConstraints(numCoinsStrongbox, 1, 3);
        Label labelServantS = new Label("Servants: ");
        GridPane.setConstraints(labelServantS, 2, 3);
        TextField numServantsStrongbox = new TextField();
        GridPane.setConstraints(numServantsStrongbox, 3, 3);
        Label labelShieldS = new Label("Shields: ");
        GridPane.setConstraints(labelShieldS, 4, 3);
        TextField numShieldsStrongbox = new TextField();
        GridPane.setConstraints(numShieldsStrongbox, 5, 3);
        Label labelStonesS = new Label("Stones: ");
        GridPane.setConstraints(labelStonesS, 6, 3);
        TextField numStonesStrongbox = new TextField();
        GridPane.setConstraints(numStonesStrongbox, 7, 3);

        Label labelExtraChest = new Label("ExtraChest: ");
        GridPane.setConstraints(labelExtraChest, 0, 4);

        Label labelCoinE = new Label("Coins: ");
        GridPane.setConstraints(labelCoinE, 0, 5);
        TextField numCoinsExtraChest = new TextField();
        GridPane.setConstraints(numCoinsExtraChest, 1, 5);
        Label labelServantE = new Label("Servants: ");
        GridPane.setConstraints(labelServantE, 2, 5);
        TextField numServantsExtraChest = new TextField();
        GridPane.setConstraints(numServantsExtraChest, 3, 5);
        Label labelShieldE = new Label("Shields: ");
        GridPane.setConstraints(labelShieldE, 4, 5);
        TextField numShieldsExtraChest = new TextField();
        GridPane.setConstraints(numShieldsExtraChest, 5, 5);
        Label labelStonesE = new Label("Stones: ");
        GridPane.setConstraints(labelStonesE, 6, 5);
        TextField numStonesExtraChest = new TextField();
        GridPane.setConstraints(numStonesExtraChest, 7, 5);

        Button enterButton = new Button("Enter");
        enterButton.setOnAction(e->{
            Map<String, Integer> warehouseMap = new HashMap<>();
            Map<String, Integer> strongboxMap = new HashMap<>();
            Map<String, Integer> extraChestMap = new HashMap<>();
            if (numCoinsWarehouse.getText() != null)
                warehouseMap.put("Coins", Integer.parseInt(numCoinsWarehouse.getText()));
            if (numServantsWarehouse.getText() != null)
                warehouseMap.put("Servants", Integer.parseInt(numServantsWarehouse.getText()));
            if (numShieldsWarehouse.getText() != null)
                warehouseMap.put("Shields", Integer.parseInt(numShieldsWarehouse.getText()));
            if (numStonesWarehouse.getText() != null)
                warehouseMap.put("Stones", Integer.parseInt(numStonesWarehouse.getText()));
            if (numCoinsStrongbox.getText() != null)
                strongboxMap.put("Coins", Integer.parseInt(numCoinsStrongbox.getText()));
            if (numServantsStrongbox.getText() != null)
                strongboxMap.put("Servants", Integer.parseInt(numServantsStrongbox.getText()));
            if (numShieldsStrongbox.getText() != null)
                strongboxMap.put("Shields", Integer.parseInt(numShieldsStrongbox.getText()));
            if (numStonesStrongbox.getText() != null)
                strongboxMap.put("Stones", Integer.parseInt(numStonesStrongbox.getText()));
            if (numCoinsExtraChest.getText() != null)
                extraChestMap.put("Coins", Integer.parseInt(numCoinsExtraChest.getText()));
            if (numServantsExtraChest.getText() != null)
                extraChestMap.put("Servants", Integer.parseInt(numServantsExtraChest.getText()));
            if (numShieldsExtraChest.getText() != null)
                extraChestMap.put("Shields", Integer.parseInt(numShieldsExtraChest.getText()));
            if (numStonesExtraChest.getText() != null)
                extraChestMap.put("Stones", Integer.parseInt(numStonesExtraChest.getText()));
            controller.sendMessage(new MessagePayResources(playerBoard.getNickname(), warehouseMap, strongboxMap, extraChestMap));
        });
        GridPane.setConstraints(enterButton, 0, 6);
        grid.getChildren().addAll(labelWarehouse, labelCoinW, numCoinsWarehouse, labelServantW, numServantsWarehouse,
                labelShieldW, numShieldsWarehouse, labelStonesW, numStonesWarehouse, labelStrongbox,
                labelCoinS, numCoinsStrongbox, labelServantS, numServantsStrongbox, labelShieldS, numShieldsStrongbox,
                labelStonesS, numStonesStrongbox, labelExtraChest, labelCoinE, numCoinsExtraChest, labelServantE,
                numServantsExtraChest, labelShieldE, numShieldsExtraChest, labelStonesE, numStonesExtraChest, enterButton
        );
        return new Scene(grid, 1000, 1000);
    }

    public void activeExtraction(){
        Stage stage2 = new Stage();
        Pane marketTrayPane = new Pane();

        ImageView marblesPunchView = new ImageView(new Image("punchboard/plancia portabiglie.png"));
        marblesPunchView.setFitWidth(400);
        marblesPunchView.setPreserveRatio(true);
        marketTrayPane.getChildren().add(marblesPunchView);

        ImageView marketTrayView = new ImageView(new Image("punchboard/spazioBiglie.png"));
        marketTrayView.setFitWidth(280);
        marketTrayView.setPreserveRatio(true);
        marketTrayView.setY(62);
        marketTrayView.setX(62);
        marketTrayPane.getChildren().add(marketTrayView);

        ImageView[][] marblesView = new ImageView[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                marblesView[i][j] = new ImageView(new Image("punchboard/Marbles/" + playerBoard.getMarketTray()[i][j] + ".png"));
                marblesView[i][j].setFitWidth(40);
                marblesView[i][j].setPreserveRatio(true);
                marblesView[i][j].setX(j*46 + 120);
                marblesView[i][j].setY(i*46 + 120);
                marketTrayPane.getChildren().add(marblesView[i][j]);
            }
        }
        ImageView remainingMarbleView = new ImageView(new Image("punchboard/Marbles/" + playerBoard.getRemainingMarble() + ".png"));
        remainingMarbleView.setFitWidth(40);
        remainingMarbleView.setPreserveRatio(true);
        remainingMarbleView.setX(290);
        remainingMarbleView.setY(80);
        marketTrayPane.getChildren().add(remainingMarbleView);

        final int maxCount = 1;
        final Set<CheckBox> activatedCheckBoxes = new LinkedHashSet<>();
        List<CheckBox> checkBoxList = new ArrayList<>();
        List<CheckBox> checkBoxListColumns = new ArrayList<>();
        List<CheckBox> checkBoxListRows = new ArrayList<>();

        ChangeListener<Boolean> listener = (o, oldValue, newValue) -> {
            CheckBox checkBox = (CheckBox) ((ReadOnlyProperty) o).getBean();

            if (newValue) {
                activatedCheckBoxes.add(checkBox);
                checkBoxList.add(checkBox);
                if (activatedCheckBoxes.size() > maxCount) {
                    // get first checkbox to be activated
                    checkBox = activatedCheckBoxes.iterator().next();

                    // unselect; change listener will remove
                    checkBox.setSelected(false);
                    checkBoxList.remove(checkBox);
                }
            } else {
                activatedCheckBoxes.remove(checkBox);
            }
        };

        // create checkboxes
        for (int i = 0; i < 4; i++) {
            CheckBox checkBox = new CheckBox();
            checkBox.setUserData('C');
            checkBox.setLayoutX(marblesView[2][i].getX() + 10);
            checkBox.setLayoutY(marblesView[2][i].getY() + 50);
            checkBox.selectedProperty().addListener(listener);
            checkBoxListColumns.add(checkBox);
            marketTrayPane.getChildren().add(checkBox);
        }

        for (int i = 0; i < 3; i++) {
            CheckBox checkBox = new CheckBox();
            checkBox.setUserData('R');
            checkBox.setLayoutX(marblesView[2][3].getX() + 52);
            checkBox.setLayoutY(marblesView[0][0].getY() + 12 + i*45);
            checkBox.selectedProperty().addListener(listener);
            checkBoxListRows.add(checkBox);
            marketTrayPane.getChildren().add(checkBox);
        }

        Button enterButton = new Button("Extract");
        enterButton.setOnAction(e->{
            try {
                if (checkBoxListColumns.contains(checkBoxList.get(0))) {
                    System.out.println("c" + checkBoxListColumns.indexOf(checkBoxList.get(0)));
                    controller.sendMessage(new MessageExtractionMarbles(playerBoard.getNickname(), 'C', checkBoxListColumns.indexOf(checkBoxList.get(0))));
                }
                else if (checkBoxListRows.contains(checkBoxList.get(0))) {
                    System.out.println("r" + checkBoxListRows.indexOf(checkBoxList.get(0)));
                    controller.sendMessage(new MessageExtractionMarbles(playerBoard.getNickname(), 'R', checkBoxListRows.indexOf(checkBoxList.get(0))));
                }
                else
                    System.out.println("Sbagliato");
            }
            catch (IndexOutOfBoundsException exception) {
                showErrorMessage("Choose a row or a column");
            }
        });
        enterButton.setLayoutX(323);
        enterButton.setLayoutY(330);
        marketTrayPane.getChildren().add(enterButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(marketTrayPane);

        stage2.setScene(new Scene(borderPane));
        stage2.setTitle("Extract marbles");
        stage2.show();
    }

    public void activebuy(){}

    public void showMarket(){
        Stage stage1 = new Stage();
        Pane marketTrayPane = new Pane();

        ImageView marblesPunchView = new ImageView(new Image("punchboard/plancia portabiglie.png"));
        marblesPunchView.setFitWidth(400);
        marblesPunchView.setPreserveRatio(true);
        marketTrayPane.getChildren().add(marblesPunchView);

        ImageView marketTrayView = new ImageView(new Image("punchboard/spazioBiglie.png"));
        marketTrayView.setFitWidth(280);
        marketTrayView.setPreserveRatio(true);
        marketTrayView.setY(62);
        marketTrayView.setX(62);
        marketTrayPane.getChildren().add(marketTrayView);

        ImageView[][] marblesView = new ImageView[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                marblesView[i][j] = new ImageView(new Image("punchboard/Marbles/" + playerBoard.getMarketTray()[i][j] + ".png"));
                marblesView[i][j].setFitWidth(40);
                marblesView[i][j].setPreserveRatio(true);
                marblesView[i][j].setX(j*46 + 120);
                marblesView[i][j].setY(i*46 + 120);
                marketTrayPane.getChildren().add(marblesView[i][j]);
            }
        }
        ImageView remainingMarbleView = new ImageView(new Image("punchboard/Marbles/" + playerBoard.getRemainingMarble() + ".png"));
        remainingMarbleView.setFitWidth(40);
        remainingMarbleView.setPreserveRatio(true);
        remainingMarbleView.setX(290);
        remainingMarbleView.setY(80);
        marketTrayPane.getChildren().add(remainingMarbleView);

        stage1.setScene(new Scene(marketTrayPane));
        stage1.setTitle("Market tray");
        stage1.show();
    }

    public void showDevDeck(){}

    public void showOtherPlayers(){}
}
