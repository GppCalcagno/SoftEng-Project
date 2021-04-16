package it.polimi.ingsw.player;

import it.polimi.ingsw.card.leadereffect.ExtraChest;
import it.polimi.ingsw.producible.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WarehouseDepots {
    /**
     * sizex and sizey represent the size of the warehouse
     */
    private final int sizex = 3;
    private final int sizey = 3;
    /** The warehouse is a diagonal matrix, where the constructor block three
     * cells in order to trasform the warehouse
     */
    private Resources[][] warehouse;

    /** The list leaderCardEffect is not null when the player has a leader card
     * that add extra chest to the warehouse.
     */
    private List<ExtraChest> leaderCardEffect;

    /** this is the constructor, the warehouse is created as a matrix then transform
     * in a diagonal matrix
     */
    public WarehouseDepots(){
        leaderCardEffect= new ArrayList<>();
        warehouse = new Resources[sizex][sizey];
        warehouse[0][1] = null;
        warehouse[0][2] = null;
        warehouse[1][2] = null;

    }

    /**
     * @param row the caller wants to know how many resources are in that
     *      *               row
     * @return an integer: the number of resources
     */
    public int getWarehouseNum(int row){
        return (int) Arrays.stream(warehouse[row]).filter(i -> i !=null).count();
    }

    /** this method check if an insertion from the market into the warehouse
     * is legal in accord with the rules of the game
     * @param row the column where i want put my resources
     * @param res the type of Resources i want insert
     */
    public boolean checkInsertion(int row, Resources res){
        boolean already = false;
        if(warehouse[row][0]!=null) {
            if (res.getClass().equals(warehouse[row][0].getClass())) {
                if(insertResources(row, res)) return true;
                else return  false;
            }
            else return false;
        }
        else{
            for(int i=0; i<warehouse.length; i++){
                if(warehouse[i][0]!=null && res.getClass().equals(warehouse[i][0].getClass())) already= true;
            }
            if(!already) {
                if(insertResources(row, res)) return true;
                else return false;
            }
            else return false;
        }
    }

    /** this method allow to insert a resource in the warehouse matrix
     * @param row where
     * @param res
     * @return
     */
    public boolean insertResources (int row, Resources res) {
        int i = 0;
        while (warehouse[row][i]!=null){
            i++;
        }
        if(i<row+1){
            warehouse[row][i] = res;
            return true;
        }
        else return false;
    }

    /** this method check if the player can do a change of depots in accord with the rules
     * @param row1 the first column that player want change
     * @param row2 the second column that player want to change
     */
    public boolean checkExchange( int row1, int row2){
        if(getWarehouseNum(row1)<=row2+1 && getWarehouseNum(row2)<=row1+1){
            if(getWarehouseNum(row1)>=getWarehouseNum(row2)) exchange(row1, row2);
            else exchange(row2, row1);
            return true;
        }
        else return false;
    }

    /** this method does the exchange if checkExchange allows it
     * @param row1 the first column that player want change
     * @param row2 the second column that player want to change
     */
    public void exchange(int row1, int row2){
        Resources[] vet = new Resources[warehouse[row1].length];
        for(int i=0; i<getWarehouseNum(row1); i++){
            vet[i] = warehouse[row1][i];
            warehouse[row1][i] = warehouse[row2][i];
            warehouse[row2][i] = vet[i];
        }

    }

    /**
     * this method delete the resources inside the warehouse when player use them
     * @param res the resources you want delete.
     */
    public boolean delete(Resources res){
        int i = 0;
        int j = warehouse.length-1;

        while(warehouse[i][0]==null || !res.getClass().equals(warehouse[i][0].getClass()) || i>warehouse.length-1){
            i++;
        }
        if(i>warehouse.length-1) return false;
        else{
            while (warehouse[i][j]==null) j--;
            warehouse[i][j]=null;
            return true;
        }
    }

    /**
     * this method add the effect of activeted LeaderCard
     * @param resources type of resource stored
     */
    public void addleaderCardEffect(Resources resources) {
        leaderCardEffect.add(new ExtraChest(resources));
    }

    /**
     * this method is a getter of the number of a resource in the warehouse
     * @param res type of resource
     * @return the number of the selected resouce
     */
    public int getNumResources(Resources res){
        int sum=0;
        int j=0;
        int i=0;
        for (ExtraChest chest: leaderCardEffect){
            if(chest.getResources().getClass().equals(res.getClass())){
                sum+=chest.getnum();
            }
        }

        for(int x=0;x<sizex;x++){
            if(warehouse[x][0]!= null && warehouse[x][0].getClass().equals(res.getClass())){
                while(warehouse[x][j]!=null && j<sizey)j++;
            }
            sum+=j;
            j=0;
        }
        return sum;
    }

    public List<ExtraChest> getLeaderCardEffect(){
        return leaderCardEffect;
    }

    public Resources[][] getWarehouse() {
        return warehouse;
    }
}