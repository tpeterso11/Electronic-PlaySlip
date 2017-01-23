package models;

import java.util.ArrayList;

/**
 * Created by toussaintpeterson on 1/11/16.
 */
public class CartObject {
    private String gameName;
    private String quantity;
    private String cost;
    private ArrayList<Ticket> games;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public ArrayList<Ticket> getGames() {
        return games;
    }

    public void setGames(ArrayList<Ticket> games) {
        this.games = games;
    }
}
