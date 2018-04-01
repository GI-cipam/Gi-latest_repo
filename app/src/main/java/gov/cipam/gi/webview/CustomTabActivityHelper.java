package gov.cipam.gi.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

import java.util.List;

public class CustomTabActivityHelper implements ServiceConnectionCallback {

    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mCustomTabsClient;
    private CustomTabsServiceConnection mConnection;
    private CustomTabConnectionCallback mConnectionCallback;

    /**
     * Interface to handle cases where the chrome custom tab cannot be opened.
     */
    public interface CustomTabFallback {
        void openUri(Activity activity, Uri uri);
    }

    /**
     * Interface to handle connection callbacks to the custom tab. We'll use this to handle UI changes
     * when the connection is either connected or disconnected.
     */
    public interface CustomTabConnectionCallback {
        void onCustomTabConnected();

        void onCustomTabDisconnected();
    }


    /**
     * Utility method for opening a custom tab
     *
     * @param activity         Host activity
     * @param customTabsIntent custom tabs intent
     * @param uri              uri to open
     * @param fallback         fallback to handle case where custom tab cannot be opened
     */
    public static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri, CustomTabFallback fallback, boolean fallbackToBrowser) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, uri);
        } else if (fallback == null || fallbackToBrowser) {
            activity.startActivity(new Intent("android.intent.action.VIEW").setData(uri));
        } else {
            fallback.openUri(activity, uri);
        }
    }


    /**
     * Binds the activity to the custom tabs service.
     *
     * @param activity activity to be "bound" to the service
     */
    public void bindCustomTabsService(Activity activity) {
        if (mCustomTabsClient != null) return;

        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (packageName == null) return;

        mConnection = new ServiceConnection(this);
        CustomTabsClient.bindCustomTabsService(activity, packageName, mConnection);
    }

    /**
     * Unbinds the activity from the custom tabs service
     *
     * @param activity
     */
    public void unbindCustomTabsService(Activity activity) {
        if (mConnection == null) return;
        activity.unbindService(mConnection);
        mCustomTabsClient = null;
        mCustomTabsSession = null;
        mConnection = null;
    }

    /**
     * Creates or retrieves an exiting CustomTabsSession.
     *
     * @return a CustomTabsSession.
     */
    public CustomTabsSession getSession() {
        if (mCustomTabsClient == null) {
            mCustomTabsSession = null;
        } else if (mCustomTabsSession == null) {
            mCustomTabsSession = mCustomTabsClient.newSession(null);
        }
        return mCustomTabsSession;
    }

    /**
     * Register a Callback to be called when connected or disconnected from the Custom Tabs Service.
     *
     * @param connectionCallback
     */
    public void setConnectionCallback(CustomTabConnectionCallback connectionCallback) {
        this.mConnectionCallback = connectionCallback;
    }

    /**
     * @return true if call to mayLaunchUrl was accepted.
     * @see {@link CustomTabsSession#mayLaunchUrl(Uri, Bundle, List)}.
     */
    public boolean mayLaunchUrl(Uri uri, Bundle extras, List<Bundle> otherLikelyBundles) {
        if (mCustomTabsClient == null) return false;

        CustomTabsSession session = getSession();
        if (session == null) return false;

        return session.mayLaunchUrl(uri, extras, otherLikelyBundles);
    }

    @Override
    public void onServiceConnected(CustomTabsClient client) {
        mCustomTabsClient = client;
        mCustomTabsClient.warmup(0L);
        if (mConnectionCallback != null) {
            mConnectionCallback.onCustomTabConnected();
        }
    }

    @Override
    public void onServiceDisconnected() {
        mCustomTabsClient = null;
        mConnection = null;
        if (mConnectionCallback != null) {
            mConnectionCallback.onCustomTabDisconnected();
        }
    }
}
