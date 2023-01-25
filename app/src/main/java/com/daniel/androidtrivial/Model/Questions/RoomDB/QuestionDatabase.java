package com.daniel.androidtrivial.Model.Questions.RoomDB;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RenameColumn;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;

@Database(entities = {Category.class, Question.class, QuestionOption.class}, version = 5,
        autoMigrations = {
                @AutoMigration (
                        from = 4,
                        to = 5,
                        spec = QuestionDatabase.MyAutoMigration.class
                )
        }, exportSchema = true)
public abstract class QuestionDatabase extends RoomDatabase
{
    public abstract CategoryDAO categoryDAO();
    public abstract QuestionDAO questionDAO();
    public abstract QuestionOptionDAO optionDAO();


    static class MyAutoMigration implements AutoMigrationSpec { }
}
