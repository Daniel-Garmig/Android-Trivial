package com.daniel.androidtrivial.Fragments.Game;

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

import com.daniel.androidtrivial.Fragments.App.InfoDialogFragment;
import com.daniel.androidtrivial.Fragments.App.LoadingDialogFragment;
import com.daniel.androidtrivial.Fragments.App.MainMenuFragment;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.Game.GameData;
import com.daniel.androidtrivial.Game.GameViewModel;
import com.daniel.androidtrivial.Game.MyGame;
import com.daniel.androidtrivial.Game.MyHandler;
import com.daniel.androidtrivial.Model.GameState;
import com.daniel.androidtrivial.R;
import com.daniel.androidtrivial.ThreadOrchestrator;
import com.uberelectron.androidrtg.RTG_Surface;

public class GameFragment extends Fragment
{
    private static final String TAG = "GameFragment";

    LoadingDialogFragment loadingDialog;

    GameViewModel viewModel;
    RTG_Surface rtg_surface;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //When all thread ends loading data, game can start securely.
        ThreadOrchestrator.getInstance().setOnAllDataLoaded(new Runnable() {
            @Override
            public void run() {
                loadingDialog.onFinishLoad();
                loadingDialog.dismiss();
                initGame();
            }
        });

        ThreadOrchestrator.getInstance().setOnRTG_GameEndsInteraction(new Runnable() {
            @Override
            public void run() {
                onGameInteractionEnded();
            }
        });

        //FIXME: Quizás esto no va aquí...
        loadingDialog = LoadingDialogFragment.newInstance("Loading Game!");
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
        }

        loadingDialog.show(getParentFragmentManager(), "Loading");

        loadData();
        initComponents(frag);

        return frag;
    }

    private void loadData()
    {
        //Load Board data.
        Thread boardLoad = new Thread(new Runnable() {
            @Override
            public void run() {
                GameData.getInstance().loadBoardData(getContext());
                ThreadOrchestrator.getInstance().sendDataLoaded(ThreadOrchestrator.msgBoardDataLoaded);
            }
        });
        boardLoad.start();

        if(viewModel.getStage() == GameState.LoadedGame)
        {
            //TODO: Load data from JSON to viewModel.
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
    }


    private void initRTGApp(RTG_Surface surface)
    {
        surface.setApp(MyGame.class);
        surface.setHandlerClass(MyHandler.class);

        //Add events to surface.
        rtg_surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rtg_surface.getThread().getHandler(MyHandler.class).sendTouch();
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



    private void initGame()
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
            case NextTurn:
                //We will get here after question fragment.
                //FIXME: This case mb Question instead.
                nextTurnState();
                break;
        }

    }


    //GAME STATE LOGIC

    private void startGameState()
    {
        viewModel.setStage(GameState.StartGame);

        InfoDialogFragment dg = InfoDialogFragment.newInstance("Welcome!", "Nueva partida iniciada!");
        dg.setBtActions(new Runnable() {
            @Override
            public void run() {
                nextTurnState();
            }
        });
        dg.show(getParentFragmentManager(), "Info");

        //TODO: This should start a new game on RTG_APP.
        //Enviar movs al gameThread.
        rtg_surface.getThread().getHandler(MyHandler.class).sendCreatePlayers(viewModel.getPlayers());
        rtg_surface.getThread().getHandler(MyHandler.class).sendUpdatePositions(viewModel.getPlayerPositions());
    }

    private void loadedGameState()
    {
        viewModel.setStage(GameState.LoadedGame);

        InfoDialogFragment.newInstance("Welcome Back!", "Juego cargado con éxito.");

        //TODO: Pasar los datos cargados al RTG_App.
    }

    private void nextTurnState()
    {
        viewModel.setStage(GameState.NextTurn);

        viewModel.nextTurn();
        int currentPlayerID = viewModel.getCurrentPlayerID();

        //GetPlayer
        Player p = viewModel.getPlayer(currentPlayerID);

        InfoDialogFragment dg = InfoDialogFragment.newInstance("Turno de " + p.getName(), "Empieza un nuevo turno.");
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

        Player p = viewModel.getPlayer(viewModel.getCurrentPlayerID());

        DiceRollFragment dg = DiceRollFragment.newInstance("Turno: " + p.getName());
        dg.setBtActions(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Mover mucho: " + viewModel.getDiceRoll());
                moveState();
            }
        });
        dg.show(getParentFragmentManager(), "DiceRoll");
    }

    private void moveState()
    {
        viewModel.setStage(GameState.Move);

        int movs = viewModel.getDiceRoll();
        //Enviar movs al gameThread.
        rtg_surface.getThread().getHandler(MyHandler.class).sendStartMoveState(movs, viewModel.getCurrentPlayer());
    }




    private void onGameInteractionEnded()
    {
        GameState state = viewModel.getStage();

        if(state == GameState.Move)
        {
            //Update viewModelData with movement.
            int playerID = viewModel.getCurrentPlayerID();
            int updatedPosition = GameData.getInstance().getPlayerSquareID(playerID);
            viewModel.getPlayerPositions().put(playerID, updatedPosition);

            nextTurnState();
        }
    }
}
