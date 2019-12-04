package com.eis.phoneringer.structure;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.eis.phoneringer.exceptions.WrongPasswordException;
import com.eis.smslibrary.SMSPeer;

import org.junit.Before;
import org.junit.Test;

public class AppManagerTest {

    Context context = null;
    static AppManager appManager = null;
    static PasswordManager passwordManager = null;
    static RingtoneHandler ringtoneHandler = RingtoneHandler.getInstance();
    static final String WRONG_PASSWORD = "username";
    static final String VALID_PASSWORD = "password";
    static final String VALID_PHONE_NUMBER = "+391111111111";

    @Before
    public void init() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appManager = AppManager.getInstance();
        passwordManager = new PasswordManager(context);
    }

    @Test(expected = WrongPasswordException.class)
    public void onRingCommandReceived_wrong_password() {
        passwordManager.setPassword(VALID_PASSWORD);
        appManager.getInstance().onRingCommandReceived(context, new RingCommand(new SMSPeer(VALID_PHONE_NUMBER), WRONG_PASSWORD), ringtoneHandler.getDefaultRingtone(context));
    }

    @Test
    public void onRingCommandReceived_right_password() {
        passwordManager.setPassword(VALID_PASSWORD);
        appManager.getInstance().onRingCommandReceived(context, new RingCommand(new SMSPeer(VALID_PHONE_NUMBER), VALID_PASSWORD), ringtoneHandler.getDefaultRingtone(context));
    }

}