package com.daniel.androidtrivial.Game;

import android.os.Message;

import androidx.annotation.NonNull;

import com.uberelectron.androidrtg.RTG_Handler;

public class MyHandler extends RTG_Handler {
    private static final int MSG_TOUCH = 4;

    private static final int MSG_MOVE_STATE = 10;


    public void sendTouch() {
        sendMessage(obtainMessage(MSG_TOUCH));
    }

    public void sendStartMoveState(int movs)
    {
        sendMessage(obtainMessage(MSG_MOVE_STATE, movs, 0));
    }

    @Override
    public boolean sendMessageAtTime(@NonNull Message msg, long uptimeMillis) {
        switch (msg.what) {
            case MSG_TOUCH:
                thread.getApp(MyGame.class).onSurfaceTouch();
                break;
            case MSG_MOVE_STATE:
                thread.getApp(MyGame.class).startMoveState(msg.arg1);
                break;
        }

        //If it's not handled here, it may be handled in RTG_Handler.
        return super.sendMessageAtTime(msg, uptimeMillis);

    }
}
