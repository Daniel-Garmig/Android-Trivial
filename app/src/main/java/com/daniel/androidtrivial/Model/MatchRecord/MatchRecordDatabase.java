package com.daniel.androidtrivial.Model.MatchRecord;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MatchStats.class, PlayerStats.class}, version = 1)
public abstract class MatchRecordDatabase extends RoomDatabase
{
    public abstract MathStatsDAO mathStatsDAO();
    public abstract PlayerStatsDAO playerStatsDAO();
}
