package com.daniel.androidtrivial.Model.Questions.RoomDB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.google.gson.annotations.Expose;

@Entity(primaryKeys = {"ID", "ID_Cat", "ID_Question"},
        foreignKeys =
                {
                    @ForeignKey(entity = Question.class,
                            parentColumns = {"ID", "ID_Cat"}, childColumns = {"ID_Question", "ID_Cat"},
                            onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
                },
        indices = @Index(value = {"ID_Question", "ID_Cat"})
)
public class QuestionOption
{
    @Expose
    public int ID;
    @Expose
    public String answer;

    public int ID_Cat;
    public int ID_Question;
}
