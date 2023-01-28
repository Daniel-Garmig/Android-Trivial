package com.daniel.androidtrivial.Model.MatchRecord;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MathStatsDAO
{
    @Query("select * from MatchStats")
    List<MatchStats> getAll();

    @Query("select * from MatchStats where ID = :matchID")
    MatchStats getMatchByID(int matchID);


    @Insert
    long insertMatchStats(MatchStats matchStats);

    @Delete
    void deleteMatchStats(MatchStats matchStats);

}
