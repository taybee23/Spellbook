package com.example.spellbook.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.spellbook.Card;
import com.example.spellbook.User;

@Database(entities = {Card.class, User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "Card.db";
    public static final String CARD_TABLE = "card_table";
    public static final String USER_TABLE = "USER_TABLE";

    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract CardDAO CardDAO();

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
