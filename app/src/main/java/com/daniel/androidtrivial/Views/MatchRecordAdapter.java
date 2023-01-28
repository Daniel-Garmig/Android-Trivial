package com.daniel.androidtrivial.Views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.androidtrivial.MatchManager;
import com.daniel.androidtrivial.Model.MatchRecord.MatchStats;
import com.daniel.androidtrivial.R;

import java.util.List;

//TODO: Mb we can recycle the recycledView for matchRecord and matchLoad lists.
//TODO: Create MatchManager to manage all match related serialization/deserialization.
public class MatchRecordAdapter extends RecyclerView.Adapter<MatchRecordAdapter.MatchStatsViewHolder>
{
    List<MatchStats> statsList;
    public Runnable onOpenStats;


    public MatchRecordAdapter(List<MatchStats> statsList)
    {
        this.statsList = statsList;
    }

    @NonNull
    @Override
    public MatchStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_match_record_item, parent, false);

        return new MatchStatsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchStatsViewHolder holder, int position)
    {
        MatchStats stat = statsList.get(position);
        holder.getTextRecordName().setText(stat.name);
        holder.getBtOpenStats().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchManager.getInstance().setMatchStatsIDToLoad(stat.ID);
                onOpenStats.run();
            }
        });
    }

    @Override
    public int getItemCount() {
        return statsList.size();
    }

    public void setOnOpenStats(Runnable onOpenStats) { this.onOpenStats = onOpenStats; }


    // VIEW HOLDER Class!!

    public static class MatchStatsViewHolder extends RecyclerView.ViewHolder
    {
        public MatchStatsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textRecordName = itemView.findViewById(R.id.item_record_text_title);
            btOpenStats = itemView.findViewById(R.id.item_record_bt_play);
        }

        private final TextView textRecordName;
        private final ImageButton btOpenStats;

        public TextView getTextRecordName() { return textRecordName; }
        public ImageButton getBtOpenStats() { return btOpenStats; }
    }

}
