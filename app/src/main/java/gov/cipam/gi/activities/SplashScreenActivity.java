package gov.cipam.gi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import gov.cipam.gi.R;
import gov.cipam.gi.utils.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!preferences.getBoolean(Constants.ONBOARDING_COMPLETE, false))

                {
                    startActivity(new Intent(SplashScreenActivity.this, IntroActivity.class));
                } else

                {
                    startActivity(new Intent(SplashScreenActivity.this, HomePageActivity.class));
                }

                finish();
            }

        }, SPLASH_TIME_OUT);

    }
}

