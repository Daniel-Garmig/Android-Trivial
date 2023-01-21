package com.daniel.androidtrivial.Model.Questions.RoomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity
public class Category
{
    @Expose
    @PrimaryKey
    public int ID;

    @Expose
    public String name;
}
