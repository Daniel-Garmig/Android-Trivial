package com.daniel.androidtrivial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.daniel.androidtrivial.Fragments.App.InfoDialogFragment;
import com.daniel.androidtrivial.Game.Utils.AssetManager;
import com.daniel.androidtrivial.Model.Questions.JSON.QuestionJSON;
import com.daniel.androidtrivial.Model.Questions.JSON.QuestionListJSON;
import com.daniel.androidtrivial.Model.Questions.RoomDB.Category;
import com.daniel.androidtrivial.Model.Questions.RoomDB.CategoryDAO;
import com.daniel.androidtrivial.Model.Questions.RoomDB.Question;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionDAO;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionDatabase;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionOption;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionOptionDAO;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init ThreadOrchestrator.
        ThreadOrchestrator.getInstance();
        QuestionsManager.getInstance().init(getApplicationContext());

        //FIXME: Cargar los assets en otro thread podría generar una condición de carrera en la peor de las situaciones al necesitarse nada más crear el GameFragment.
        // Asegurar eso.
        //AssetManager.getInstance().loadAssets(this);
        ThreadOrchestrator.getInstance().startThread("loadGameAssets", new Runnable() {
            @Override
            public void run() {
                AssetManager.getInstance().loadAssets(getApplicationContext());
                ThreadOrchestrator.getInstance().sendDataLoaded(ThreadOrchestrator.msgAssetsLoaded);
            }
        });

        ThreadOrchestrator.getInstance().setOnDBCreationError(new Runnable() {
            @Override
            public void run() {
                InfoDialogFragment infoDg = InfoDialogFragment.newInstance(getString(R.string.error_dbCreation_error), "");
                infoDg.show(getSupportFragmentManager(), "DBError");
            }
        });

        ThreadOrchestrator.getInstance().setOnDBOperationError(new Runnable() {
            @Override
            public void run() {
                InfoDialogFragment infoDg = InfoDialogFragment.newInstance(getString(R.string.error_dbOperation_error), "");
                infoDg.show(getSupportFragmentManager(), "DBError");
            }
        });




        //TODO: Comprobar que existe la DB de preguntas y que no hay ninguna nueva versión.
        //roomTest();
    }

    @Override
    public void onBackPressed()
    {
        //TODO: Do smt depending on active fragment.
        //super.onBackPressed();
    }


    //TODO: REMOVE ALL THIS TESTS!!
    /*private void roomTest()
    {
        QuestionsManager.getInstance().init(getApplicationContext());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //addCats();
                //addQuestions();
                //addOptions();
                QuestionDatabase db = QuestionsManager.getInstance().getDb();

                //List<QuestionWithOptions> list = db.questionDAO().getQuestionWithOptionByID(1, 1);

                Log.i("DBTEST", "Ahhhhh!");

                Gson json = new Gson();
                String rs = json.toJson(list);
                Log.i("DBTEST", "Ahora... A JSON!!!");

                //createTestJSON();
                //deserializeJSONTest();
            }
        });
        t.start();
    }

    private void addCats()
    {
        QuestionDatabase db = QuestionsManager.getInstance().getDb();

        CategoryDAO catDAO = db.categoryDAO();

        Category cat1 = new Category();
        cat1.ID = 1;
        cat1.name = "Ciencias";
        catDAO.insertCat(cat1);

        Category cat2 = new Category();
        cat2.ID = 2;
        cat2.name = "Deportes";
        catDAO.insertCat(cat2);

        Category cat3 = new Category();
        cat3.ID = 3;
        cat3.name = "Histoira";
        catDAO.insertCat(cat3);
    }

    private void addQuestions()
    {
        QuestionDatabase db = QuestionsManager.getInstance().getDb();

        QuestionDAO qDAO = db.questionDAO();

        Question q1 = new Question();
        q1.ID = 1;
        q1.sentence = "s1";
        q1.ID_Cat = 1;
        qDAO.insertQuest(q1);

        Question q2 = new Question();
        q2.ID = 2;
        q2.sentence = "s2";
        q2.ID_Cat = 1;
        qDAO.insertQuest(q2);

        Question q3 = new Question();
        q3.ID = 1;
        q3.sentence = "s3";
        q3.ID_Cat = 2;
        qDAO.insertQuest(q3);
    }

    private void addOptions()
    {
        QuestionDatabase db = QuestionsManager.getInstance().getDb();

        QuestionOptionDAO optionDAO = db.optionDAO();

        QuestionOption op1 = new QuestionOption();
        op1.ID = 1;
        op1.ID_Question = 1;
        op1.ID_Cat = 1;
        op1.answer = "Pepe";
        optionDAO.insertOption(op1);

        QuestionOption op2 = new QuestionOption();
        op2.ID = 2;
        op2.ID_Question = 1;
        op2.ID_Cat = 1;
        op2.answer = "Juan";
        optionDAO.insertOption(op2);

        QuestionOption op3 = new QuestionOption();
        op3.ID = 1;
        op3.ID_Question = 2;
        op3.ID_Cat = 1;
        op3.answer = "La luna";
        optionDAO.insertOption(op3);
    }

    private void createTestJSON()
    {
        QuestionsManager.getInstance().init(getApplicationContext());

        QuestionListJSON listJSON = new QuestionListJSON();

        Category cat1 = new Category();
        cat1.ID = 1;
        cat1.name = "Ciencias";
        listJSON.category = cat1;

        QuestionJSON qJSON1 = new QuestionJSON();

        Question q1 = new Question();
        q1.ID = 1;
        q1.sentence = "s1";
        q1.ID_Cat = 1;
        qJSON1.question = q1;

        QuestionOption op1 = new QuestionOption();
        op1.ID = 1;
        op1.ID_Question = 1;
        op1.ID_Cat = 1;
        op1.answer = "Pepe";
        qJSON1.optionList.add(op1);

        QuestionOption op2 = new QuestionOption();
        op2.ID = 2;
        op2.ID_Question = 1;
        op2.ID_Cat = 1;
        op2.answer = "Juan";
        qJSON1.optionList.add(op2);

        listJSON.questions.add(qJSON1);

        Gson json = QuestionsManager.getInstance().getJson();
        String rs = json.toJson(listJSON, QuestionListJSON.class);

        Log.i("TESTJSON", "asdesad");
    }

    private void deserializeJSONTest()
    {
        QuestionsManager.getInstance().init(getApplicationContext());

        Gson json = QuestionsManager.getInstance().getJson();

        String value = "{\"category\":{\"ID\":1,\"name\":\"Ciencias\"},\"questions\":[{\"optionList\":[{\"ID\":1,\"answer\":\"Pepe\"},{\"ID\":2,\"answer\":\"Juan\"}],\"question\":{\"ID\":1,\"ID_CorrectAnswer\":0,\"sentence\":\"s1\"}}]}";

        QuestionListJSON listJSON = json.fromJson(value, QuestionListJSON.class);

        Log.i("TESTJSON", "asdesad");

    }*/

}