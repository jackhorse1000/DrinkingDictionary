package com.example.jack.drinkingdictionary;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by jackhorsburgh on 15/03/2018.
 */

@Database(entities = {DrinkingGame.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DrinkingGameDao drinkingGameDao();
}
