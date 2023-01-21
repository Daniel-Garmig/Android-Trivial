package com.daniel.androidtrivial.Model.Questions.JSON;

import com.daniel.androidtrivial.Model.Questions.RoomDB.Question;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionOption;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class QuestionJSON
{
    @Expose
    public Question question;

    @Expose
    public List<QuestionOption> optionList = new ArrayList<>();


    public QuestionJSON() {}

    public QuestionJSON(Question question, List<QuestionOption> optionList)
    {
        this.question = question;
        this.optionList = optionList;
    }
}
