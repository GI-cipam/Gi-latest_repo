package gov.cipam.gi.firebasemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import gov.cipam.gi.activities.HomePageActivity;
import gov.cipam.gi.common.SharedPref;
import gov.cipam.gi.model.Users;
import gov.cipam.gi.utils.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by Deepak on 1/5/2018.
 */

public class GuestLogin {

    private Context context;
    private FirebaseAuth mAuth;

    public GuestLogin(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public void guestLogin(){

        mAuth.signInAnonymously()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            Toast.makeText(context, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            Users user = new Users();
                            user.setEmail("Please Sign in");
                            user.setName("Guest");
                            SharedPref.saveObjectToSharedPreference(context, Constants.KEY_USER_INFO, Constants.KEY_USER_DATA,user);
                            context.startActivity(new Intent(context, HomePageActivity.class));




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }
}
