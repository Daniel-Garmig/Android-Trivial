package com.daniel.androidtrivial.Fragments.Game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.daniel.androidtrivial.Model.GameViewModel;
import com.daniel.androidtrivial.R;

import java.util.Random;

//TODO: Add intermediate state: Se muestra -> roll click -> stop click -> continue click.
public class DiceRollFragment extends DialogFragment
{

    private static final String argTitle = "title";

    public static DiceRollFragment newInstance(String title)
    {
        DiceRollFragment fg = new DiceRollFragment();

        Bundle args = new Bundle();
        args.putString(argTitle, title);
        fg.setArguments(args);

        return fg;
    }

    //It can only by instantiated by newInstance();
    //But this should be public so android can rebuild it.
    public DiceRollFragment() {}


    ///////////////////////////////////////////////

    String title;

    //Continue Button may do some stuff.
    Runnable btActions = null;

    GameViewModel viewModel;

    Button bt;
    TextView text_info;
    TextView text_rs;

    Random generator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);

        Bundle args = getArguments();
        if(args == null) { return; }
        title = args.getString(argTitle);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_rolldice, container, false);

        initComponents(v);

        viewModel = new ViewModelProvider(getActivity()).get(GameViewModel.class);

        return v;
    }

    private void initComponents(View v)
    {
        bt = v.findViewById(R.id.fg_dice_bt_roll);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });

        text_info = v.findViewById(R.id.fg_dice_text_info);
        text_info.setText(title);

        text_rs = v.findViewById(R.id.fg_dice_text_result);
        text_rs.setText("");

        //Init generator.
        generator = new Random();

        if(title != null && !title.isEmpty())
        {
            text_info.setText(title);
        }
    }

    public void rollDice()
    {
        //Dice Roll.
        int rs = generator.nextInt(6) + 1;
        text_rs.setText("" + rs);

        //Change bt behaviour.
        //To StringRes
        bt.setText("Continue");

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Send data to model.
                viewModel.setDiceRoll(rs);

                if(btActions != null) { btActions.run(); }
                dismiss();
            }
        });
    }

    public void setBtActions(Runnable actions) { this.btActions = actions; }
}
