package models;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by toussaintpeterson on 1/28/16.
 */
public class LandingCategory {
    private ArrayList<Game> games;
    private String imageUrl;
    private String categoryName;

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
