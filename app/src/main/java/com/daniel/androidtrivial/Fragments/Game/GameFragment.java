package com.daniel.androidtrivial.Fragments.Game;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniel.androidtrivial.Fragments.App.MainMenuFragment;
import com.daniel.androidtrivial.Game.GameData;
import com.daniel.androidtrivial.Game.MyGame;
import com.daniel.androidtrivial.R;
import com.uberelectron.androidrtg.RTG_Surface;

public class GameFragment extends Fragment
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
        View frag =  inflater.inflate(R.layout.fragment_game, container, false);

        initComponents(frag);

        return frag;
    }

    private void initComponents(View v)
    {
        //Init game and surface.
        RTG_Surface rtg_surface = v.findViewById(R.id.BoardSurface);
        initGame(rtg_surface);

        //Init other views.
        Button btBack = v.findViewById(R.id.game_bt_Back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReturnButton();
            }
        });
    }



    private void initGame(RTG_Surface surface)
    {
        surface.setApp(MyGame.class);

    }


    private void onReturnButton()
    {
        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, MainMenuFragment.class, null)
                .commit();
    }

}
