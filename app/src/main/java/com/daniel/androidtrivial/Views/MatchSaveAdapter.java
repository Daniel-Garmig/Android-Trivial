package com.daniel.androidtrivial.Views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.androidtrivial.MatchManager;
import com.daniel.androidtrivial.R;

import java.util.List;

public class MatchSaveAdapter extends RecyclerView.Adapter<MatchSaveAdapter.MatchSaveViewHolder>
{
    List<String> listOfSaves;
    public Runnable onItemDeleted;
    public Runnable onLoadMatch;

    public MatchSaveAdapter()
    {
        updateList();
    }

    @NonNull
    @Override
    public MatchSaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_match_save_item, parent, false);

        return new MatchSaveViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchSaveViewHolder holder, int position)
    {
        String matchName = listOfSaves.get(position);
        holder.matchFileName = matchName;

        holder.textName.setText(matchName.substring(0, matchName.length()-5));
        //FIXME: I don't think this is the correct way.
        holder.onItemDeleted = onItemDeleted;
        holder.onLoadMatch = onLoadMatch;
    }

    @Override
    public int getItemCount()
    {
        return listOfSaves.size();
    }

    public void updateList()
    {
        listOfSaves = MatchManager.getInstance().getMatchSavesList();
    }


    public static class MatchSaveViewHolder extends RecyclerView.ViewHolder
    {
        public MatchSaveViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textName = itemView.findViewById(R.id.item_save_text_title);
            btPlay = itemView.findViewById(R.id.item_save_bt_play);
            btPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPlayBt();
                }
            });

            btDelete = itemView.findViewById(R.id.item_save_bt_delete);
            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteBt();
                }
            });

        }

        private void onPlayBt()
        {
            MatchManager.getInstance().setMatchToLoad(matchFileName);
            onLoadMatch.run();
        }

        private void onDeleteBt()
        {
            MatchManager.getInstance().removeSavedMatch(matchFileName);
            onItemDeleted.run();
        }

        private final TextView textName;
        private final ImageButton btPlay;
        private final ImageButton btDelete;

        private String matchFileName = null;
        public Runnable onItemDeleted;
        public Runnable onLoadMatch;


        public TextView getTextName() { return textName; }
        public ImageButton getBtPlay() { return btPlay; }
        public ImageButton getBtDelete() { return btDelete; }
        public void setMatchFileName(String matchFileName) { this.matchFileName = matchFileName; }
    }
}
