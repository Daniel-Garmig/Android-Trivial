package com.daniel.androidtrivial.Model.MatchRecord;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.Model.WedgesColors;

import java.util.HashMap;

@Entity(primaryKeys = {"ID", "ID_Match"},
        foreignKeys =
            {
                @ForeignKey(entity = MatchStats.class,
                        parentColumns = "ID", childColumns = "ID_Match",
                        onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
            }
)
public class PlayerStats
{
    //DATA

    public int ID;

    public int ID_Match;

    public String name;
    public String color;

    //Let's be "bad". This is like this "blue,red,green".
    //It indicates if a player had that color.
    public String ownedWedges;

    public int score;
    public int squaresTraveled;
    public int questionsAnswered;
    public int correctAnswers;
    public int questionsOutOfTime;

    //HELPERS

    public static PlayerStats createFromPlayer(Player p)
    {
        PlayerStats ps = new PlayerStats();
        ps.ID = p.getId();
        ps.name = p.getName();
        ps.color = p.getPlayerColor().name();

        //Owned Wedges.
        StringBuilder ownedWedgesSB = new StringBuilder();
        for(WedgesColors c : WedgesColors.values())
        {
            if(p.haveWedge(c)) { ownedWedgesSB.append(c.name()).append(";"); }
        }
        ps.ownedWedges = ownedWedgesSB.toString();

        ps.score = p.getScore();
        ps.squaresTraveled = p.getSquaresTraveled();
        ps.questionsAnswered = p.getQuestionsAnswered();
        ps.correctAnswers = p.getCorrectAnswers();
        ps.questionsOutOfTime = p.getQuestionsOutOfTime();

        return ps;
    }
}