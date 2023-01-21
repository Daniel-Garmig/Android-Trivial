package com.daniel.androidtrivial.Model.Questions.RoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDAO
{

    @Query("select * from Category")
    List<Category> getAll();

    @Query("select * from Category where ID = :catID")
    Category getCatByID(int catID);

    @Insert
    void insertCat(Category category);

    @Delete
    void deleteCat(Category category);
}
