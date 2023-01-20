package com.daniel.androidtrivial;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.daniel.androidtrivial.Game.GameData;

/**
 * Class for communicating threads (manually).
 * It's sure not safe, but let's try anyway.
 */
public class ThreadOrchestrator extends Handler
{
    // SINGLETON.
    private static ThreadOrchestrator instance;

    public static ThreadOrchestrator getInstance()
    {
        //Singleton with double-checked locking (DCL).

        ThreadOrchestrator orchestator = instance;
        if(orchestator != null) { return orchestator; }

        synchronized (GameData.class)
        {
            if(instance == null)
            {
                instance = new ThreadOrchestrator();
            }
            return instance;
        }
    }


    // END SINGLETON.

    public ThreadOrchestrator()
    {
        instance = this;
    }

    public static final int msgGameDataLoaded = 0;
    public static final int msgBoardDataLoaded = 1;
    public static final int msgAssetsLoaded = 2;
    public static final int msgViewModelDataLoaded = 3;

    public static final int MSG_ALL_DATA_LOADED = 10;
    public static final int MSG_RTG_GAME_INTERACTION_ENDED = 11;


    boolean gameDataLoaded = false;
    boolean boardDataLoaded = false;
    boolean assetsLoaded = false;
    boolean viewModelDataLoaded = false;


    /**
     * Used when all game data is loaded by respective threads so game can start.
     */
    Runnable onAllDataLoaded;

    /**
     * Used when RTG_Game ends it's interaction and UI can continue game Logic.
     */
    Runnable onRTG_GameEndsInteraction;


    public void setOnAllDataLoaded(Runnable onAllDataLoaded) { this.onAllDataLoaded = onAllDataLoaded; }
    public void setOnRTG_GameEndsInteraction(Runnable onRTG_GameEndsInteraction) { this.onRTG_GameEndsInteraction = onRTG_GameEndsInteraction; }


    public void onDataLoaded(int msgId)
    {
        switch (msgId)
        {
            case msgGameDataLoaded:
                gameDataLoaded = true;
                break;
            case msgBoardDataLoaded:
                boardDataLoaded = true;
                break;
            case msgAssetsLoaded:
                assetsLoaded = true;
                break;
            case msgViewModelDataLoaded:
                viewModelDataLoaded = true;
                break;
        }

        if(gameDataLoaded && boardDataLoaded && assetsLoaded && viewModelDataLoaded)
        {
            sendMessage(obtainMessage(MSG_ALL_DATA_LOADED));
        }
    }

    public void sendGameEndsInteraction()
    {
        sendMessage(obtainMessage(MSG_RTG_GAME_INTERACTION_ENDED));
    }

    @Override
    public void handleMessage(@NonNull Message msg)
    {
        int msgCode = msg.what;

        switch (msgCode)
        {
            case MSG_ALL_DATA_LOADED:
                onAllDataLoaded.run();
                break;
            case MSG_RTG_GAME_INTERACTION_ENDED:
                onRTG_GameEndsInteraction.run();
                break;
        }
    }
}
