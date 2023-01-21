package com.daniel.androidtrivial.Model.Questions.RoomDB;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryWithQuestions
{
    @Embedded
    public Category category;

    @Relation(
            parentColumn = "ID",
            entityColumn = "ID_cat"
    )
    public List<Question> questionList;
}
