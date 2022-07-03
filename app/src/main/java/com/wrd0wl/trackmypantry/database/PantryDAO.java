package com.wrd0wl.trackmypantry.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wrd0wl.trackmypantry.model.ItemData;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PantryDAO {
    @Query("SELECT * FROM ItemData WHERE email = :email")
    List<ItemData> get(String email);

    @Query("SELECT * FROM ItemData WHERE email = :email AND id = :id")
    ItemData getById(String email, String id);

    @Query("SELECT * FROM ItemData WHERE email = :email AND name LIKE :name")
    List<ItemData> getByName(String email, String name);

    @Query("SELECT * FROM ItemData WHERE email = :email AND category IN (:categories)")
    List<ItemData> getByCategories(String email, ArrayList<String> categories);

    @Query("SELECT * FROM ItemData WHERE email = :email AND quantity == 0")
    List<ItemData> getRanOut(String email);

    @Query("SELECT * FROM ItemData WHERE email = :email AND expiry_date < :date")
    List<ItemData> getExpired(String email, String date);

    @Query("SELECT * FROM ItemData WHERE email = :email AND expiry_date >= :today AND expiry_date <= :week")
    List<ItemData> getExpiring(String email, String today, String week);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemData... item);

    @Update()
    void update(ItemData... item);

    @Delete()
    void delete(ItemData... item);
}
