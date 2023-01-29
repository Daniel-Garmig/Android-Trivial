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
import com.daniel.androidtrivial.ThreadOrchestrator;
import com.daniel.androidtrivial.Views.MatchRecordAdapter;

import java.util.List;

public class MatchRecordListFragment extends Fragment
{
    RecyclerView recordList;

    List<MatchStats> recordData;

    LoadingDialogFragment ld;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ThreadOrchestrator.getInstance().setOnMatchRecordQueryEnded(new Runnable() {
            @Override
            public void run() {
                ld.dismiss();
                createRecordList();
            }
        });
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
        ld = LoadingDialogFragment.newInstance(getString(R.string.record_loading_title));
        ld.show(getParentFragmentManager(), "loadingRecord");

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
        ThreadOrchestrator.getInstance().startThread("LoadMatchRecord", new Runnable() {
            @Override
            public void run() {
                MatchRecordDatabase db = MatchManager.getInstance().getDb();
                setData(db.mathStatsDAO().getAll());
                ThreadOrchestrator.getInstance().sendMatchRecordQueryEnded();
            }
        });
    }

    private void setData(List<MatchStats> data) { this.recordData = data; }

    private void createRecordList()
    {
        //TODO: Empty msg.
        if(recordData == null)
        {
            return;
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
}
