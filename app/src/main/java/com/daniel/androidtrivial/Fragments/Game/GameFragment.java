package com.daniel.androidtrivial.Fragments.Game;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.daniel.androidtrivial.Fragments.App.InfoDialogFragment;
import com.daniel.androidtrivial.Fragments.App.LoadingDialogFragment;
import com.daniel.androidtrivial.Fragments.App.MainMenuFragment;
import com.daniel.androidtrivial.Fragments.App.MatchSummaryFragment;
import com.daniel.androidtrivial.MatchManager;
import com.daniel.androidtrivial.Model.MatchRecord.MatchRecordDatabase;
import com.daniel.androidtrivial.Model.MatchRecord.MatchStats;
import com.daniel.androidtrivial.Model.MatchRecord.MathStatsDAO;
import com.daniel.androidtrivial.Model.MatchRecord.PlayerStats;
import com.daniel.androidtrivial.Model.MatchRecord.PlayerStatsDAO;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.Game.GameData;
import com.daniel.androidtrivial.Model.GameViewModel;
import com.daniel.androidtrivial.Game.MyGame;
import com.daniel.androidtrivial.Game.MyHandler;
import com.daniel.androidtrivial.Model.GameState;
import com.daniel.androidtrivial.Model.Questions.RoomDB.Category;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionDAO;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionDatabase;
import com.daniel.androidtrivial.Model.Questions.RoomDB.QuestionWithOptions;
import com.daniel.androidtrivial.Model.SavedMatch;
import com.daniel.androidtrivial.QuestionsManager;
import com.daniel.androidtrivial.R;
import com.daniel.androidtrivial.ThreadOrchestrator;
import com.uberelectron.androidrtg.RTG_Surface;

import java.util.List;
import java.util.Random;

public class GameFragment extends Fragment
{
    private static final String TAG = "GameFragment";

    LoadingDialogFragment loadingDialog;

    GameViewModel viewModel;
    RTG_Surface rtg_surface;

    boolean busy = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initEventHandler();

        //FIXME: Quizás esto no va aquí...
        loadingDialog = LoadingDialogFragment.newInstance(getString(R.string.game_loading_game));

        //Set event if match ends after match stats go into db.
        ThreadOrchestrator.getInstance().setOnMatchRecordQueryEnded(new Runnable() {
            @Override
            public void run() {
                onGameFinished();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View frag =  inflater.inflate(R.layout.fragment_game, container, false);

        //Get game ViewModel.
        FragmentActivity act = getActivity();
        if(act != null)
        {
            viewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);
            //TODO: Mb create error MSG?
        }

        loadingDialog.show(getParentFragmentManager(), "Loading");

        loadData();
        initComponents(frag);

        return frag;
    }

    private void initEventHandler()
    {
        //When all thread ends loading data, game can start securely.
        ThreadOrchestrator.getInstance().setOnAllDataLoaded(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
                startGameLoop();
            }
        });

        ThreadOrchestrator.getInstance().setOnRTG_GameEndsInteraction(new Runnable() {
            @Override
            public void run() {
                onGameInteractionEnded();
            }
        });

