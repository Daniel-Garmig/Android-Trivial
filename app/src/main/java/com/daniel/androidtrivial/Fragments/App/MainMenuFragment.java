package com.daniel.androidtrivial.Fragments.App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniel.androidtrivial.Fragments.Game.DiceRollFragment;
import com.daniel.androidtrivial.Fragments.Game.GameFragment;
import com.daniel.androidtrivial.R;

public class MainMenuFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View frag =  inflater.inflate(R.layout.fragment_main_menu, container, false);

        initComponents(frag);

        return frag;
    }

    private void initComponents(View v)
    {
        Button btBack = v.findViewById(R.id.menu_bt_Play);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayButton();
            }
        });

        //DEBUG: Used for testing roll dice dialog.
        Button btRollDice = v.findViewById(R.id.menu_bt_rollDice);
        btRollDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debugRollDice();
            }
        });

        //DEBUG: Used for testing.
        Button btRoom = v.findViewById(R.id.menu_bt_room);
        btRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debugLoadRoom();
            }
        });
    }

    private void onPlayButton()
    {
        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, GameFragment.class, null)
                .commit();
    }

    //DEBUG: Used for testing roll dice dialog.
    private void debugRollDice()
    {
        DiceRollFragment diceRoll = DiceRollFragment.newInstance("Turno DEBUG!");
        diceRoll.show(getParentFragmentManager(), "diceRoll");
    }

    //DEBUG
    private void debugLoadRoom()
    {
        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, MatchRoomFragment.class, null)
                .commit();
    }
}
