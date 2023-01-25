package com.daniel.androidtrivial.Game;

import android.os.Message;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.daniel.androidtrivial.Model.Player;
import com.uberelectron.androidrtg.RTG_Handler;

import java.util.HashMap;
import java.util.List;

public class MyHandler extends RTG_Handler {
    private static final int MSG_TOUCH = 4;

    private static final int MSG_MOVE_STATE = 10;
    private static final int MSG_ROLL_DICE_STATE = 11;

    private static final int MSG_CREATE_PLAYERS = 15;
    private static final int MSG_UPDATE_POSITIONS = 16;


    public void sendTouch(MotionEvent event)
    {
        sendMessage(obtainMessage(MSG_TOUCH, event));
    }

    public void sendStartMoveState(int movs, Player currentPlayer)
    {
        sendMessage(obtainMessage(MSG_MOVE_STATE, movs, 0, currentPlayer));
    }

    public void sendCreatePlayers(List<Player> playerList)
    {
        sendMessage(obtainMessage(MSG_CREATE_PLAYERS, playerList));
    }

    public void sendUpdatePositions(HashMap<Integer, Integer> positions)
    {
        sendMessage(obtainMessage(MSG_UPDATE_POSITIONS, positions));
    }

    public void sendRollDiceState()
    {
        sendMessage(obtainMessage(MSG_ROLL_DICE_STATE));
    }

    @Override
    public boolean sendMessageAtTime(@NonNull Message msg, long uptimeMillis) {
        switch (msg.what) {
            case MSG_TOUCH:
                thread.getApp(MyGame.class).onSurfaceTouch((MotionEvent) msg.obj);
                break;
            case MSG_MOVE_STATE:
                thread.getApp(MyGame.class).startMoveState(msg.arg1, (Player) msg.obj);
                break;
            case MSG_CREATE_PLAYERS:
                thread.getApp(MyGame.class).createPlayers((List<Player>) msg.obj);
                break;
            case MSG_UPDATE_POSITIONS:
                thread.getApp(MyGame.class).updatePositions((HashMap<Integer, Integer>) msg.obj);
                break;
            case MSG_ROLL_DICE_STATE:
                thread.getApp(MyGame.class).startRollDiceState();
                break;
        }

        //If it's not handled here, it may be handled in RTG_Handler.
        return super.sendMessageAtTime(msg, uptimeMillis);

    }
}
