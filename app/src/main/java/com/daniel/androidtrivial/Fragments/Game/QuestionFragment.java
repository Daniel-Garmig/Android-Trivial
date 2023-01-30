package com.daniel.androidtrivial.Fragments.Game;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.daniel.androidtrivial.Model.GameViewModel;
import com.daniel.androidtrivial.Model.GameState;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.Model.Questions.RoomDB.Question;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionOption;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionWithOptions;
import com.daniel.androidtrivial.R;

import java.util.Collections;
import java.util.List;

public class QuestionFragment extends Fragment
{
    private static final int POINTS_ON_CORRECT_ANSWER = 5;

    private static final long QUESTION_TIME_MS = 10 * 1000;
    private static final long TIME_BETWEEN_UPDATES = 30;


    GameViewModel viewModel;

    CountDownTimer timer = null;

    LinearLayout layoutOptions;
    ProgressBar timeBar;

    private boolean timerFinished = false;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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

        layoutOptions = v.findViewById(R.id.quest_layout_options);

        // Get question from viewModel.
        QuestionWithOptions question = viewModel.getCurrentQuestion();

        TextView title = v.findViewById(R.id.quest_text_title);
        title.setText(question.question.sentence);

        timeBar = v.findViewById(R.id.quest_timeBar);
        timeBar.setProgress(100);
        startTimer();

        //Add option button for each option.
        //Shuffle list so order is random.
        List<QuestionOption> unorderedOptions = question.optionList;
        Collections.shuffle(unorderedOptions);
        for(QuestionOption option : unorderedOptions)
        {
            addOpButton(option);
        }

    }

    private void startTimer()
    {
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

        Question q = viewModel.getCurrentQuestion().question;
        int correctOpID = q.ID_CorrectAnswer;
        dialogText = q.additionalInformation;

        if(opId == correctOpID)
        {

            //Add score.
            p.addScorePoints(POINTS_ON_CORRECT_ANSWER);
            p.addCorrectAnswer();

            dialogTitle = getString(R.string.question_info_correct_normal_title);

            //Switch state.
            viewModel.setStage(GameState.RollDice);

            //If is wedge square, give it to the player.
            if(viewModel.isCurrentQuestionQuesito())
            {
                dialogTitle = String.format(getString(R.string.question_info_correct_quesito_title), viewModel.getCurrentCategory().name);

                p.setWedge(viewModel.getCurrentColor(), true);
                viewModel.setStage(GameState.NextTurn);
            }
        }
        else
        {
            dialogTitle = getString(R.string.question_info_error_title);
            viewModel.setStage(GameState.NextTurn);
        }

        //Show info and return to game fragment.
        infoAndReturn(dialogTitle, dialogText);
    }


    private void onTimeEnds()
    {
        String dialogTitle = "null";
        String dialogText = "";

        Player p = viewModel.getCurrentPlayer();
        //FIXME: Mb addQuestionAnswered as well?
        p.addQuestionOutOfTime();
        viewModel.setStage(GameState.NextTurn);

        dialogTitle = getString(R.string.question_info_outOfTIme_title);
        dialogText = viewModel.getCurrentQuestion().question.additionalInformation;

        infoAndReturn(dialogTitle, dialogText);
    }

    private void infoAndReturn(String title, String text)
    {
        //Show info and return to game fragment.
        InfoDialogFragment infodg = InfoDialogFragment.newInstance(title, text);
        infodg.setBtActions(new Runnable() {
            @Override
            public void run() {
                //Return to GameFragment.
                FragmentManager mng = getParentFragmentManager();
                mng.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.MainFragmentContainer, GameFragment.class, null)
                        .commit();
            }
        });
        infodg.show(getParentFragmentManager(), "infoQuestion");
    }

}
