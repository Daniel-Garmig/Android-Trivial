package com.daniel.androidtrivial.Model.MatchRecord;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MatchStats
{
    @PrimaryKey(autoGenerate = true)
    public int ID;

    public String name;
}
