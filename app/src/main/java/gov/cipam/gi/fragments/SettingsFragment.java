package gov.cipam.gi.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import gov.cipam.gi.R;

/**
 * Created by karan on 11/20/2017.
 */

public class SettingsFragment extends PreferenceFragment implements
        Preference.OnPreferenceClickListener {

    Preference tts;
    Dialog mDialog;
    Spinner mTTSSpinner;

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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tts = findPreference("tts");
        tts.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "tts":
                Toast.makeText(getActivity(), "tts", Toast.LENGTH_SHORT).show();
                setDialogBox();
                break;
        }
        return false;
    }

    private void setDialogBox() {
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.dialog_tts);
        mTTSSpinner = mDialog.findViewById(R.id.spinner_tts_accent);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                getActivity().getResources().getStringArray(R.array.dev_names));
        mTTSSpinner.setAdapter(adapter);
        mDialog.setTitle("Set Speech");
        mDialog.show();
    }
}
