package com.example.spellbook;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.spellbook.Card;
import com.example.spellbook.DB.AppDatabase;
import com.example.spellbook.Deck;

@Entity(tableName = AppDatabase.DECK_CARD_TABLE,
        primaryKeys = {"deckId", "cardId"},
        foreignKeys = {
            @ForeignKey(entity = Deck.class,
                    parentColumns = "mDeckId",
                    childColumns = "deckId",
                    onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Card.class,
                    parentColumns = "mCardId",
                    childColumns = "cardId",
                    onDelete = ForeignKey.CASCADE)
        })
public class DeckCard {

    private int deckId;
    private int cardId;

    public DeckCard(int deckId, int cardId) {
        this.deckId = deckId;
        this.cardId = cardId;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}
