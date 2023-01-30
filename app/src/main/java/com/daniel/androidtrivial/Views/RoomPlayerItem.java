package com.daniel.androidtrivial.Views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.daniel.androidtrivial.Model.WedgesColors;
import com.daniel.androidtrivial.R;

public class RoomPlayerItem extends CardView
{
    public TextView playerName;
    public Button btEditPlayer;
    public Button btRemovePlayer;
    private ImageView playerIcon;
    public int playerID;

    public RoomPlayerItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_room_player, this, true);

        initComponents();
    }

    private void initComponents()
    {
        playerName = findViewById(R.id.view_room_player_name);
        btEditPlayer = findViewById(R.id.view_room_player_bt_edit);
        btRemovePlayer = findViewById(R.id.view_room_player_bt_remove);
        playerIcon = findViewById(R.id.view_room_player_icon);
    }


    public void setIconFromWedges(WedgesColors color)
    {
        switch (color)
        {
            case blue:
                playerIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.piece_blue_svg));
                break;
            case green:
                playerIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.piece_green_svg));
                break;
            case orange:
                playerIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.piece_orange_svg));
                break;
            case pink:
                playerIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.piece_pink_svg));
                break;
            case purple:
                playerIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.piece_purple_svg));
                break;
            case yellow:
                playerIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.piece_yellow_svg));
                break;
        }
    }

}
