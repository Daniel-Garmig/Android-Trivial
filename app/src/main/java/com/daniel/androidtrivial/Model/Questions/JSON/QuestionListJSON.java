package com.daniel.androidtrivial.Model.Questions.JSON;

import com.daniel.androidtrivial.Model.Questions.RoomDB.Category;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class QuestionListJSON
{
    @Expose
    public Category category;

    @Expose
    public List<QuestionJSON> questions = new ArrayList<>();

    public QuestionListJSON() {}

    public QuestionListJSON(Category category, List<QuestionJSON> questions)
    {
        this.category = category;
        this.questions = questions;
    }


}
