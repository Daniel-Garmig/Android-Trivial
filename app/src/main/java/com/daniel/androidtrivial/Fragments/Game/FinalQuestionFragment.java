package com.daniel.androidtrivial.Fragments.Game;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.daniel.androidtrivial.Fragments.App.InfoDialogFragment;
import com.daniel.androidtrivial.Fragments.App.LoadingDialogFragment;
import com.daniel.androidtrivial.Fragments.App.MainMenuFragment;
import com.daniel.androidtrivial.Fragments.App.MatchSummaryFragment;
import com.daniel.androidtrivial.MatchManager;
import com.daniel.androidtrivial.Model.GameState;
import com.daniel.androidtrivial.Model.GameViewModel;
import com.daniel.androidtrivial.Model.MatchRecord.MatchRecordDatabase;
import com.daniel.androidtrivial.Model.MatchRecord.MatchStats;
import com.daniel.androidtrivial.Model.MatchRecord.MathStatsDAO;
import com.daniel.androidtrivial.Model.MatchRecord.PlayerStats;
import com.daniel.androidtrivial.Model.MatchRecord.PlayerStatsDAO;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.Model.Questions.RoomDB.Category;
import com.daniel.androidtrivial.Model.Questions.RoomDB.Question;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionDAO;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionDatabase;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionOption;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionWithOptions;
import com.daniel.androidtrivial.QuestionsManager;
import com.daniel.androidtrivial.R;
import com.daniel.androidtrivial.ThreadOrchestrator;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FinalQuestionFragment extends Fragment
{

    private static final int POINTS_ON_CORRECT_ANSWER = 5;

    private static final long QUESTION_TIME_MS = 10 * 1000;
    private static final long TIME_BETWEEN_UPDATES = 30;


    GameViewModel viewModel;
    CountDownTimer timer = null;

    LoadingDialogFragment loadingDialog;

    LinearLayout layoutOptions;
    ProgressBar timeBar;
    TextView textTitle;

    private boolean timerFinished = false;

    //Questions with categoryID.
    HashMap<Integer, QuestionWithOptions> questionsToAnswer;
    ArrayDeque<Category> categoriesToAnswer;

    int currentCatID = -1;
    int correctAnswers = 0;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ThreadOrchestrator.getInstance().setOnQuestionQueryEnded(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
                nextQuestion();
            }
        });

        ThreadOrchestrator.getInstance().setOnMatchRecordQueryEnded(new Runnable() {
            @Override
            public void run() {
                exitGame();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View frag =  inflater.inflate(R.layout.fragment_question, container, false);

        //Get game ViewModel.
        FragmentActivity act = getActivity();
        if(act != null)
        {
            viewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);
            //TODO: MB create error msg and return to menu?
        }

        initComponents(frag);

        return frag;
    }

    @Override
    public void onDestroyView() {
        if(timer != null) { timer.cancel(); }
        super.onDestroyView();
    }

    private void initComponents(View v)
    {
        loadingDialog = LoadingDialogFragment.newInstance(getString(R.string.question_loading_final_questions));
        loadingDialog.show(getParentFragmentManager(), "GenerateQuestions");

        layoutOptions = v.findViewById(R.id.quest_layout_options);

        textTitle = v.findViewById(R.id.quest_text_title);

        timeBar = v.findViewById(R.id.quest_timeBar);
        timeBar.setProgress(100);

        questionsToAnswer = new HashMap<>();
        categoriesToAnswer = new ArrayDeque<>();
        correctAnswers = 0;
        generateAllQuestions();
    }


    private void nextQuestion()
    {
        if(categoriesToAnswer.isEmpty())  { endGame(); return; }

        //Get next Cat from Queue.
        Category cat = categoriesToAnswer.pop();
        currentCatID = cat.ID;
        InfoDialogFragment info = InfoDialogFragment.newInstance(String.format(getString(R.string.question_final_nextcat_title), cat.name),
                getString(R.string.question_final_nextcat_text));
        info.setBtActions(new Runnable() {
            @Override
            public void run() {
                loadQuestionUI();
                startTimer();
            }
        });
        info.show(getParentFragmentManager(), "infoCategory");
    }


    private void loadQuestionUI()
    {
        // Get question from viewModel.
        QuestionWithOptions question = questionsToAnswer.get(currentCatID);
        textTitle.setText(question.question.sentence);

        //Add option button for each option.
        //Shuffle list so order is random.
        List<QuestionOption> unorderedOptions = question.optionList;
        Collections.shuffle(unorderedOptions);
        layoutOptions.removeAllViews();
        for(QuestionOption option : unorderedOptions)
        {
            addOpButton(option);
        }
    }

    private void startTimer()
    {
        timeBar.setProgress(100);
        timer = new CountDownTimer(QUESTION_TIME_MS, TIME_BETWEEN_UPDATES) {
            @Override
            public void onTick(long millisUntilFinished)
            {
                //Calculate percentage.
                float progress = (float)millisUntilFinished / (float)QUESTION_TIME_MS * 100;
                timeBar.setProgress(Math.round(progress));
            }

            @Override
            public void onFinish() {
                timeBar.setProgress(0);
                timerFinished = true;
                onTimeEnds();
            }
        };
        timer.start();
    }

    private void addOpButton(QuestionOption option)
    {
        Button bt = new Button(getContext());
        bt.setText(option.answer);
        bt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionClick(option.ID);
            }
        });

        layoutOptions.addView(bt);
    }

    private void onOptionClick(int opId)
    {
        if(timerFinished) { return; }
        timer.cancel();

        //TODO: Add win stuff...
        String dialogTitle = "null";
        String dialogText = "";

        Player p = viewModel.getCurrentPlayer();
        p.addQuestionAnswered();

        Question q = questionsToAnswer.get(currentCatID).question;
        int correctOpID = q.ID_CorrectAnswer;
        dialogText = q.additionalInformation;

        if(opId == correctOpID)
        {
            //Add score.
            p.addScorePoints(POINTS_ON_CORRECT_ANSWER);
            p.addCorrectAnswer();
            correctAnswers++;

            dialogTitle = getString(R.string.question_info_correct_normal_title);
        }
        else
        {
            dialogTitle = getString(R.string.question_info_error_title);
        }

        //Show info and return to game fragment.
        infoAndNext(dialogTitle, dialogText);
    }


    private void onTimeEnds()
    {
        String dialogTitle = "null";
        String dialogText = "";

        Player p = viewModel.getCurrentPlayer();
        //FIXME: Mb addQuestionAnswered as well?
        p.addQuestionOutOfTime();

        dialogTitle = getString(R.string.question_info_outOfTIme_title);
        dialogText = viewModel.getCurrentQuestion().question.additionalInformation;

        infoAndNext(dialogTitle, dialogText);
    }

    private void infoAndNext(String title, String text)
    {
        //Show info and return to game fragment.
        InfoDialogFragment infodg = InfoDialogFragment.newInstance(title, text);
        infodg.setBtActions(new Runnable() {
            @Override
            public void run() {
                nextQuestion();
            }
        });
        infodg.show(getParentFragmentManager(), "infoQuestion");
    }

    private void generateAllQuestions()
    {
        ThreadOrchestrator.getInstance().startThread("GenerateFinalQuestions", new Runnable() {
            @Override
            public void run() {
                for(int catID : viewModel.getColorsCategories().values())
                {
                    generateQuestion(catID);
                }

                ThreadOrchestrator.getInstance().sendQuestionQueryEnded();
            }
        });

        Log.i("asd", "asd");
    }

    //TO BE CALLED BY OTHER THREAD!!
    private void generateQuestion(int catID)
    {
        QuestionDatabase db = QuestionsManager.getInstance().getDb();

        //Get Category.
        Category cat = db.categoryDAO().getCatByID(catID);
        if(cat == null) { ThreadOrchestrator.getInstance().sendDBOperationError("No se ha podido obtener la categoría."); }

        //TODO: Buscar forma para no tener que cargar todas, sólo la necesaria.
        //  Teniendo en cuenta que los IDs no tienen porqué ser continuos.
        //  Puedes tener 10 preguntas, pero la que está en posición 5 tener ID 8.

        //Get Question List.
        QuestionDAO qDAO = db.questionDAO();
        List<Question> questions = qDAO.getQuestionsByCategory(catID);

        //Select random question.
        Random gen = new Random();
        int pos = gen.nextInt(questions.size());

        //Get Question.
        QuestionWithOptions myQuestion = new QuestionWithOptions();
        myQuestion.question = questions.get(pos);
        //Get Options.
        myQuestion.optionList = db.optionDAO().getOptionsByQuestion(cat.ID, questions.get(pos).ID);

        categoriesToAnswer.addLast(cat);
        questionsToAnswer.put(cat.ID, myQuestion);
    }


    private void endGame()
    {
        //Check player won.
        if(correctAnswers >= 4)
        {
            winGame();
            return;
        }

        //If not... Next turn and return.
        viewModel.setStage(GameState.NextTurn);
        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, GameFragment.class, null)
                .commit();
    }

    private void winGame()
    {
        String matchName = viewModel.getMatchName();

        //TODO: Show error.
        if(matchName == null) { return; }

        //Remove save file!
        MatchManager.getInstance().removeSavedMatch(matchName + ".json");
        Log.i("FinalQuestion", "Game Save cleared!");


        //Add Match Stats to DB.
        ThreadOrchestrator.getInstance().startThread("addMatchStatsToDB", new Runnable() {
            @Override
            public void run() {
                MatchStats matchStats = new MatchStats();
                matchStats.name = matchName;

                //FIXME: Move this stuff to manager class or smth
                MatchRecordDatabase db = MatchManager.getInstance().getDb();

                //Add Match
                MathStatsDAO mathStatsDAO = db.mathStatsDAO();
                long matchId = mathStatsDAO.insertMatchStats(matchStats);

                //Add Players.
                PlayerStatsDAO playerStatsDAO = db.playerStatsDAO();
                for(Player p : viewModel.getPlayers())
                {
                    PlayerStats ps = PlayerStats.createFromPlayer(p);
                    ps.ID_Match = (int) matchId;
                    playerStatsDAO.insertPlayerStats(ps);
                }
                //Set the ID so summary load this data.
                MatchManager.getInstance().setMatchStatsIDToLoad((int) matchId);

                ThreadOrchestrator.getInstance().sendMatchRecordQueryEnded();
            }
        });
    }

    private void exitGame()
    {
        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, MatchSummaryFragment.class, null)
                .commit();
    }
}

