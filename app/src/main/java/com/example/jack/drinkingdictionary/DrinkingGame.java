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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String timestamp;

    public DrinkingGame(String gameName, String descShort, String descLong, String difficulty, String imageRef) {
        this.gameName = gameName;
        this.descShort = descShort;
        this.descLong = descLong;
        this.difficulty = difficulty;
        this.imageRef = imageRef;
    }

    public DrinkingGame(){}


    public DrinkingGame(int gameId, String gameName, String descShort, String descLong, String difficulty, String timestamp) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.descShort = descShort;
        this.descLong = descLong;
        this.difficulty = difficulty;
        this.timestamp = timestamp;
    }

    public DrinkingGame(int gameId,
                        String gameName,
                        String descShort,
                        String descLong,
                        String difficulty,
                        String imageRef,
                        String timestamp) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.descShort = descShort;
        this.descLong = descLong;
        this.difficulty = difficulty;
        this.imageRef = imageRef;
        this.timestamp = timestamp;
    }



    @Override
    public String toString() {
        return "DrinkingGame{" +
                "gameId=" + gameId +
                ", gameName='" + gameName + '\'' +
                ", descShort='" + descShort + '\'' +
                ", descLong='" + descLong + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", timestamp='" + timestamp + '\'' +
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
