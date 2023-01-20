package com.daniel.androidtrivial.Fragments.App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.daniel.androidtrivial.Fragments.Game.GameFragment;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.Model.WedgesColors;
import com.daniel.androidtrivial.Game.GameViewModel;
import com.daniel.androidtrivial.Model.GameState;
import com.daniel.androidtrivial.R;
import com.daniel.androidtrivial.ThreadOrchestrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MatchRoomFragment extends Fragment
{
    GameViewModel viewModel;

    ArrayList<Player> playerList;
    HashMap<WedgesColors, Boolean> colorAvailable;

    Button btPlay;
    Button btGenPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_match_room, container, false);

        initData();
        initComponents(v);

        return v;
    }


    private void initData()
    {
        //FIXME: playerList debería vivir en el ViewModel para que no se reinicien los datos al recrear la app (girar, cerrar, ...).
        //Se puede plantear crear un viewModel para cada vista. Tener estos datos en un MatchRoomViewModel y luego moverlos al GameViewModel.

        FragmentActivity act = getActivity();
        if(act == null) { returnOnError(); }
        viewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);

        //Init available colors.
        colorAvailable = new HashMap<WedgesColors, Boolean>();
        colorAvailable.put(WedgesColors.blue, true);
        colorAvailable.put(WedgesColors.green, true);
        colorAvailable.put(WedgesColors.yellow, true);
        colorAvailable.put(WedgesColors.orange, true);
        colorAvailable.put(WedgesColors.pink, true);
        colorAvailable.put(WedgesColors.purple, true);

        //Create player.
        playerList = new ArrayList<>();

        //Generate default player.
        generatePlayer();
    }

    private void initComponents(View v)
    {

        Button btReturn = v.findViewById(R.id.fg_room_bt_return);
        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMenu();
            }
        });

        btPlay = v.findViewById(R.id.fg_room_bt_play);
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGame();
            }
        });

        //TODO: BT add Player -> Generate new player and add card.
        btGenPlayer = v.findViewById(R.id.fg_room_bt_genPlayers);
        btGenPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePlayer();
            }
        });

    }


    private void generatePlayer()
    {
        //Limit 6 players.
        if(playerList.size() >= 6)
        {
            return;
        }

        //NOTE: Player ID starts at 0.
        int nextID = playerList.size();
        Player p = new Player();
        p.setId(nextID);
        p.setName("Player " + nextID);

        //Get first available color.
        for(Map.Entry<WedgesColors, Boolean> pair : colorAvailable.entrySet())
        {
            if(pair.getValue())
            {
                p.setPlayerColor(pair.getKey());
                pair.setValue(false);
                break;
            }
        }

        //Add player to player list.
        playerList.add(p);

        //TODO: Instantiate player card.
    }

    private void generateRandomOrder()
    {
        //Shuffle a list of player ids.

        ArrayList<Integer> playerOrder = new ArrayList<>();
        for(int i = 0; i < playerList.size(); i++)
        {
            int id = playerList.get(i).getId();
            playerOrder.add(id);
        }

        Collections.shuffle(playerList, new Random());

        //Convert into primitive int[].
        int[] order = new int[playerOrder.size()];
        for(int i = 0; i < order.length; i++)
        {
            order[i] = playerOrder.get(i);
        }
        viewModel.setPlayerOrder(order);

    }


    private void initGame()
    {
        //Create Match and set data.
        viewModel.setPlayers(playerList);

        generateRandomOrder();

        //Set all players on start position.
        int startPos = 0;
        HashMap<Integer, Integer> playerPos = new HashMap<>();
        for(Player p : playerList)
        {
            playerPos.put(p.getId(), startPos);
        }
        viewModel.setPlayerPositions(playerPos);

        //viewModel.setStage(new StartGameState());
        viewModel.setStage(GameState.StartGame);

        ThreadOrchestrator.getInstance().onDataLoaded(ThreadOrchestrator.msgViewModelDataLoaded);

        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, GameFragment.class, null)
                .commit();
    }


    private void returnOnError()
    {
        //TO STRINGRES:
        Toast.makeText(getContext(), "Error en la sala de partida.", Toast.LENGTH_LONG).show();
        returnToMenu();
    }

    private void returnToMenu()
    {
        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.MainFragmentContainer, MainMenuFragment.class, null)
            .commit();
    }
}
