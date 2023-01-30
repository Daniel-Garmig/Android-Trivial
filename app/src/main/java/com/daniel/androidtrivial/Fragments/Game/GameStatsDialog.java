package com.daniel.androidtrivial.Fragments.Game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.R;
import com.daniel.androidtrivial.Views.StatsPlayerItem;

import java.util.List;

public class GameStatsDialog extends DialogFragment
{

    LinearLayout playerListLayout;
    List<Player> players;

    public GameStatsDialog() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_DialogWhenLarge);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_stats_list, container, false);

        initComponents(v);

        return v;
    }

    private void initComponents(View v)
    {
        playerListLayout = v.findViewById(R.id.fg_stats_players_layout);
        if(players != null)
        {
            addPlayers();
        }
    }

    public void setPlayers(List<Player> players) { this.players = players; }

    private void addPlayers()
    {
        playerListLayout.removeAllViews();

        for (Player p : players)
        {
            StatsPlayerItem pItem = new StatsPlayerItem(getContext(), null);
            pItem.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            playerListLayout.addView(pItem);
            pItem.updateUIFromPlayer(p);
        }
        playerListLayout.invalidate();
    }

}
