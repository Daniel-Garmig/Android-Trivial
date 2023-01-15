package com.daniel.androidtrivial.Game;

import android.os.Message;

import androidx.annotation.NonNull;

import com.uberelectron.androidrtg.RTG_Handler;

public class MyHandler extends RTG_Handler {
    private static final int MSG_TOUCH = 4;


    public void sendTouch() {
        sendMessage(obtainMessage(MSG_TOUCH));
    }

    @Override
    public boolean sendMessageAtTime(@NonNull Message msg, long uptimeMillis) {
        switch (msg.what) {
            case MSG_TOUCH:
                thread.getApp(MyGame.class).onSurfaceTouch();
                break;
        }

        //If it's not handled here, it may be handled in RTG_Handler.
        return super.sendMessageAtTime(msg, uptimeMillis);

    }
}
