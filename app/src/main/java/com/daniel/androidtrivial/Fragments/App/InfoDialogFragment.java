package com.daniel.androidtrivial.Fragments.App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.daniel.androidtrivial.R;

public class InfoDialogFragment extends DialogFragment
{

    private static final String argTitle = "title";
    private static final String argMsg = "msg";

    public static InfoDialogFragment newInstance(String title, String msg)
    {
        InfoDialogFragment fg = new InfoDialogFragment();

        Bundle args = new Bundle();
        args.putString(argTitle, title);
        args.putString(argMsg, msg);
        fg.setArguments(args);

        return fg;
    }

    //It can only by instantiated by newInstance();
    //But this should be public so android can rebuild it.
    public InfoDialogFragment() {}


    /////////////////////////////////////////////

    String title;
    String msg;

    //Continue Button may do some stuff.
    Runnable btActions = null;

    Button btContinue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);

        Bundle args = getArguments();
        if(args == null) { return; }

        title = args.getString(argTitle);
        msg = args.getString(argMsg);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_info, container, false);

        initComponents(v);

        return v;
    }

    private void initComponents(View v)
    {
        TextView textTitle = v.findViewById(R.id.fg_info_text_title);
        textTitle.setText(title);

        TextView textMsg = v.findViewById(R.id.fg_info_text_msg);
        textMsg.setText(msg);

        btContinue = v.findViewById(R.id.fg_info_bt_continue);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btActions!= null) { btActions.run(); }
                dismiss();
            }
        });
    }


    public void setBtActions(Runnable actions) { this.btActions = actions; }

}
