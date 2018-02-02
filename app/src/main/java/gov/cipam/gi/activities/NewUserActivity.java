package gov.cipam.gi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import gov.cipam.gi.R;
import gov.cipam.gi.firebasemanager.GuestLogin;
import gov.cipam.gi.utils.Constants;

public class NewUserActivity extends AppCompatActivity implements View.OnClickListener{

    Button              skipLoginBtn,registerUserButton;
    FirebaseAuth        mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        launchActivity();
        mAuth = FirebaseAuth.getInstance();

        skipLoginBtn=findViewById(R.id.skipLogin);
        registerUserButton=findViewById(R.id.registerUser);

        skipLoginBtn.setOnClickListener(this);
        registerUserButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, HomePageActivity.class));
            finish();
        }
    }

    public void launchActivity() {
        SharedPreferences preferences =
                getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);

        if (!preferences.getBoolean(Constants.ONBOARDING_COMPLETE, false)) {

            startActivity(new Intent(this, IntroActivity.class));
        }
    }

    @Override
    public void onClick(View view) {

        int id=view.getId();

        switch (id){

            case R.id.skipLogin:
                GuestLogin login = new GuestLogin(this);
                login.guestLogin();
                break;

            case R.id.registerUser:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }
}

