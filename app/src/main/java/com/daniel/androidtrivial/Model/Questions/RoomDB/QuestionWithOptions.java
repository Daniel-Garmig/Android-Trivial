package com.daniel.androidtrivial.Model.Questions.RoomDB;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class QuestionWithOptions
{
    @Embedded
    public Question question;

    @Relation(
            parentColumn = "ID",
            entityColumn = "ID_Question"
    )
    public List<QuestionOption> optionList;
}
