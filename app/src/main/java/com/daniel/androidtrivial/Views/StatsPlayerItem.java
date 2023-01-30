package com.daniel.androidtrivial.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.daniel.androidtrivial.Model.MatchRecord.PlayerStats;
import com.daniel.androidtrivial.Model.Player;
import com.daniel.androidtrivial.Model.WedgesColors;
import com.daniel.androidtrivial.R;

public class StatsPlayerItem extends CardView
{
    private ConstraintLayout expandedLayout;


    public ImageView playerIcon;

    public TextView playerName;
    public TextView textStatsScore;
    public TextView textStatsSquares;
    public TextView textStatsQuestions;
    public TextView textStatsCorrectAnswers;

    public ImageButton btExpand;

    private ImageView img_wedge_blue;
    private ImageView img_wedge_green;
    private ImageView img_wedge_orange;
    private ImageView img_wedge_yellow;
    private ImageView img_wedge_purple;
    private ImageView img_wedge_pink;

    //TODO: ImageView Wedges.

    private boolean expanded = false;


    public StatsPlayerItem(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_player_stats_item, this, true);

        initComponents();
    }

    private void initComponents()
    {
        playerName = findViewById(R.id.view_summary_text_playerName);
        textStatsScore = findViewById(R.id.view_summary_text_score);
        textStatsSquares = findViewById(R.id.view_summary_text_squares);
        textStatsQuestions = findViewById(R.id.view_summary_text_questions);
        textStatsCorrectAnswers = findViewById(R.id.view_summary_text_correctAnswers);
        playerIcon = findViewById(R.id.view_summary_image_player_color);

        img_wedge_blue = findViewById(R.id.view_summary_img_wedge_blue);
        img_wedge_blue.setVisibility(GONE);
        img_wedge_green = findViewById(R.id.view_summary_img_wedge_green);
        img_wedge_green.setVisibility(GONE);
        img_wedge_orange = findViewById(R.id.view_summary_img_wedge_orange);
        img_wedge_orange.setVisibility(GONE);
        img_wedge_yellow = findViewById(R.id.view_summary_img_wedge_yellow);
        img_wedge_yellow.setVisibility(GONE);
        img_wedge_purple = findViewById(R.id.view_summary_img_wedge_purple);
        img_wedge_purple.setVisibility(GONE);
        img_wedge_pink = findViewById(R.id.view_summary_img_wedge_pink);
        img_wedge_pink.setVisibility(GONE);

        expandedLayout = findViewById(R.id.view_summary_expanded_layout);
        expandedLayout.setVisibility(GONE);

        btExpand = findViewById(R.id.view_summary_ibt_expand);
        btExpand.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleExpand();
            }
        });
    }


    private void toggleExpand()
    {
        expanded = !expanded;
        if(expanded) { expandedLayout.setVisibility(VISIBLE); }
        else { expandedLayout.setVisibility(GONE); }
    }

    public void updateUIFromPlayer(PlayerStats p)
    {
        this.playerName.setText(p.name);
        this.textStatsScore.setText(String.format(getContext().getString(R.string.summary_stats_score), p.score));
        this.textStatsSquares.setText(String.format(getContext().getString(R.string.summary_stats_squares), p.squaresTraveled));
        this.textStatsQuestions.setText(String.format(getContext().getString(R.string.summary_stats_questions), p.questionsAnswered));
        this.textStatsCorrectAnswers.setText(String.format(getContext().getString(R.string.summary_stats_correctAnswers), p.correctAnswers));
        this.setIconFromWedges(WedgesColors.valueOf(p.color));
        this.setOwnedWedges(p.ownedWedges);
    }

    public void updateUIFromPlayer(Player p)
    {
        this.playerName.setText(p.getName());
        this.textStatsScore.setText(String.format(getContext().getString(R.string.summary_stats_score), p.getScore()));
        this.textStatsSquares.setText(String.format(getContext().getString(R.string.summary_stats_squares), p.getSquaresTraveled()));
        this.textStatsQuestions.setText(String.format(getContext().getString(R.string.summary_stats_questions), p.getQuestionsAnswered()));
        this.textStatsCorrectAnswers.setText(String.format(getContext().getString(R.string.summary_stats_correctAnswers), p.getCorrectAnswers()));
        this.setIconFromWedges(p.getPlayerColor());
        for(WedgesColors c : WedgesColors.values())
        {
            if(p.haveWedge(c))
            {
                showWedgeAsOwned(c);
            }
        }
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

    public void setOwnedWedges(String wedgesList)
    {
        try
        {
            String[] splitedString = wedgesList.split(";");
            for (String s : splitedString)
            {
                if(s == null || s.isEmpty()) { continue; }
                WedgesColors c = WedgesColors.valueOf(s);
                showWedgeAsOwned(c);
            }
        }
        catch (Exception e)
        {
            Log.e("MatchSummaryItem", "Invalid OwnedWedgeData");
        }
    }

    private void showWedgeAsOwned(WedgesColors wedge)
    {
        switch (wedge)
        {
            case green:
                img_wedge_green.setVisibility(VISIBLE);
                break;
            case purple:
                img_wedge_purple.setVisibility(VISIBLE);
                break;
            case orange:
                img_wedge_orange.setVisibility(VISIBLE);
                break;
            case yellow:
                img_wedge_yellow.setVisibility(VISIBLE);
                break;
            case pink:
                img_wedge_pink.setVisibility(VISIBLE);
                break;
            case blue:
                img_wedge_blue.setVisibility(VISIBLE);
                break;
        }
    }
}

