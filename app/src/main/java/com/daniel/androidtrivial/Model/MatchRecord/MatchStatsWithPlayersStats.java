package com.daniel.androidtrivial.Model.MatchRecord;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class MatchStatsWithPlayersStats
{
    @Embedded
    public MatchStats matchStats;

    @Relation(
            parentColumn = "ID",
            entityColumn = "ID_Match"
    )
    public List<PlayerStats> playerList;
}
