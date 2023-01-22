package com.daniel.androidtrivial.Fragments.App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.daniel.androidtrivial.R;


//TODO: Mb do it fullscreen?
//TODO: Add more customization?
public class LoadingDialogFragment extends DialogFragment
{
    private static final String argMsg = "msg";

    public static LoadingDialogFragment newInstance(String msg)
    {
        LoadingDialogFragment fg = new LoadingDialogFragment();

        Bundle args = new Bundle();
        args.putString(argMsg, msg);
        fg.setArguments(args);

        return fg;
    }

    //It can only by instantiated by newInstance();
    private LoadingDialogFragment() {}


    /////////////////////////////////////////////


    String msg;

    //Continue Button may do some stuff.
    Runnable btActions = null;

    Button btContinue;
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);

        Bundle args = getArguments();
        if(args == null) { return; }

        msg = args.getString(argMsg);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_loading, container, false);

        initComponents(v);

        return v;
    }

    private void initComponents(View v)
    {
        TextView textMsg = v.findViewById(R.id.fg_loading_text_msg);
        textMsg.setText(msg);

        progressBar = v.findViewById(R.id.fg_loading_progessBar);
        progressBar.setIndeterminate(true);

        btContinue = v.findViewById(R.id.fg_loading_bt_continue);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btActions!= null) { btActions.run(); }
                dismiss();
            }
        });
        btContinue.setVisibility(View.GONE);
    }


    public void setBtActions(Runnable actions) { this.btActions = actions; }

    public void onFinishLoad()
    {
        btContinue.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(100);
        progressBar.invalidate();
    }
}
