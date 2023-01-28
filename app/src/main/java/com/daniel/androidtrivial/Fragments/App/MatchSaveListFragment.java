package com.daniel.androidtrivial.Fragments.App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.androidtrivial.Fragments.Game.GameFragment;
import com.daniel.androidtrivial.Game.GameData;
import com.daniel.androidtrivial.Model.GameState;
import com.daniel.androidtrivial.Model.GameViewModel;
import com.daniel.androidtrivial.R;
import com.daniel.androidtrivial.Views.MatchSaveAdapter;

public class MatchSaveListFragment extends Fragment
{
    GameViewModel viewModel;

    RecyclerView saveList;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View frag =  inflater.inflate(R.layout.fragment_match_save_list, container, false);

        //Get game ViewModel.
        FragmentActivity act = getActivity();
        if(act != null)
        {
            viewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);
        }

        initComponents(frag);

        return frag;
    }

    private void initComponents(View v)
    {
        saveList = v.findViewById(R.id.fg_saveList_RecyclerView);
        loadRecyclerView();

        Button btReturn = v.findViewById(R.id.fg_saveList_bt_back);
        btReturn.setOnClickListener(new View.OnClickListener() {
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
        MatchSaveAdapter adapter = new MatchSaveAdapter();
        adapter.onItemDeleted = new Runnable() {
            @Override
            public void run() {
                loadRecyclerView();
            }
        };
        adapter.onLoadMatch = new Runnable() {
            @Override
            public void run() {
                GameData.getInstance().gameDataResets();
                viewModel.setStage(GameState.LoadedGame);
                FragmentManager mng = getParentFragmentManager();
                mng.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.MainFragmentContainer, GameFragment.class, null)
                        .commit();
            }
        };

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());


        saveList.setLayoutManager(layoutManager);
        saveList.setAdapter(adapter);
    }

}
