package com.daniel.androidtrivial.Fragments.App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniel.androidtrivial.QuestionsManager;
import com.daniel.androidtrivial.R;
import com.daniel.androidtrivial.ThreadOrchestrator;

public class MainMenuFragment extends Fragment
{
    LoadingDialogFragment loadingDialog;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View frag =  inflater.inflate(R.layout.fragment_main_menu, container, false);

        initComponents(frag);

        return frag;
    }

    private void initComponents(View v)
    {
        Button btBack = v.findViewById(R.id.menu_bt_Play);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayButton();
            }
        });

        //DEBUG: Used for testing roll dice dialog.
        Button btRollDice = v.findViewById(R.id.menu_bt_matchRecord);
        btRollDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debugRollDice();
            }
        });

        //DEBUG: Used for testing.
        Button btRoom = v.findViewById(R.id.menu_bt_room);
        btRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debugLoadRoom();
            }
        });

        Button btCreateDB = v.findViewById(R.id.menu_bt_createDB);
        btCreateDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debugCreateDB();
            }
        });
    }

    private void onPlayButton()
    {
        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, MatchSaveListFragment.class, null)
                .commit();
    }

    //DEBUG: Used for testing roll dice dialog.
    private void debugRollDice()
    {
        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, MatchRecordListFragment.class, null)
                .commit();
    }

    //DEBUG
    private void debugLoadRoom()
    {
        FragmentManager mng = getParentFragmentManager();
        mng.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.MainFragmentContainer, MatchRoomFragment.class, null)
                .commit();
    }


    private void debugCreateDB()
    {
        CreateDBDialogFragment df = new CreateDBDialogFragment();
        df.onBtContinue = new Runnable() {
            @Override
            public void run() {
                createDB(df.languageGroup.getCheckedRadioButtonId());
                df.dismiss();
            }
        };
        df.show(getParentFragmentManager(), "CreateDB");
    }

    private void createDB(int languageOp)
    {
        //STRINGRES
        loadingDialog = LoadingDialogFragment.newInstance("Creating Questions DB");
        loadingDialog.show(getParentFragmentManager(), "LoadingDialog");

        ThreadOrchestrator.getInstance().setOnDBCreationSuccess(new Runnable() {
            @Override
            public void run() {
                loadingDialog.onFinishLoad();
            }
        });

        int[] categories = { R.raw.q_arts_es, R.raw.q_entertainment_es, R.raw.q_geography_es, R.raw.q_history_es, R.raw.q_science_es, R.raw.q_sports_es };
        if(languageOp == R.id.fg_createdb_op_en)
        {
            categories = new int[] { R.raw.q_arts_en, R.raw.q_entertainment_en, R.raw.q_geography_en, R.raw.q_history_en, R.raw.q_science_en, R.raw.q_sports_en };
        }

        QuestionsManager.getInstance().createDefaultDB(categories);
    }
}
