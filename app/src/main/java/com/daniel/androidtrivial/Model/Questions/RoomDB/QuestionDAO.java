package com.daniel.androidtrivial.Model.Questions.RoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface QuestionDAO
{
    @Query("select * from question")
    List<Question> getAll();

    @Query("select * from question where ID_Cat = :categoryID")
    List<Question> getQuestionsByCategory(int categoryID);

    @Query("select count(ID) from question where ID_Cat = :categoryID")
    int getQuestionsCountByCategory(int categoryID);

    @Transaction
    @Query("select * from question")
    List<QuestionWithOptions> getQuestionsWithOptions();

    @Transaction
    @Query("select * from question where ID_Cat = :idCat")
    List<QuestionWithOptions> getQuestionsWithOptionsByCategory(int idCat);

    @Transaction
    @Query("select * from question where ID = :idQuestion AND ID_Cat = :idCat")
    List<QuestionWithOptions> getQuestionWithOptionByID(int idCat, int idQuestion);


    @Insert
    void insertQuest(Question question);

    @Delete
    void deleteQuestion(Question question);

}
