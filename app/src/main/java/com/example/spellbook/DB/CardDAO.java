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

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE)
    List<Card> getCards();

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE mCardId = :cardId")
    List<Card> getCardById(int cardId);

    @Query("SELECT * FROM " + AppDatabase.CARD_TABLE + " WHERE mUserId = :cardId")
    List<Card> getCardByUserId(int cardId);

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
