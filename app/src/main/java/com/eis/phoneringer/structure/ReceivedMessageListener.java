package com.eis.phoneringer.structure;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.eis.phoneringer.R;
import com.eis.phoneringer.exceptions.IllegalPasswordException;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.listeners.SMSReceivedListener;

/**
 * Listener used to manage incoming SMS and evaluate if they are valid commands for the application
 *
 * @author Alberto Ursino, Luca Crema
 */
public class ReceivedMessageListener implements SMSReceivedListener {

    private RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();
    private Context context;

    /**
     * Constructor which defines a context
     *
     * @param context The current application context
     */
    public ReceivedMessageListener(Context context) {
        this.context = context;
    }

    /**
     * Creates the {@link RingCommand} upon receipt of the message and calls the class {@link AppManager} if the RingCommand is not null
     *
     * @param smsMessage The SMSMessage object received
     * @throws IllegalPasswordException Exception thrown by the method "onRingCommandReceived" of the class {@link AppManager} when the password received is not valid
     */
    @Override
    public void onMessageReceived(@NonNull SMSMessage smsMessage) throws IllegalPasswordException {
        Log.d("ReceivedMessage", "Received a message in the service");
        //Let's parse this message and build a RingCommand object
        RingCommand ringCommand = RingCommandHandler.getInstance().parseMessage(smsMessage);

        if (ringCommand == null) {
            Log.d("Invalid RingCommand", "The message received is not a ring command");
        } else {
            try {
                AppManager.getInstance().onRingCommandReceived(context, ringCommand, ringtoneHandler.getDefaultRingtone(context));
            } catch (IllegalPasswordException e) {
                //Password from the ring command received is not correct
                Toast.makeText(context, smsMessage.getPeer() + context.getString(R.string.invalid_password_received_toast), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
