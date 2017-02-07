package com.android.internal.telephony;

/**
 * Created by phuong on 07/02/2017.
 */

public interface ITelephony {
    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}
