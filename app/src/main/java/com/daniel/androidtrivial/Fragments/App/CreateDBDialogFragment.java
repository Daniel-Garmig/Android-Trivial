package com.daniel.androidtrivial.Fragments.App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.daniel.androidtrivial.R;

public class CreateDBDialogFragment extends DialogFragment
{
    public Runnable onBtContinue;

    private Button btContinue;
    public RadioGroup languageGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_createdb, container, false);

        initComponents(v);

        return v;
    }

    private void initComponents(View v)
    {
        languageGroup = v.findViewById(R.id.fg_createdb_radioGroup);

        btContinue = v.findViewById(R.id.fg_createdb_bt_continue);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtContinue.run();
                dismiss();
            }
        });
    }


}
