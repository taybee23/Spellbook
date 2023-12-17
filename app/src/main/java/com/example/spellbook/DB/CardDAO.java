package com.example.spellbook.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.spellbook.Card;
import com.example.spellbook.Deck;
import com.example.spellbook.DeckCard;
import com.example.spellbook.User;

import java.util.List;

@Dao
public interface CardDAO {

    @Insert
    void insert(Card...cards);

    @Update
    void update(Card...cards);

    @Delete
    void delete(Card card);

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE)
    List<Card> getCards();

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE mCardId = :cardId")
    List<Card> getCardById(int cardId);

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE mUserId = :userId")
    List<Card> getCardByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE (mCardName = :name OR :name IS NULL) " + " OR (mCardManaCost = :color OR :color IS NULL) " + " OR (mCardType = :type OR :type IS NULL)" + " OR (mCardRarity = :rarity OR :rarity IS NULL)")
    List<Card> searchCollection(String name, String type, String color, String rarity);

    @Insert
    void insert(User...users);

    @Update
    void update(User...users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM " + AppDatabase.USER_TABLE + " WHERE mUserName = :username")
    void deleteUserByUsername(String username);

    @Query("DELETE FROM " + AppDatabase.CARD_TABLE + " WHERE mUserId = :userId")
    void deleteCardsByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);

    @Insert
    void insert(Deck...decks);

    @Update
    void update(Deck...decks);

    @Delete
    void delete (Deck deck);

    @Query(" DELETE FROM " + AppDatabase.DECK_TABLE + " WHERE mUserId = :userId")
    void deleteDecksByUserId(int userId);

    @Query(" SELECT * FROM " + AppDatabase.DECK_TABLE + " WHERE mUserId = :userId")
    List<Deck> getDeckByUserId(int userId);

    @Query(" SELECT mDeckName FROM " + AppDatabase.DECK_TABLE + " WHERE mDeckId = :deckId")
    String getDeckNameByDeckId(int deckId);

    @Query("SELECT mDeckName FROM " + AppDatabase.DECK_TABLE + " WHERE mUserId = :userId")
    String getDeckNameByUserId(int userId);

    @Query(" SELECT mDeckId FROM " + AppDatabase.DECK_TABLE + " WHERE mUserId = :userId LIMIT 1")
    int getDeckIdByUserId(int userId);

    @Insert
    void insert(DeckCard deckCard);

    @Query(" SELECT * FROM " + AppDatabase.CARD_TABLE + " INNER JOIN " + AppDatabase.DECK_CARD_TABLE + " ON mCardId = cardId WHERE deckId = :deckId")
    List<Card> getCardsByDeckId(int deckId);

    @Query(" DELETE FROM " + AppDatabase.DECK_CARD_TABLE + " WHERE deckId = :deckId AND cardId = :cardId")
    void deleteCardFromDeck(int deckId, int cardId);

    @Query(" SELECT COUNT(*) FROM " + AppDatabase.DECK_CARD_TABLE + " WHERE cardId = :cardId")
    int countByCardId(int cardId);
}