        ThreadOrchestrator.getInstance().setOnQuestionQueryEnded(new Runnable() {
            @Override
            public void run() {
                onQuestionQueryEnded();
            }
        });
    }

    private void loadData()
    {
        //Load Board data.
        ThreadOrchestrator.getInstance().startThread("boardLoading", new Runnable() {
            @Override
            public void run() {
                GameData.getInstance().loadBoardData(getContext());
                ThreadOrchestrator.getInstance().sendDataLoaded(ThreadOrchestrator.msgBoardDataLoaded);
            }
        });

        //Autosave game if not just loaded.
        if(viewModel.getMatchName() != null &&
                viewModel.getStage() != GameState.StartGame &&viewModel.getStage() != GameState.LoadedGame)
        {
            //TODO: Test it works fine.
            ThreadOrchestrator.getInstance().startThread("SaveGame", new Runnable() {
                @Override
                public void run() {
                    SavedMatch save = viewModel.generateSave();
                    MatchManager.getInstance().saveMatchData(save);

                    ThreadOrchestrator.getInstance().sendDataLoaded(ThreadOrchestrator.msgMatchSaved);
                }
            });
        } else
        {
            ThreadOrchestrator.getInstance().sendDataLoaded(ThreadOrchestrator.msgMatchSaved);
        }

        //Load game.
        if(viewModel.getStage() == GameState.LoadedGame)
        {
            ThreadOrchestrator.getInstance().startThread("LoadGame", new Runnable() {
                @Override
                public void run() {
                    SavedMatch save = MatchManager.getInstance().loadMatchData();
                    viewModel.loadSave(save);

                    ThreadOrchestrator.getInstance().sendDataLoaded(ThreadOrchestrator.msgViewModelDataLoaded);
                }
            });
        }
    }

    private void initComponents(View v)
    {
        //Init game and surface.
        rtg_surface = v.findViewById(R.id.BoardSurface);
        initRTGApp(rtg_surface);


        //Init other views.
        Button btBack = v.findViewById(R.id.game_bt_Back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReturnButton();
            }
        });

        Button btEnd = v.findViewById(R.id.game_bt_endGame);
        btEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGameEnds();
            }
        });
    }


    private void initRTGApp(RTG_Surface surface)
    {
        surface.setApp(MyGame.class);
        surface.setHandlerClass(MyHandler.class);

        //Add events to surface.
        rtg_surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rtg_surface.getThread().getHandler(MyHandler.class).sendTouch(event);
                return true;
            }
        });
    }


    private void onReturnButton()
    {
        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, MainMenuFragment.class, null)
                .commit();
    }



    private void startGameLoop()
    {

        //TODO: Obtener datos del ViewModel para actualizar el UI.

        //State Logic.
        GameState state = viewModel.getStage();
        switch (state)
        {
            case StartGame:
                startGameState();
                break;
            case LoadedGame:
                loadedGameState();
                break;
            case RollDice:
                //We will get here after successful question.
                onPlayerAnsweredCorrectly();
                //rollDiceState(); -> It's done on the method above.
                break;
            case NextTurn:
                //We will get here after wrong question answer.
                nextTurnState();
                break;
        }

    }

    private void onPlayerAnsweredCorrectly()
    {
        Player p = viewModel.getCurrentPlayer();
        if(p.haveALllWedges())
        {
            InfoDialogFragment infodg = InfoDialogFragment.newInstance(getString(R.string.game_info_win_chance_title), getString(R.string.game_info_win_chance_msg));
            infodg.setBtActions(new Runnable() {
                @Override
                public void run() {
                    rollDiceState();
                }
            });
            infodg.show(getParentFragmentManager(), "InfoWinChance");
        }
        else
        {
            rollDiceState();
        }
    }


    //GAME STATE LOGIC

    private void startGameState()
    {
        viewModel.setStage(GameState.StartGame);

        InfoDialogFragment dg = InfoDialogFragment.newInstance(getString(R.string.game_info_new_game_title), getString(R.string.game_info_new_game_text));
        dg.setBtActions(new Runnable() {
            @Override
            public void run() {
                nextTurnState();
            }
        });
        dg.show(getParentFragmentManager(), "Info");

        //Enviar movs al gameThread.
        rtg_surface.getThread().getHandler(MyHandler.class).sendCreatePlayers(viewModel.getPlayers());
        rtg_surface.getThread().getHandler(MyHandler.class).sendUpdatePositions(viewModel.getPlayerPositions());
    }

    private void loadedGameState()
    {
        viewModel.continueLoadMatch();

        InfoDialogFragment dg = InfoDialogFragment.newInstance(getString(R.string.game_info_load_game_title),
                getString(R.string.game_info_load_game_text));
        dg.setBtActions(new Runnable() {
            @Override
            public void run() {
                switch (viewModel.getStage())
                {
                    case NextTurn:
                        nextTurnState();
                        break;
                    case RollDice:
                        rollDiceState();
                        break;
                    case Move:
                        moveState();
                        break;
                    case Question:
                        questionState();
                        break;
                    default:
                        onReturnButton();
                }
            }
        });
        dg.show(getParentFragmentManager(), "LoadInfo");

        //Enviar movs al gameThread.
        rtg_surface.getThread().getHandler(MyHandler.class).sendCreatePlayers(viewModel.getPlayers());
        rtg_surface.getThread().getHandler(MyHandler.class).sendUpdatePositions(viewModel.getPlayerPositions());
    }

    private void nextTurnState()
    {
        viewModel.setStage(GameState.NextTurn);

        viewModel.nextTurn();
        int currentPlayerID = viewModel.getCurrentPlayerID();

        //GetPlayer
        Player p = viewModel.getPlayer(currentPlayerID);

        InfoDialogFragment dg = InfoDialogFragment.newInstance(String.format(getString(R.string.game_info_nextturn_title),
                p.getName()), getString(R.string.game_info_nextturn_text));
        dg.setBtActions(new Runnable() {
            @Override
            public void run() {
                rollDiceState();
            }
        });
        dg.show(getParentFragmentManager(), "Info");

        Log.i(TAG, "viewModel state: " + viewModel.getStage().name());
    }


    private void rollDiceState()
    {
        viewModel.setStage(GameState.RollDice);

        rtg_surface.getThread().getHandler(MyHandler.class).sendRollDiceState();
    }

    private void moveState()
    {
        viewModel.setStage(GameState.Move);

        int movs = viewModel.getDiceRoll();
        //Enviar movs al gameThread.
        rtg_surface.getThread().getHandler(MyHandler.class).sendStartMoveState(movs, viewModel.getCurrentPlayer());
    }

    private void questionState()
    {
        viewModel.updateCurrentQuestion();

        Runnable getQuestionTask = new Runnable() {
            @Override
            public void run() {
                try
                {
                    int categoryID = viewModel.getColorsCategories().get(viewModel.getCurrentColor());

                    QuestionDatabase db = QuestionsManager.getInstance().getDb();

                    //Get Category.
                    Category cat = db.categoryDAO().getCatByID(categoryID);
                    if(cat == null) { ThreadOrchestrator.getInstance().sendDBOperationError("No se ha podido obtener la categoría."); }
                    viewModel.setCurrentCategory(cat);

                    //TODO: Buscar forma para no tener que cargar todas, sólo la necesaria.
                    //  Teniendo en cuenta que los IDs no tienen porqué ser continuos.
                    //  Puedes tener 10 preguntas, pero la que está en posición 5 tener ID 8.

                    QuestionDAO qDAO = db.questionDAO();
                    List<QuestionWithOptions> questions = qDAO.getQuestionsWithOptionsByCategory(categoryID);

                    //Select random question.
                    Random gen = new Random();
                    int pos = gen.nextInt(questions.size());

                    viewModel.setCurrentQuestion(questions.get(pos));

                    ThreadOrchestrator.getInstance().sendQuestionQueryEnded();
                }
                catch (Exception e)
                {
                    ThreadOrchestrator.getInstance().sendDBOperationError(e.toString());
                }
            }
        };

        ThreadOrchestrator.getInstance().startThread("Question Querry", getQuestionTask);

        loadingDialog = LoadingDialogFragment.newInstance(getString(R.string.game_loading_question));
        loadingDialog.show(getParentFragmentManager(), "LoadingQuestion");
    }




    private void onQuestionQueryEnded()
    {
        loadingDialog.dismiss();

        String title = String.format(getString(R.string.game_info_question_title), viewModel.getCurrentCategory().name);
        String msg = (viewModel.isCurrentQuestionQuesito() ? getString(R.string.game_info_question_quesito) : getString(R.string.game_info_question_noQuesito));
        InfoDialogFragment dg = InfoDialogFragment.newInstance(title, msg);

        dg.setBtActions(new Runnable() {
            @Override
            public void run() {
                busy = false;
                FragmentManager mng = getParentFragmentManager();
                mng.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.MainFragmentContainer, QuestionFragment.class, null)
                        .commit();
            }
        });
        dg.show(getParentFragmentManager(), "QuestionInfo");
    }


    private void onGameInteractionEnded()
    {
        GameState state = viewModel.getStage();

        if(busy) { return; }

        if(state == GameState.RollDice)
        {
            busy = true;
            onRollDiceReturn();
        }

        if(state == GameState.Move)
        {
            busy = true;
            onMovementReturn();
        }
    }


    private void onRollDiceReturn()
    {
        Player p = viewModel.getCurrentPlayer();

        DiceRollFragment dg = DiceRollFragment.newInstance(String.format(getString(R.string.game_info_roll_title), p.getName()));
        dg.setBtActions(new Runnable() {
            @Override
            public void run() {
                viewModel.getCurrentPlayer().addSquaresTraveled(viewModel.getDiceRoll());
                moveState();
                busy = false;
            }
        });
        dg.show(getParentFragmentManager(), "DiceRoll");
    }

    private void onMovementReturn()
    {
        //Update viewModelData with movement from GameThread.
        int playerID = viewModel.getCurrentPlayerID();
        int updatedPosition = GameData.getInstance().getPlayerSquareID(playerID);
        viewModel.getPlayerPositions().put(playerID, updatedPosition);


        //If player have all wedges and is in square 0 -> finalQuestionsState.
        Player p = viewModel.getCurrentPlayer();
        if(p.haveALllWedges() && viewModel.getPlayerPositions().get(playerID) == 0)
        {
            //TODO: Go to final questions for win chance.
        }


        //Question state.
        questionState();
    }



    private void onGameEnds()
    {
        Context context = getContext();
        String matchName = viewModel.getMatchName();

        //TODO: Show error.
        if(matchName == null) { return; }

        //Remove save file!
        MatchManager.getInstance().removeSavedMatch(matchName + ".json");
        Log.i(TAG, "Game Save cleared!");


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
                MatchManager.getInstance().matchStatsIDToLoad = (int) matchId;

                ThreadOrchestrator.getInstance().sendMatchRecordQueryEnded();
            }
        });
    }

    private void onGameFinished()
    {
        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, MatchSummaryFragment.class, null)
                .commit();
    }
}
