package gov.cipam.gi.fragments;

import android.app.Dialog;
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
import gov.cipam.gi.common.SharedPref;

/**
 * Created by karan on 11/20/2017.
 */

public class SettingsFragment extends PreferenceFragment implements
        Preference.OnPreferenceClickListener, View.OnClickListener {

    int pitch, speed;
    Preference tts;
    Button mSaveBtn;
    Dialog mDialog;
    Spinner mTTSSpinner;
    SharedPref sharedPref;
    SeekBar mPitchSeekBar, mSpeedSeekBar;

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
        sharedPref = new SharedPref();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tts = findPreference("tts");
        setmDialog();

        mPitchSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pitch = progress;
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
                speed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tts.setOnPreferenceClickListener(this);
        mSaveBtn.setOnClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "tts":
                Toast.makeText(getActivity(), "tts", Toast.LENGTH_SHORT).show();
                mDialog.show();
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
        mPitchSeekBar = mDialog.findViewById(R.id.seekPitch);
        mSpeedSeekBar = mDialog.findViewById(R.id.seekSpeed);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                getActivity().getResources().getStringArray(R.array.dev_names));
        mTTSSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save_tts:
                SharedPref.saveObjectToSharedPreference(getActivity(), pitch, speed);
                mDialog.dismiss();
                break;
        }

    }
}
