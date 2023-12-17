package com.example.spellbook;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.spellbook.DB.AppDatabase;

@Entity(tableName = AppDatabase.DECK_TABLE)
public class Deck {

    @PrimaryKey(autoGenerate = true)
    private int mDeckId;

    private String mDeckName;

    private int mUserId;

    public int getDeckId() { return mDeckId; }

    public void setDeckId(int deckId) {
        mDeckId = deckId;
    }

    public int getUserId() {
        return mUserId;
    }

    public Deck(String deckName, int userId) {
        mDeckName = deckName;
        mUserId = userId;
    }

    public String getDeckName() {
        return mDeckName;
    }

    public void setDeckName(String deckName) {
        mDeckName = deckName;
    }

    @Override
    public String toString() {
        return "Deck Name: " + mDeckName + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n";
    }
}
