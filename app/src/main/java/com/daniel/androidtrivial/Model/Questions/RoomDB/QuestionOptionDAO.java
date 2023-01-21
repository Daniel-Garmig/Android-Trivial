package com.daniel.androidtrivial.Model.Questions.RoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionOptionDAO
{
    @Query("select * from QuestionOption")
    List<QuestionOption> getAll();

    @Query("select * from QuestionOption where ID_Cat = :catID AND ID_Question = :questionID")
    List<QuestionOption> getOptionsByQuestion(int catID, int questionID);

    @Insert
    void insertOption(QuestionOption option);

    @Delete
    void deleteOption(QuestionOption option);
}
