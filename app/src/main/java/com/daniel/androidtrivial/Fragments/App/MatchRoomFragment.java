package com.daniel.androidtrivial.Fragments.App;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.daniel.androidtrivial.Fragments.Game.GameFragment;
import com.daniel.androidtrivial.Game.GameData;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.Model.WedgesColors;
import com.daniel.androidtrivial.Model.GameViewModel;
import com.daniel.androidtrivial.Model.GameState;
import com.daniel.androidtrivial.R;
import com.daniel.androidtrivial.ThreadOrchestrator;
import com.daniel.androidtrivial.Views.RoomPlayerItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MatchRoomFragment extends Fragment
{
    GameViewModel viewModel;

    HashMap<WedgesColors, Boolean> colorAvailable;

    Button btPlay;
    FloatingActionButton btAddPlayer;

    LinearLayout playerListLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_match_room, container, false);

        playerListLayout = v.findViewById(R.id.fg_romm_layout_playerList);

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
        viewModel.setPlayers(new ArrayList<>());

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
        btAddPlayer = v.findViewById(R.id.fg_room_bt_genPlayers);
        btAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePlayer();
            }
        });

    }


    private void generatePlayer()
    {
        ArrayList<Player> playerList = viewModel.getPlayers();

        //Limit 6 players.
        if(playerList.size() >= 6)
        {
            return;
        }

        //NOTE: Player ID starts at 0.
        int nextID = 0;
        if(playerList.size() != 0)
        {
            nextID = playerList.get(playerList.size()-1).getId() + 1;
        }
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

        //Set all wedges to false.
        for(WedgesColors c : WedgesColors.values())
        {
            p.setWedge(c, false);
        }

        //Add player to player list.
        playerList.add(p);

        //TODO: Instantiate player card.
        RoomPlayerItem playerListItem = new RoomPlayerItem(getContext(), null);
        playerListLayout.addView(playerListItem);

        playerListItem.playerID = p.getId();
        playerListItem.playerName.setText(p.getName());
        playerListItem.setIconFromWedges(p.getPlayerColor());

        playerListItem.btRemovePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerList.remove(p);
                playerListLayout.removeView(playerListItem);
            }
        });
    }

    private void generateRandomOrder()
    {
        ArrayList<Player> playerList = viewModel.getPlayers();

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

    private void useDefaultColorCategories()
    {
        HashMap<WedgesColors, Integer> colorsCategories = new HashMap<>();
        colorsCategories.put(WedgesColors.green, 1);
        colorsCategories.put(WedgesColors.purple, 2);
        colorsCategories.put(WedgesColors.orange, 3);
        colorsCategories.put(WedgesColors.yellow, 4);
        colorsCategories.put(WedgesColors.pink, 5);
        colorsCategories.put(WedgesColors.blue, 6);

        viewModel.setColorsCategories(colorsCategories);
    }


    private void initGame()
    {
        ArrayList<Player> playerList = viewModel.getPlayers();

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

        //TODO: Create color-category relation for the given match and story it on viewModel.
        useDefaultColorCategories();

        //Create Save File.
        saveNewMatch();

        //Reset gameData vars
        GameData.getInstance().gameDataResets();
        //Send ViewModel loaded.
        ThreadOrchestrator.getInstance().sendDataLoaded(ThreadOrchestrator.msgViewModelDataLoaded);

        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, GameFragment.class, null)
                .commit();
    }


    private void saveNewMatch()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH_mm", Locale.US);
        String name = format.format(Calendar.getInstance().getTime());
        viewModel.setMatchName(name);

        //SavedMatch save = viewModel.generateSave();
        //MatchManager.getInstance().saveMatchData(save);

        Log.i("MatchRoom", "Create new save");
    }


    private void returnOnError()
    {
        Toast.makeText(getContext(), getString(R.string.error_matchRoom_error), Toast.LENGTH_LONG).show();
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
