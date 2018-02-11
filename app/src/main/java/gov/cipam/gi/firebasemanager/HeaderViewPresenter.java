package gov.cipam.gi.firebasemanager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import gov.cipam.gi.R;
import gov.cipam.gi.common.SharedPref;
import gov.cipam.gi.model.Users;
import gov.cipam.gi.utils.Constants;
import gov.cipam.gi.utils.TextDrawable;


public class HeaderViewPresenter implements View.OnClickListener {

    private Context context;
    private View view;
    private DrawerLayout drawer;
    private ImageView imgevProfile;
    private TextView textvName;
    private TextView textvEmail;
    private TextView txtvNameInitials;
    private FirebaseAuth mAuth;
    private Users users;

    public HeaderViewPresenter(Context context, View view, DrawerLayout drawer,FirebaseAuth mAuth,Users users) {
        this.context=context;
        this.view = view;
        this.drawer = drawer;
        this.mAuth=mAuth;
        this.users=users;
    }

    public void initViews() {
        imgevProfile =  view.findViewById(R.id.userImage);
        textvName =view.findViewById(R.id.nav_header_name);
        textvEmail = view.findViewById(R.id.nav_header_email);
        txtvNameInitials=view.findViewById(R.id.userNameShort);
        txtvNameInitials.setVisibility(View.INVISIBLE);

        view.setOnClickListener(this);
        setData();
    }

    private void setData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("HeaderViewPresenter", "user.getDisplayName(): " + user.getDisplayName());
        users = SharedPref.getSavedObjectFromPreference(context, Constants.KEY_USER_INFO,Constants.KEY_USER_DATA,Users.class);
        if(users!=null) {

            textvEmail.setText(context.getString(R.string.login_request));
            textvName.setText(context.getString(R.string.hi));
            imgevProfile.setImageResource(R.drawable.account);

            if (mAuth.getCurrentUser().isAnonymous()){
                textvEmail.setText(context.getString(R.string.login_request));
                textvName.setText(context.getString(R.string.hi));
                imgevProfile.setImageResource(R.drawable.account);
            }
            else
            {
                txtvNameInitials.setVisibility(View.VISIBLE);
                textvEmail.setText(user.getEmail());
                txtvNameInitials.setText(users.getName().substring(0,1));
                textvName.setText("Hi "+users.getName().substring(0,1).toUpperCase()+users.getName().substring(1));
                imgevProfile.setImageResource(R.drawable.circle);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == view) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
