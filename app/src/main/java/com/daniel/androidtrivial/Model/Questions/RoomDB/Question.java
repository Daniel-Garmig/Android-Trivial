package com.daniel.androidtrivial.Model.Questions.RoomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.google.gson.annotations.Expose;

@Entity(primaryKeys = {"ID", "ID_Cat"},
        foreignKeys =
            {
                @ForeignKey(entity = Category.class,
                        parentColumns = "ID", childColumns = "ID_Cat",
                        onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
            },
        indices =
            {
                @Index(value = "ID_CorrectAnswer"),
                @Index(value = "ID_Cat"),
                @Index(value = "ID")
            }
)
public class Question
{
    @Expose
    public int ID;

    @Expose
    public String sentence;
    @Expose
    public String additionalInformation;

    public int ID_Cat;

    @Expose
    @ColumnInfo(defaultValue = "1")
    public int ID_CorrectAnswer;

}
