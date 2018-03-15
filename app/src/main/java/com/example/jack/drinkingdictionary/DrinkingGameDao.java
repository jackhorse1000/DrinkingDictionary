package com.example.jack.drinkingdictionary;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by jackhorsburgh on 15/03/2018.
 */

@Dao
public interface DrinkingGameDao {
    @Query("SELECT * FROM drinkinggame")
    List<DrinkingGame> getAll();

    @Query("SELECT * FROM drinkinggame WHERE categories = :category")
    public DrinkingGame[] loadAllCategoriesWhere(String category);

    @Query("SELECT * FROM drinkinggame WHERE GameId IN (:gameIds)")
    List<DrinkingGame> loadAllByIds(int[] userIds);

    @Query("SELECT name, description_short FROM DrinkingGame WHERE categories IN (:category)")
    public LiveData<List<DrinkingGame>> loadGamesFromCategorySync(List<String> category);

    @Insert
    void insertAll(DrinkingGame... drinkingGames);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertGames(DrinkingGame... drinkingGames);

    @Insert
    public void insertBothGames(DrinkingGame game1, DrinkingGame game2);

    @Update
    public void updateGame(DrinkingGame... drinkingGames);

    @Delete
    void delete(DrinkingGame drinkingGame);

}
