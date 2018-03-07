package gov.cipam.gi.common;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import gov.cipam.gi.R;



public class HeaderViewPresenter implements View.OnClickListener {

    private Context context;
    private View view;
    private DrawerLayout drawer;
    private ImageView imgevProfile;
    private TextView textvName;
    private TextView textvEmail;
    private TextView txtvNameInitials;


    public HeaderViewPresenter(Context context, View view, DrawerLayout drawer) {
        this.context=context;
        this.view = view;
        this.drawer = drawer;

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

        textvName.setText(context.getString(R.string.hi));
        imgevProfile.setImageResource(R.drawable.ic_account);

    }

    @Override
    public void onClick(View v) {
        if (v == view) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
