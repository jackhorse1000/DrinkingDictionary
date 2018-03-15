package com.example.jack.drinkingdictionary;

/**
 * Created by jackhorsburgh on 26/01/2018.
 */

public class DrinkingGame
{

    // Fields
    private int GameId;
    private String Name;
    private String backgroundImage;
    private String fullImage;
    private String descriptionShort;
    private String descriptionLong;
    private String difficulty;
    private String categories;
    private String link;

    public DrinkingGame(int gameId,
                        String name,
                        String backgroundImage,
                        String fullImage,
                        String descriptionShort,
                        String descriptionLong,
                        String difficulty,
                        String categories,
                        String link)
    {
        GameId = gameId;
        Name = name;
        this.backgroundImage = backgroundImage;
        this.fullImage = fullImage;
        this.descriptionShort = descriptionShort;
        this.descriptionLong = descriptionLong;
        this.difficulty = difficulty;
        this.categories = categories;
        this.link = link;
    }

    public void setGameId(int gameId) {
        GameId = gameId;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setFullImage(String fullImage) {
        this.fullImage = fullImage;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public void setDescriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getGameId() {
        return GameId;
    }

    public String getName() {
        return Name;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getFullImage() {
        return fullImage;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategories() {
        return categories;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "DrinkingGame{" +
                "GameId=" + GameId +
                ", Name='" + Name + '\'' +
                ", backgroundImage='" + backgroundImage + '\'' +
                ", fullImage='" + fullImage + '\'' +
                ", descriptionShort='" + descriptionShort + '\'' +
                ", descriptionLong='" + descriptionLong + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", categories='" + categories + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

}
