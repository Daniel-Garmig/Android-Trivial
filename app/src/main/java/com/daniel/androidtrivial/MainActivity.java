package com.daniel.androidtrivial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.daniel.androidtrivial.Game.Utils.AssetManager;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init ThreadOrchestrator.
        ThreadOrchestrator.getInstance();

        //FIXME: Cargar los assets en otro thread podría generar una condición de carrera en la peor de las situaciones. Asegurar eso.
        AssetManager.getInstance().loadAssets(this);
        Thread loadAssets = new Thread(new Runnable() {
            @Override
            public void run() {
                AssetManager.getInstance().loadAssets(getApplicationContext());
                ThreadOrchestrator.getInstance().onDataLoaded(ThreadOrchestrator.msgAssetsLoaded);
            }
        });
        loadAssets.start();


        //TODO: Comprobar que existe la DB de preguntas y que no hay ninguna nueva versión.
    }

    @Override
    public void onBackPressed()
    {
        //TODO: Do smt depending on active fragment.
        //super.onBackPressed();
    }
}