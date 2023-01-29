package com.daniel.androidtrivial.Model.MatchRecord;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface PlayerStatsDAO
{
    @Query("select * from playerstats")
    List<PlayerStats> getAll();

    @Query("select * from playerstats where ID_Match = :id_Match")
    List<PlayerStats> getPlayersByMatch(int id_Match);


    @Insert
    void insertPlayerStats(PlayerStats playerStats);

    @Delete
    void deletePlayerStats(PlayerStats playerStats);
}
