package com.jimbodev.jimla.projectg;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Settings {
    private static final Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }

    private static SharedPreferences preferences;

    private static final String SETTINGS_FILE = "SETTINGS_FILE";

    private static final String OPTION_TEXT = "OPTION_TEXT";
    private static final String OPTION_VOICE = "OPTION_VOICE";
    private static final String OPTION_SOUND = "OPTION_SOUND";

    public static long getTransitionTime() {
        return transitionTime;
    }
    public static long getAnimationTimer() { return animationTimer; }

    private static final long transitionTime = 400;

    private static final long animationTimer = 1000/100; //100 uppdateringar/sekund (Uppd. var 10e ms)

    private Settings() {
        Log.e("hejsan", "SettingsConstructor");
    }

    public static void loadSettings(Context context) {
        preferences = context.getSharedPreferences(SETTINGS_FILE, 0);

        if(!preferences.contains(OPTION_TEXT)) {
            setOptionText(true);
            Log.e("hejsan","loadSettings");
        }

        if(!preferences.contains(OPTION_VOICE))
            setOptionVoice(true);

        if(!preferences.contains(OPTION_SOUND))
            setOptionSound(true);
    }

    public static void setOptionText(boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(OPTION_TEXT, value);
        editor.apply();
    }

    public static void setOptionVoice(boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(OPTION_VOICE, value);
        editor.apply();
    }

    public static void setOptionSound(boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(OPTION_SOUND, value);
        editor.apply();
    }

    public static boolean isOptionText() {
        return getSetting(OPTION_TEXT);
    }
    public static boolean isOptionVoice() {
        return getSetting(OPTION_VOICE);
    }
    public static boolean isOptionSound() {
        return getSetting(OPTION_SOUND);
    }

    private static boolean getSetting(String option) {
        if(preferences.contains(option)) {
            return preferences.getBoolean(option, false);
        }
        else {
            Log.i("hejsan", option + " does not exist");
            return false;
        }
    }
}
