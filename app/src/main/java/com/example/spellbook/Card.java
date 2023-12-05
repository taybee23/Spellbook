package com.example.spellbook;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.spellbook.DB.AppDatabase;

@Entity(tableName = AppDatabase.CARD_TABLE)
public class Card {

    @PrimaryKey(autoGenerate = true)
    private int mCardId;

    private String mCardName;
    private String mCardType;
    private String mCardManaCost;
    private String mCardRarity;
    private String mCardTextBox;

    private int mUserId;

    public Card(String cardName, String cardType, String cardManaCost, String cardRarity, String cardTextBox, int userId) {
        mCardName = cardName;
        mCardType = cardType;
        mCardManaCost = cardManaCost;
        mCardRarity = cardRarity;
        mCardTextBox = cardTextBox;

        mUserId = userId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    @Override
    public String toString() {
        return "Card # " + mCardId + "\n" +
                "Card Name: " + mCardName + "\n" +
                "Card Type: " + mCardType + "\n" +
                "Card Mana Cost: " + mCardManaCost + "\n" +
                "Card Rarity: " + mCardRarity + "\n" +
                "Card Text: " + mCardTextBox + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n";
    }

    public int getCardId() {
        return mCardId;
    }

    public void setCardId(int cardId) {
        mCardId = cardId;
    }

    public String getCardName() {
        return mCardName;
    }

    public void setCardName(String cardName) {
        mCardName = cardName;
    }

    public String getCardType() {
        return mCardType;
    }

    public void setCardType(String cardType) {
        mCardType = cardType;
    }

    public String getCardManaCost() {
        return mCardManaCost;
    }

    public void setCardManaCost(String cardManaCost) {
        mCardManaCost = cardManaCost;
    }

    public String getCardRarity() {
        return mCardRarity;
    }

    public void setCardRarity(String cardRarity) {
        mCardRarity = cardRarity;
    }

    public String getCardTextBox() {
        return mCardTextBox;
    }

    public void setCardTextBox(String cardTextBox) {
        mCardTextBox = cardTextBox;
    }
}
