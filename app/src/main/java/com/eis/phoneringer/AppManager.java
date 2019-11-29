package com.eis.phoneringer.structure;

import android.content.Context;
import android.media.Ringtone;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.eis.smslibrary.SMSHandler;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;

/**
 * This is a singleton class used to manage a received RingCommand or to send one
 *
 * @author Alberto Ursino, Luca Crema, Alessandra Tonin, Marco Mariotto
 */
public class AppManager {

    private static final String WRONG_PASSWORD_TOAST = "Wrong password";
    private static final int TIMEOUT_TIME = 30 * 1000; //30 seconds

    /**
     * Instance of the class that is instantiated in getInstance method
     */
    private static AppManager instance = null;

    /**
     * Private constructor
     */
    private AppManager() {
    }

    /**
     * @return the AppManager instance
     */
    public static AppManager getInstance() {
        if (instance == null)
            instance = new AppManager();
        return instance;
    }

    /**
     * If the password of the message received is valid then play ringtone for fixed amount of time
     *
     * @param context     of the application
     * @param ringCommand a ring command not null
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onRingCommandReceived(Context context, RingCommand ringCommand, final Ringtone ringtone) {
        final RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();

        if (checkPassword(context, ringCommand)) {
            ringtoneHandler.playRingtone(ringtone);
            //Timer: the ringtone is playing for TIMEOUT_TIME seconds.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ringtoneHandler.stopRingtone(ringtone);
                }
            }, TIMEOUT_TIME);
        } else {
            Toast.makeText(context, WRONG_PASSWORD_TOAST, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method used to send a RingCommand via SMS using the library class {@link SMSHandler}
     *
     * @param context     of the application
     * @param ringCommand to send
     * @throws InvalidTelephoneNumberException
     * @throws InvalidSMSMessageException
     */
    public void sendCommand(Context context, RingCommand ringCommand) throws InvalidSMSMessageException, InvalidTelephoneNumberException {
        SMSHandler smsHandler = SMSHandler.getInstance();
        smsHandler.setup(context);
        smsHandler.sendMessage(RingCommandHandler.getInstance().parseCommand(ringCommand));
    }

    /**
     * Checks if the RingCommand password and the one saved in memory corresponds
     *
     * @param context     a valid context
     * @param ringCommand a valid RingCommand object
     * @return a boolean: true = passwords are corresponding, false = passwords are NOT corresponding
     */
    public boolean checkPassword(Context context, RingCommand ringCommand) {
        return ringCommand.getPassword().equals(new PasswordManager(context).getPassword());
    }

}

