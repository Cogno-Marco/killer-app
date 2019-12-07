package com.eis.phoneringer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eis.phoneringer.structure.AppManager;
import com.eis.phoneringer.structure.PasswordManager;
import com.eis.phoneringer.structure.ReceivedMessageListener;
import com.eis.phoneringer.structure.RingCommand;
import com.eis.smslibrary.SMSHandler;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.exceptions.InvalidSMSMessageException;
import com.eis.smslibrary.exceptions.InvalidTelephoneNumberException;
import com.eis.smslibrary.listeners.SMSSentListener;

/**
 * Test app: through this MainActivity we can test out our basic library for sending ring commands
 *
 * @author Alberto Ursino
 */
public class MainActivity extends AppCompatActivity {

    private Button ringButton;
    private EditText phoneNumber;
    private static final String DEFAULT_PASSWORD = "password";
    private static final int WAIT_TIME_PERMISSION = 1500;
    private PasswordManager passwordManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = getApplicationContext();
        ringButton = findViewById(R.id.ring_button);
        phoneNumber = findViewById(R.id.phone_number);
        passwordManager = new PasswordManager(context);

        //Some tricks with permissions
        if (checkPermission()) {
            SMSHandler.getInstance().setup(context);
            SMSHandler.getInstance().setReceivedListener(new ReceivedMessageListener(context));
        } else {
            requestPermission();
        }

        //Default password equals for all the devices
        if (passwordManager.getPassword().isEmpty())
            passwordManager.setPassword(DEFAULT_PASSWORD);

        ringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand();
            }
        });
    }

    /**
     * Sends a ring command to the phone number given by the user
     */
    private void sendCommand() {
        final Context context = getApplicationContext();
        //Callback when the command is sent
        SMSSentListener smsSentListener = new SMSSentListener() {
            @Override
            public void onSMSSent(SMSMessage message, SMSMessage.SentState sentState) {
                Toast.makeText(context, getString(R.string.sent_listener_toast) + phoneNumber.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        };
        //Passing to the AppManager a new RingCommand built through the phone number given by the user
        try {
            AppManager.getInstance().sendCommand(context, new RingCommand(new SMSPeer(phoneNumber.getText().toString()), passwordManager.getPassword()), smsSentListener);
        } catch (InvalidTelephoneNumberException e) {
            Toast.makeText(context, getString(R.string.invalid_phone_number_toast), Toast.LENGTH_SHORT).show();
        } catch (InvalidSMSMessageException e) {
            Toast.makeText(context, getString(R.string.invalid_sms_message_toast), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @return true if the app has permissions, false otherwise
     */
    public boolean checkPermission() {
        return !((getApplicationContext().checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) ||
                !(getApplicationContext().checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED));
    }

    /**
     * Checks if permissions are granted, if not then requests them to the user
     */
    public void requestPermission() {
        if (!checkPermission())
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Context context = getApplicationContext();
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            SMSHandler.getInstance().setup(context);
            SMSHandler.getInstance().setReceivedListener(new ReceivedMessageListener(context));
        } else {
            Toast.makeText(context, getString(R.string.permissions_denied_toast), Toast.LENGTH_SHORT).show();
            //Let's wait the toast ends
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    requestPermission();
                }
            }, WAIT_TIME_PERMISSION);
        }
    }

}
