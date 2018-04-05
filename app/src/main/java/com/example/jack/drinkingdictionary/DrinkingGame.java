package com.example.jack.drinkingdictionary;

/**
 * Created by jackhorsburgh on 22/03/2018.
 */

public class DrinkingGame {
    private int gameId;
    private String gameName;
    private String descShort;
    private String descLong;
    private String difficulty;
    private String imageRef;

    public DrinkingGame(String gameName, String descShort, String descLong, String difficulty, String imageRef) {
        this.gameName = gameName;
        this.descShort = descShort;
        this.descLong = descLong;
        this.difficulty = difficulty;
        this.imageRef = imageRef;
    }

    public DrinkingGame(){}


    public DrinkingGame(int gameId, String gameName, String descShort, String descLong, String difficulty) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.descShort = descShort;
        this.descLong = descLong;
        this.difficulty = difficulty;
    }

    public DrinkingGame(int gameId, String gameName, String descShort, String descLong, String difficulty, String imageRef) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.descShort = descShort;
        this.descLong = descLong;
        this.difficulty = difficulty;
        this.imageRef = imageRef;
    }



    @Override
    public String toString() {
        return "DrinkingGame{" +
                "gameId=" + gameId +
                ", gameName='" + gameName + '\'' +
                ", descShort='" + descShort + '\'' +
                ", descLong='" + descLong + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getDescShort() {
        return descShort;
    }

    public void setDescShort(String descShort) {
        this.descShort = descShort;
    }

    public String getDescLong() {
        return descLong;
    }

    public void setDescLong(String descLong) {
        this.descLong = descLong;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
