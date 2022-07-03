package com.wrd0wl.trackmypantry.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wrd0wl.trackmypantry.model.ItemData;

@Database(entities = {ItemData.class}, version = 1)
public abstract class PantryDatabase extends RoomDatabase {
    public static PantryDatabase INSTANCE;

    public abstract PantryDAO pantryDAO();

    public static PantryDatabase getDBInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PantryDatabase.class, "PantryDatabase")
                    .fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}
