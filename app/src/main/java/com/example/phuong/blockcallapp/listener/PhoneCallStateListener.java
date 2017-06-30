package com.example.phuong.blockcallapp.listener;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.example.phuong.blockcallapp.models.ContactBlock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuong on 05/02/2017.
 */

public class PhoneCallStateListener extends PhoneStateListener {
    private Context context;
    private List<ContactBlock> mPhoneBlocks;

    public PhoneCallStateListener(Context context) {
        this.context = context;
        mPhoneBlocks = new ArrayList<>();
    }

    public static boolean showCallLog(Context context) {
        try {
            Intent showCallLog = new Intent();
            showCallLog.setAction(Intent.ACTION_VIEW);
            showCallLog.setType(android.provider.CallLog.Calls.CONTENT_TYPE);
            context.startActivity(showCallLog);
            return true;
        } catch (Exception e) {
            Log.d("Couldn't show call log.", e.getMessage());
        }
        return false;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        switch (state) {

            case TelephonyManager.CALL_STATE_RINGING:

                mPhoneBlocks = ContactBlock.listAll(ContactBlock.class);
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                //Turn ON the mute
                audioManager.setStreamMute(AudioManager.STREAM_RING, true);
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    Class clazz = Class.forName(telephonyManager.getClass().getName());
                    Method method = clazz.getDeclaredMethod("getITelephony");
                    method.setAccessible(true);
                    ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);
                    for (ContactBlock phoneBlock : mPhoneBlocks) {
                        if (phoneBlock.getNumberPhone().contains("+84")) {
                            phoneBlock.setNumberPhone(phoneBlock.getNumberPhone().replace("+84", "0"));
                        }
                        if (incomingNumber.equalsIgnoreCase(phoneBlock.getNumberPhone())) {
                            telephonyService = (ITelephony) method.invoke(telephonyManager);
                            telephonyService.silenceRinger();
                            telephonyService.endCall();
                            showCallLog(context);
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
                //Turn OFF the mute
                audioManager.setStreamMute(AudioManager.STREAM_RING, false);
                break;
            case PhoneStateListener.LISTEN_CALL_STATE:
        }
        super.onCallStateChanged(state, incomingNumber);
    }
}
