package com.daniel.androidtrivial.Fragments.App;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniel.androidtrivial.MatchManager;
import com.daniel.androidtrivial.Model.MatchRecord.MatchStatsWithPlayersStats;
import com.daniel.androidtrivial.Model.MatchRecord.MathStatsDAO;
import com.daniel.androidtrivial.Model.MatchRecord.PlayerStatsDAO;
import com.daniel.androidtrivial.R;
import com.daniel.androidtrivial.ThreadOrchestrator;

import java.util.List;

public class MatchSummaryFragment extends Fragment
{
    LoadingDialogFragment loadingDialog;

    MatchStatsWithPlayersStats statsData;

    TextView text1;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ThreadOrchestrator.getInstance().setOnMatchRecordQueryEnded(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
                updateUI();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View frag =  inflater.inflate(R.layout.fragment_match_summary, container, false);

        initComponents(frag);

        return frag;
    }

    private void initComponents(View v)
    {
        loadingDialog = LoadingDialogFragment.newInstance(getString(R.string.summary_loading_title));
        loadingDialog.show(getParentFragmentManager(), "loadingStats");

        //TODO: Debería mostrar un loading mientras hace la consulta. Este fragment debería operar con el ID de match proveniente del list o del recien creado.
        //  Al terminar le query, ya muestra los datos.

        text1 = v.findViewById(R.id.fg_summary_text1);

        Button btReturn = v.findViewById(R.id.fg_summary_bt_return);
        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mng = getParentFragmentManager();
                mng.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.MainFragmentContainer, MatchRecordListFragment.class, null)
                        .commit();
            }
        });

        initQueryForData();
    }

    private void initQueryForData()
    {
        ThreadOrchestrator.getInstance().startThread("QueryStatsData", new Runnable() {
            @Override
            public void run() {
                MathStatsDAO matchDAO = MatchManager.getInstance().getDb().mathStatsDAO();
                MatchStatsWithPlayersStats stats = matchDAO.getMatchWithPlayers(MatchManager.getInstance().matchStatsIDToLoad);
                setStats(stats);
                ThreadOrchestrator.getInstance().sendMatchRecordQueryEnded();
            }
        });
    }

    public void setStats(MatchStatsWithPlayersStats statsData) { this.statsData = statsData; }

    public void updateUI()
    {
        //TODO: Show error
        if(statsData == null) { return; }

        String debugText = "Loading ID: " + statsData.matchStats.ID + "";
        text1.setText(debugText);

        Log.i("asd", "asd");
    }
}
