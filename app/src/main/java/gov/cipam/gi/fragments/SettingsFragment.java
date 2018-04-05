package gov.cipam.gi.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import gov.cipam.gi.R;
import gov.cipam.gi.activities.StatePreferenceActivity;
import gov.cipam.gi.common.SharedPref;
import gov.cipam.gi.utils.Constants;

/**
 * Created by karan on 11/20/2017.
 */

public class SettingsFragment extends PreferenceFragment implements
        Preference.OnPreferenceClickListener
        , View.OnClickListener {
    private final static String KEY_TTS = "tts", KEY_STATE_PREFERENCE = "state_preference";
    private int pitchInitial, speedInitial;
    private float pitch, speed;
    Preference tts, statePreferences;
    Button mSaveBtn;
    Dialog mDialog;
    Spinner mTTSSpinner;
    SeekBar mPitchSeekBar, mSpeedSeekBar;
    SharedPreferences sharedPreferences;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);
        sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        pitchInitial = (int) sharedPreferences.getFloat("pitch", 5);
        speedInitial = (int) sharedPreferences.getFloat("speed", 5);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tts = findPreference(KEY_TTS);
        statePreferences = findPreference(KEY_STATE_PREFERENCE);
        setmDialog();

        mPitchSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pitch = (float) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.clearFocus();
            }
        });

        mSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = (float) progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tts.setOnPreferenceClickListener(this);
        statePreferences.setOnPreferenceClickListener(this);
        mSaveBtn.setOnClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case KEY_TTS:
                Toast.makeText(getActivity(), "tts", Toast.LENGTH_SHORT).show();
                mDialog.show();
                break;
            case KEY_STATE_PREFERENCE:
                startActivity(new Intent(getActivity(), StatePreferenceActivity.class)
                        .putExtra(Constants.INTENT_TYPE, 1));
                break;
        }
        return false;
    }

    private void setmDialog() {
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.dialog_tts);
        mDialog.setTitle("Set Speech");
        mDialog.setCanceledOnTouchOutside(false);

        mSaveBtn = mDialog.findViewById(R.id.button_save_tts);
        mTTSSpinner = mDialog.findViewById(R.id.spinner_tts_accent);
        mSpeedSeekBar = mDialog.findViewById(R.id.seekSpeed);


        mSpeedSeekBar.setThumbOffset(3);
        mPitchSeekBar = mDialog.findViewById(R.id.seekPitch);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                getActivity().getResources().getStringArray(R.array.dev_names));
        mTTSSpinner.setAdapter(adapter);
        mPitchSeekBar.setProgress(pitchInitial);
        mSpeedSeekBar.setProgress(speedInitial);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save_tts:
                SharedPref.saveObjectToSharedPreference(getActivity(), pitch, speed);
                mDialog.dismiss();
                Toast.makeText(getActivity(), pitch + " " + speed, Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
