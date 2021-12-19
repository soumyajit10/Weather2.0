package com.example.weather20;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingDetails extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, detal {



    private void setPreferenceSummary(Preference preferences, Object value){
        String stringValue = value.toString();
        String key = preferences.getKey();
        if (preferences instanceof ListPreference){
            ListPreference listPreference = ( ListPreference) preferences;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex>=0){
                preferences.setSummary(listPreference.getEntries()[prefIndex]);
            }else {
                preferences.setSummary(stringValue);
            }
        }

    }


    @Override
    public void onCreatePreferences (@Nullable Bundle savedInstanceState, String s) {


        addPreferencesFromResource(R.xml.set);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference p = preferenceScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);


            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if ( null != preference){
            if (!(preference instanceof CheckBoxPreference)){
                setPreferenceSummary(preference,sharedPreferences.getString(key,""));
            }
        }

    }
}







