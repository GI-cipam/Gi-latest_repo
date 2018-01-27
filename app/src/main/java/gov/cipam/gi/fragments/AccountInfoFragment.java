package gov.cipam.gi.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gov.cipam.gi.R;
import gov.cipam.gi.common.SharedPref;
import gov.cipam.gi.model.Users;
import gov.cipam.gi.utils.Constants;

/**
 * Created by karan on 1/25/2018.
 */

public class AccountInfoFragment extends Fragment implements View.OnClickListener{

    TextView                        tvChangePass,tvUpdatePass,tvNameShort;
    ImageView                       ivProfile;
    private TextInputLayout         mChangePassFieldLayout;
    EditText                        mNameField,mEmailField,mChangePassField;
    DatabaseReference               mDatabaseUser;
    private static String           TAG = "AccountInfoActivity";
    private static String           user_id;
    private FirebaseAuth mAuth;
    Users users;

    public static AccountInfoFragment newInstance() {

        Bundle args = new Bundle();

        AccountInfoFragment fragment = new AccountInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference(Constants.KEY_USERS);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_info,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNameShort=view.findViewById(R.id.nameInitials);
        ivProfile=view.findViewById(R.id.profileImage);
        mNameField =view.findViewById(R.id.nameField);
        mEmailField =view.findViewById(R.id.emailField);
        tvChangePass =view.findViewById(R.id.changePass);

        setData();
        
        tvChangePass.setOnClickListener(this);
        
    }

    private void setData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("HeaderViewPresenter", "user.getDisplayName(): " + user.getDisplayName());
        users = SharedPref.getSavedObjectFromPreference(getContext(), Constants.KEY_USER_INFO,Constants.KEY_USER_DATA,Users.class);

        if(users!=null) {

            mEmailField.setText(getContext().getString(R.string.login_request));
            mNameField.setText(getContext().getString(R.string.hi));
            ivProfile.setImageResource(R.drawable.account);

            if (mAuth.getCurrentUser().isAnonymous()){
                mEmailField.setText(getContext().getString(R.string.login_request));
                mNameField.setText(getContext().getString(R.string.hi));
                ivProfile.setImageResource(R.drawable.account);
            }
            else
            {
                tvNameShort.setVisibility(View.VISIBLE);
                tvNameShort.setText(users.getName().substring(0,1));
                mEmailField.setText(user.getEmail());
                mNameField.setText("Hi "+users.getName().substring(0,1).toUpperCase()+users.getName().substring(1));
                ivProfile.setImageResource(R.drawable.account);
            }
        }
    }

    private void changePasswordDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Change Password");
        alertDialog.setMessage("Enter new password");

        final EditText newPass = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        newPass.setLayoutParams(lp);
        alertDialog.setView(newPass);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                final String newPassword = newPass.getText().toString();
                if (!TextUtils.isEmpty(newPassword)){
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), getString(R.string.toast_password_change_success),
                                                Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "User password updated.");
                                    }
                                    else Toast.makeText(getContext(), getString(R.string.toast_password_change_failed),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else newPass.setError("Enter Password");
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.changePass:
                changePasswordDialog();
                break;
        }
    }
}
