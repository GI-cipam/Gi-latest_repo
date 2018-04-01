package gov.cipam.gi.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;

/**
 * Created by Deepak on 11/18/2017.
 */

public class SharedPref {

    public static void saveObjectToSharedPreference(Context context, float pitch, float speed) {
        SharedPreferences.Editor myPrefs=context.getSharedPreferences("myPrefs",Context.MODE_PRIVATE).edit();
        myPrefs.putFloat("pitch",pitch);
        myPrefs.putFloat("speed",speed);
        myPrefs.apply();
    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

}
