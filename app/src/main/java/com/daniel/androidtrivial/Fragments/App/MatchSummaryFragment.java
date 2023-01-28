package com.daniel.androidtrivial.Fragments.App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.daniel.androidtrivial.MatchManager;
import com.daniel.androidtrivial.R;

public class MatchSummaryFragment extends Fragment
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
        //TODO: Debería mostrar un loading mientras hace la consulta. Este fragment debería operar con el ID de match proveniente del list o del recien creado.
        //  Al terminar le query, ya muestra los datos.

        TextView text1 = v.findViewById(R.id.fg_summary_text1);
        String debugText = "Loading ID: " + MatchManager.getInstance().matchStatsIDToLoad + "";
        text1.setText(debugText);
    }
}
