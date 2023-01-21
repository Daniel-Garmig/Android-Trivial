package com.daniel.androidtrivial;

import android.content.Context;

import androidx.room.Room;

import com.daniel.androidtrivial.Fragments.App.InfoDialogFragment;
import com.daniel.androidtrivial.Game.GameData;
import com.daniel.androidtrivial.Model.Questions.JSON.QuestionJSON;
import com.daniel.androidtrivial.Model.Questions.JSON.QuestionListJSON;
import com.daniel.androidtrivial.Model.Questions.RoomDB.Question;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionDatabase;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionOption;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.util.Scanner;

public class QuestionsManager
{
    // SINGLETON.
    private static QuestionsManager instance;

    public static QuestionsManager getInstance()
    {
        //Singleton with double-checked locking (DCL).

        QuestionsManager questMng = instance;
        if(questMng != null) { return questMng; }

        synchronized (QuestionsManager.class)
        {
            if(instance == null)
            {
                instance = new QuestionsManager();
            }
            return instance;
        }
    }

    private QuestionsManager() {}

    // END SINGLETON.

    private Context appContext;

    private boolean isInit = false;

    private QuestionDatabase db;
    private Gson json;


    public void init(Context context)
    {
        if(isInit) { return; }
        isInit = true;

        this.appContext = context;

        //Create DB.
        db = Room.databaseBuilder(context, QuestionDatabase.class, "questions").build();
        json = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }


    public QuestionDatabase getDb() { return db; }

    public Gson getJson() { return json; }


    public void loadCategory()
    {

    }

    public boolean createDefaultDB(int[] resources)
    {
        if(resources == null || resources.length == 0) { return false; }
        if(appContext == null) { return false; }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int res : resources)
                {
                    try
                    {
                        InputStream is = appContext.getResources().openRawResource(res);
                        String data = new Scanner(is).useDelimiter("\\A").next();
                        is.close();

                        QuestionListJSON questionList = json.fromJson(data, QuestionListJSON.class);

                        //If cat already exist, override it!
                        db.categoryDAO().deleteCat(questionList.category);

                        //Add Category to DB.
                        db.categoryDAO().insertCat(questionList.category);

                        //Add Questions to DB.
                        int id_cat = questionList.category.ID;
                        for (QuestionJSON qJSON : questionList.questions)
                        {
                            qJSON.question.ID_Cat = id_cat;
                            db.questionDAO().insertQuest(qJSON.question);

                            //Add Options to DB.
                            int id_quest = qJSON.question.ID;
                            for(QuestionOption qo : qJSON.optionList)
                            {
                                qo.ID_Cat = id_cat;
                                qo.ID_Question = id_quest;

                                db.optionDAO().insertOption(qo);
                            }
                        }

                    }
                    catch (Exception e)
                    {
                        ThreadOrchestrator.getInstance().sendDBCreationError("");
                    }

                    ThreadOrchestrator.getInstance().sendDBCreationSuccess("");
                }
            }
        });
        t.start();

        return true;
    }
}
