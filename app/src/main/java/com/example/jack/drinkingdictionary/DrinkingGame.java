package com.example.jack.drinkingdictionary;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by jackhorsburgh on 26/01/2018.
 */

@Entity
public class DrinkingGame
{

    // Fields
    @PrimaryKey
    private int GameId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "background_image")
    private String backgroundImage;
    @ColumnInfo(name = "full_image")
    private String fullImage;
    @ColumnInfo(name = "description_short")
    private String descriptionShort;
    @ColumnInfo(name = "description_long")
    private String descriptionLong;
    @ColumnInfo(name = "difficulty")
    private String difficulty;
    @ColumnInfo(name = "categories")
    private String categories;
    @ColumnInfo(name = "app_link")
    private String applink;

    public DrinkingGame(int gameId,
                        String name,
                        String backgroundImage,
                        String fullImage,
                        String descriptionShort,
                        String descriptionLong,
                        String difficulty,
                        String categories,
                        String applink)
    {
        GameId = gameId;
        name = name;
        this.backgroundImage = backgroundImage;
        this.fullImage = fullImage;
        this.descriptionShort = descriptionShort;
        this.descriptionLong = descriptionLong;
        this.difficulty = difficulty;
        this.categories = categories;
        this.applink = applink;
    }

    @Override
    public String toString() {
        return "DrinkingGame{" +
                "GameId=" + GameId +
                ", Name='" + name + '\'' +
                ", backgroundImage='" + backgroundImage + '\'' +
                ", fullImage='" + fullImage + '\'' +
                ", descriptionShort='" + descriptionShort + '\'' +
                ", descriptionLong='" + descriptionLong + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", categories='" + categories + '\'' +
                ", appLink='" + applink + '\'' +
                '}';
    }

    public int getGameId() {
        return GameId;
    }

    public void setGameId(int gameId) {
        GameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getFullImage() {
        return fullImage;
    }

    public void setFullImage(String fullImage) {
        this.fullImage = fullImage;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public void setDescriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }
}
