package com.daniel.androidtrivial;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.HashMap;

//TODO: Move all thread start to here, so it's controlled on a central location.
//  At the end of the day, this is a thread orchestrator...

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

        synchronized (ThreadOrchestrator.class)
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
        activeThreads = new HashMap<>();
    }


    //THREAD MANAGEMENT

    //TODO: Revisar si de verdad necesitamos guardar los threads. Me parece un memory leak.
    HashMap<String, Thread> activeThreads;

    public void startThread(String name, Runnable task)
    {
        Thread t = new Thread(task);
        t.setName(name);
        t.start();
    }


    // HANDLER


    public static final int msgGameDataLoaded = 0;
    public static final int msgBoardDataLoaded = 1;
    public static final int msgAssetsLoaded = 2;
    public static final int msgViewModelDataLoaded = 3;

    public static final int MSG_ALL_DATA_LOADED = 10;
    public static final int MSG_RTG_GAME_INTERACTION_ENDED = 11;

    public static final int MSG_DB_CREATION_ERROR = 20;
    public static final int MSG_DB_CREATION_SUCCESS = 21;

    //Error doing operations on DB (query, insert, update, ...).
    public static final int MSG_DB_OPERATION_ERROR = 25;

    public static final int MSG_QUESTION_QUERY_ENDED = 30;


    boolean gameDataLoaded = false;
    boolean boardDataLoaded = false;
    boolean assetsLoaded = false;
    boolean viewModelDataLoaded = false;


    /**
     * Used when all game data is loaded by respective threads so game can start.
     */
    private Runnable onAllDataLoaded;

    /**
     * Used when RTG_Game ends it's interaction and UI can continue game Logic.
     */
    private Runnable onRTG_GameEndsInteraction;

    private Runnable onDBCreationError;
    private Runnable onDBCreationSuccess;
    private Runnable onDBOperationError;

    private Runnable onQuestionQueryEnded;


    public void setOnAllDataLoaded(Runnable onAllDataLoaded) { this.onAllDataLoaded = onAllDataLoaded; }
    public void setOnRTG_GameEndsInteraction(Runnable onRTG_GameEndsInteraction) { this.onRTG_GameEndsInteraction = onRTG_GameEndsInteraction; }

    public void setOnDBCreationError(Runnable onDBCreationError) { this.onDBCreationError = onDBCreationError; }
    public void setOnDBCreationSuccess(Runnable onDBCreationSuccess) { this.onDBCreationSuccess = onDBCreationSuccess; }

    public void setOnDBOperationError(Runnable onDBOperationError) { this.onDBOperationError = onDBOperationError; }


    public void setOnQuestionQueryEnded(Runnable onQuestionQueryEnded) { this.onQuestionQueryEnded = onQuestionQueryEnded; }



    public synchronized void sendDataLoaded(int msgId)
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
            gameDataLoaded = false;
            boardDataLoaded = false;
            //Assets and viewModel data are only loaded once.
        }
    }

    public void sendGameEndsInteraction()
    {
        sendMessage(obtainMessage(MSG_RTG_GAME_INTERACTION_ENDED));
    }

    public void sendDBCreationError(String msg)
    {
        sendMessage(obtainMessage(MSG_DB_CREATION_ERROR));
    }

    public void sendDBCreationSuccess(String msg)
    {
        sendMessage(obtainMessage(MSG_DB_CREATION_SUCCESS));
    }

    public void sendDBOperationError(String msg)
    {
        sendMessage(obtainMessage(MSG_DB_OPERATION_ERROR));
    }


    public void sendQuestionQueryEnded()
    {
        sendMessage(obtainMessage(MSG_QUESTION_QUERY_ENDED));
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
            case MSG_DB_CREATION_ERROR:
                onDBCreationError.run();
                break;
            case MSG_DB_CREATION_SUCCESS:
                onDBCreationSuccess.run();
                break;
            case MSG_DB_OPERATION_ERROR:
                onDBOperationError.run();
                break;
            case MSG_QUESTION_QUERY_ENDED:
                onQuestionQueryEnded.run();
                break;
        }
    }
}
