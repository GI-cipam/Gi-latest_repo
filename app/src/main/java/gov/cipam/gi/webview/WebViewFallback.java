package gov.cipam.gi.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import gov.cipam.gi.activities.WebViewActivity;

import static gov.cipam.gi.utils.Constants.EXTRA_URL;

/**
 * Created by karan on 4/1/2018.
 */

public class WebViewFallback implements CustomTabActivityHelper.CustomTabFallback {
    @Override
    public void openUri(Activity activity, Uri uri) {
        Intent webViewIntent = new Intent(activity, WebViewActivity.class);
        webViewIntent.putExtra(EXTRA_URL, uri.toString());
        activity.startActivity(webViewIntent);
    }
}
