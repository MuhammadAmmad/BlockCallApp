package com.example.phuong.blockcallapp.ui.block_message.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.phuong.blockcallapp.R;
import com.example.phuong.blockcallapp.models.ContactBlock;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by Phuong Pham T. on 30/06/2017.
 */
public class MessageIncomeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(context.getString(R.string.provider_msg))) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from = "";
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get(context.getString(R.string.text_pdus));
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                    }
                    doBlockMessage(msg_from, context);
                } catch (Exception e) {
                }
            }
        }
    }


    public void doBlockMessage(String phoneAddress, Context context) {
        List<ContactBlock> mPhoneBlocks = ContactBlock.listAll(ContactBlock.class);
        String income = phoneAddress.replace(context.getString(R.string.text_84), context.getString(R.string.text_0));
        for (ContactBlock contactBlock : mPhoneBlocks) {
            if (contactBlock.getNumberPhone().contains(context.getString(R.string.text_84))) {
                contactBlock.getNumberPhone().replace(context.getString(R.string.text_84), context.getString(R.string.text_0));
            }
            if (income.equals(contactBlock.getNumberPhone())) {
                Log.d(getClass().getSimpleName(), "delete this message");
            }
        }
    }
}
