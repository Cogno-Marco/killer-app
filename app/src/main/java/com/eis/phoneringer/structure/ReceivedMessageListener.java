package com.eis.phoneringer.structure;

import android.content.Context;
import android.media.Ringtone;
import android.util.Log;

import androidx.annotation.NonNull;

import com.eis.phoneringer.exceptions.IllegalPasswordException;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.listeners.SMSReceivedServiceListener;

/**
 * Listener used to manage incoming SMS and evaluate if they are valid commands for the application
 *
 * @author Alberto Ursino, Luca Crema
 */
public class ReceivedMessageListener extends SMSReceivedServiceListener {

    private static final String TAG = "ReceivedMessageListener";
    private RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();

    /**
     * Creates the {@link RingCommand} upon receipt of the message and calls the class {@link AppManager} if the RingCommand is not null
     *
     * @param smsMessage The SMSMessage object received
     * @throws IllegalPasswordException Exception thrown by {@link AppManager#onRingCommandReceived(Context, RingCommand, Ringtone)} when the password received is not valid
     */
    @Override
    public void onMessageReceived(@NonNull SMSMessage smsMessage) throws IllegalPasswordException {
        Log.d(TAG, "Received a message in the service");
        //Let's parse this message and build a RingCommand object
        RingCommand ringCommand = RingCommandHandler.getInstance().parseMessage(smsMessage);

        if (ringCommand == null) {
            Log.d(TAG, "The message received is not a valid RingCommand");
        } else {
            try {
                AppManager.getInstance().onRingCommandReceived(getApplicationContext(), ringCommand, ringtoneHandler.getDefaultRingtone(getApplicationContext()));
            } catch (IllegalPasswordException e) {
                //Password from the ring command received is not correct
                Log.d(TAG, "Password received is not valid");
            }
        }
    }

}
