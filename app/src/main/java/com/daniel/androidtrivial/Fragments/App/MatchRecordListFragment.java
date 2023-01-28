package com.daniel.androidtrivial.Fragments.App;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.androidtrivial.Fragments.Game.GameFragment;
import com.daniel.androidtrivial.MatchManager;
import com.daniel.androidtrivial.Model.MatchRecord.MatchRecordDatabase;
import com.daniel.androidtrivial.Model.MatchRecord.MatchStats;
import com.daniel.androidtrivial.R;
import com.daniel.androidtrivial.Views.MatchRecordAdapter;

import java.util.List;

public class MatchRecordListFragment extends Fragment
{
    RecyclerView recordList;

    List<MatchStats> recordData;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View frag =  inflater.inflate(R.layout.fragment_match_record_list, container, false);

        initComponents(frag);

        return frag;
    }

    private void initComponents(View v)
    {
        recordList = v.findViewById(R.id.fg_recordList_RecyclerView);
        loadRecyclerView();

        Button btBack = v.findViewById(R.id.fg_recordList_bt_back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mng = getParentFragmentManager();
                mng.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.MainFragmentContainer, MainMenuFragment.class, null)
                        .commit();
            }
        });
    }

    private void loadRecyclerView()
    {
        //TODO: Do properly.
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                MatchRecordDatabase db = MatchManager.getInstance().getDb();
                setData(db.mathStatsDAO().getAll());
            }
        });
        t.start();
        try {
            t.join();
        } catch (Exception e)
        {
            Log.e("asd", e.toString());
        }

        MatchRecordAdapter adapter = new MatchRecordAdapter(recordData);
        adapter.setOnOpenStats(new Runnable() {
            @Override
            public void run() {
                FragmentManager mng = getParentFragmentManager();
                mng.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.MainFragmentContainer, MatchSummaryFragment.class, null)
                        .commit();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recordList.setLayoutManager(layoutManager);
        recordList.setAdapter(adapter);
    }

    private void setData(List<MatchStats> data)
    {
        this.recordData = data;
    }
}
