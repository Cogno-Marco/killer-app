package com.eis.audioUtility;


import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Francesco Bau', Alberto Ursino
 */
public class AudioUtilityManagerTest {

    Context context;
    private static final int INVALID_PERCENTAGE_LOW = -1;
    private static final int INVALID_PERCENTAGE_HIGH = 101;

    @Before
    public void init() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setAlarmVolume_percentage_isTooLow() {
        AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.ALARM, INVALID_PERCENTAGE_LOW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setAlarmVolume_percentage_isTooHigh() {
        AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.ALARM, INVALID_PERCENTAGE_HIGH);
    }

    @Test
    public void setAlarmVolumeToMax_maxVolume_isOK() {
        AudioUtilityManager.setVolumeToMax(context, AudioUtilityManager.AUMStream.ALARM);
    }

    @Test
    public void setAlarmVolumeToMin_minVolume_isOK() {
        AudioUtilityManager.setVolumeToMin(context, AudioUtilityManager.AUMStream.ALARM);
    }

    // It fails automatically if an exception is thrown.
    @Test
    public void getCurrentAlarmVolume_method_isOK() {
        AudioUtilityManager.getVolume(context, AudioUtilityManager.AUMStream.ALARM);
    }

//-----------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void setMusicVolume_percentage_isTooLow() {
        AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.ALARM, INVALID_PERCENTAGE_LOW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMusicVolume_percentage_isTooHigh() {
        AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.MUSIC, INVALID_PERCENTAGE_HIGH);
    }

    @Test
    public void setMusicVolumeToMax_maxVolume_isOK() {
        AudioUtilityManager.setVolumeToMax(context, AudioUtilityManager.AUMStream.MUSIC);
    }

    @Test
    public void setMusicVolumeToMin_minVolume_isOK() {
        AudioUtilityManager.setVolumeToMin(context, AudioUtilityManager.AUMStream.MUSIC);
    }

    @Test
    public void getCurrentMusicVolume_method_isOK() {
        AudioUtilityManager.getVolume(context, AudioUtilityManager.AUMStream.MUSIC);
    }

//-----------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void setRingVolume_percentage_isTooLow() {
        AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.RING, INVALID_PERCENTAGE_LOW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setRingVolume_percentage_isTooHigh() {
        AudioUtilityManager.setVolume(context, AudioUtilityManager.AUMStream.RING, INVALID_PERCENTAGE_HIGH);
    }

    @Test
    public void setRingVolumeToMax_maxVolume_isOK() {
        AudioUtilityManager.setVolumeToMax(context, AudioUtilityManager.AUMStream.RING);
    }

    @Test
    public void setRingVolumeToMin_minVolume_isOK() {
        AudioUtilityManager.setVolumeToMin(context, AudioUtilityManager.AUMStream.RING);
    }

    @Test
    public void getCurrentRingVolume_method_isOK() {
        AudioUtilityManager.getVolume(context, AudioUtilityManager.AUMStream.RING);
    }

//-----------------------------------------------------------------


}
