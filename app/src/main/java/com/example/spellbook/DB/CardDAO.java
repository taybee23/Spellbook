package com.example.spellbook.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.spellbook.Card;
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

    @Query("DELETE FROM " + AppDatabase.USER_TABLE + " WHERE mUserName = :username")
    void deleteUserByUsername(String username);

    @Query("DELETE FROM " + AppDatabase.CARD_TABLE + " WHERE mUserId = :userId")
    void deleteCardsByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE)
    List<Card> getCards();

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE mCardId = :cardId")
    List<Card> getCardById(int cardId);

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE mUserId = :userId")
    List<Card> getCardByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE mCardName = :name")
    List<Card> getCardByName(String name);

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE mCardType = :type")
    List<Card> getCardByType(String type);

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE mCardRarity = :rarity")
    List<Card> getCardByRarity(String rarity);

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE mCardManaCost = :color")
    List<Card> getCardByColor(String color);

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE (mCardName = :name OR :name IS NULL) " + " OR (mCardManaCost = :color OR :color IS NULL) " + " OR (mCardType = :type OR :type IS NULL)" + " OR (mCardRarity = :rarity OR :rarity IS NULL)")
    List<Card> searchCollection(String name, String type, String color, String rarity);

    @Insert
    void insert(User...users);

    @Update
    void update(User...users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);
}
