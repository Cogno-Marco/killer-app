package com.eis.audioUtility;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.NonNull;

import static android.content.Context.AUDIO_SERVICE;

/**
 * This is an Utility class used to manage main Audio Streams (Alarm, Music, and Ring),
 * and to manage Vibration
 *
 * @author Francesco Bau'
 */
public class AudioUtilityManager {

    public static final int MAX_PERCENTAGE = 100;
    public static final int MIN_PERCENTAGE = 0;

    private static final int VIBRATION_TIME = 500;
    private static final int VIBRATION_DELAY = 500;
    private static final int VIBRATION_REPEAT_CODE = 1;

    // Needed to don't show any flag from any Volume variation.
    private static final int ZERO_FLAG_CODE = 0;

    public enum AUMStream {
        ALARM,
        RING,
        MUSIC
    }

    /**
     * Returns the current AudioManager instance.
     *
     * @param context The current Context.
     * @return The current AudioManager instance.
     */
    protected static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    /**
     * Returns the percentage of the current steam Volume.
     *
     * @param context The current Context.
     * @param stream  The chosen Stream (ALARM, RING or MUSIC).
     * @return The current stream Volume (in percentage).
     */
    public static int getVolume(Context context, AUMStream stream) {
        int currentVolume = getAudioManager(context).getStreamVolume(getStream(stream));

        int maxVolume = getMaxVolume(context, stream);
        return Math.round(MAX_PERCENTAGE * currentVolume / maxVolume);
    }

    /**
     * Returns the maximum Stream Volume.
     *
     * @param context The current Context.
     * @param stream  The chosen Stream (ALARM, RING or MUSIC).
     * @return The maximum Stream Volume (real value, not percentage).
     */
    private static int getMaxVolume(Context context, AUMStream stream) {
        return getAudioManager(context).getStreamMaxVolume(getStream(stream));
    }

    /**
     * Sets up the Stream Volume, given a certain percentage.
     *
     * @param context    The current Context.
     * @param stream     The chosen Stream (ALARM, RING or MUSIC).
     * @param percentage Target Volume (expressed in %). It can't be null.
     * @param visibility Makes the user choose to show the flag or not. It can't be null.
     * @throws IllegalArgumentException if percentage is not between 0 and 100.
     */
    public static void setVolume(Context context, AUMStream stream, @NonNull int percentage, @NonNull boolean visibility) throws IllegalArgumentException {
        if (percentage < MIN_PERCENTAGE)
            throw new IllegalArgumentException("Your value is too low. Please insert a value between 0 and 100.");
        if (percentage > MAX_PERCENTAGE)
            throw new IllegalArgumentException("Your value is too high. Please insert a value between 0 and 100.");
        int maxVolume = getMaxVolume(context, stream);

        // Calculate the real value of the new Volume
        int newVolume = maxVolume * percentage;
        newVolume = Math.round(newVolume / MAX_PERCENTAGE);

        if (visibility == true)
            // Sets the Volume.
            getAudioManager(context).setStreamVolume(
                    getStream(stream),
                    newVolume,
                    AudioManager.FLAG_SHOW_UI
            );
        else getAudioManager(context).setStreamVolume(
                getStream(stream),
                newVolume, ZERO_FLAG_CODE
        );

    }


    /**
     * Overload of method setVolume(Context,AUMStream,int,boolean), with default visibility.
     *
     * @param context    The current Context.
     * @param stream     The chosen Stream (ALARM, RING or MUSIC).
     * @param percentage Target Volume (expressed in %). It can't be null.
     * @throws IllegalArgumentException if percentage is not between 0 and 100.
     */
    public static void setVolume(Context context, AUMStream stream, @NonNull int percentage) {
        // By default, flag is shown
        setVolume(context, stream, percentage, true);
    }

    /**
     * Sets up the Stream Volume to its maximum value (in percentage).
     *
     * @param context The current Context.
     * @param stream  The chosen Stream (ALARM, RING or MUSIC).
     */
    public static void setVolumeToMax(Context context, AUMStream stream) {
        setVolume(context, stream, MAX_PERCENTAGE);
    }

    /**
     * Sets up the Stream Volume to its minimum value (in percentage).
     *
     * @param context The current Context.
     * @param stream  The chosen Stream (ALARM, RING or MUSIC).
     */
    public static void setVolumeToMin(Context context, AUMStream stream) {
        setVolume(context, stream, MIN_PERCENTAGE);
    }

    /**
     * Returns the constant representing the Stream.
     *
     * @param stream The chosen Stream (ALARM, RING or MUSIC).
     * @return The constant representing the Stream.
     * @throws IllegalArgumentException if parameter stream is not valid (It shouldn't happen, thanks to the enum type).
     */
    private static int getStream(AUMStream stream) throws IllegalArgumentException {

        switch (stream) {
            case ALARM:
                return AudioManager.STREAM_ALARM;
            case RING:
                return AudioManager.STREAM_RING;
            case MUSIC:
                return AudioManager.STREAM_MUSIC;
            default:
                throw new IllegalArgumentException("Illegal Stream. It should be ALARM, RING or MUSIC .");
        }
    }

    /**
     * Returns the current Vibrator instance.
     *
     * @param context The current Context.
     * @return The current Vibrator instance.
     */
    protected static Vibrator getVibrator(Context context) {
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * Makes the target device vibrate in one shot. Vibration time is set as default.
     *
     * @param context The current Context.
     */
    public static void singleVibrate(Context context) {
        int time = VIBRATION_TIME + VIBRATION_DELAY;
        // Check if API version is >26 or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getVibrator(context).vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            getVibrator(context).vibrate(time);
        }
    }

    /**
     * Makes the target device vibrate multiple times, with default pattern.
     *
     * @param context The current Context.
     */
    public static void multipleVibrate(Context context) {
        // Vibration pattern
        long[] pattern = {VIBRATION_DELAY, VIBRATION_TIME, VIBRATION_DELAY, VIBRATION_TIME};
        // Check if API version is >26 or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getVibrator(context).vibrate(VibrationEffect.createWaveform(pattern, VIBRATION_REPEAT_CODE));
        } else {
            //deprecated in API 26
            getVibrator(context).vibrate(pattern, VIBRATION_REPEAT_CODE);
        }
    }

    /**
     * Makes the target device vibrate multiple times, with a certain pattern.
     *
     * @param context The current Context.
     * @param pattern The pattern of the vibration, created in couples of pause-vibration.
     */
    public static void multipleVibrate(Context context, long[] pattern) {
        // Check if API version is >26 or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getVibrator(context).vibrate(VibrationEffect.createWaveform(pattern, VIBRATION_REPEAT_CODE));
        } else {
            //deprecated in API 26
            getVibrator(context).vibrate(pattern, VIBRATION_REPEAT_CODE);
        }
    }

    /**
     * This method stops the vibration.
     *
     * @param context The current Context.
     */
    public static void stopVibrate(Context context) {
        getVibrator(context).cancel();
    }


}
