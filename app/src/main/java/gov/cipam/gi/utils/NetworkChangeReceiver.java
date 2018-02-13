package gov.cipam.gi.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.widget.FrameLayout;


public abstract class NetworkChangeReceiver extends BroadcastReceiver {

    private Snackbar snackbar;
    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private int layoutId;
    Activity activity;

    public NetworkChangeReceiver(Snackbar snackbar, Activity activity) {
        this.snackbar = snackbar;
        this.activity=activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        dismissSnackbar();

        if (intent.getAction().equals(CONNECTIVITY_CHANGE_ACTION)) {

            FrameLayout frameLayout=activity.findViewById(setUpLayout());
        }
    }

    /**
     * PURPOSE: Dismisses error snackbar when network becomes available
     */
    protected void dismissSnackbar(){
        snackbar.dismiss();
    }

    /**
     * PURPOSE: Show error fragment when there is no network state
     */
    protected int setUpLayout(){
     return getLayoutId();
    }

    protected abstract int getLayoutId();
}
