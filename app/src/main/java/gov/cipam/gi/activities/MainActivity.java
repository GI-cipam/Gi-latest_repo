package gov.cipam.gi.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import gov.cipam.gi.R;

public class MainActivity extends BaseActivity{

    int imageViewMargin;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.overlapImage);
    }

    @Override
    protected int getToolbarID() {
        return 0;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        imageViewMargin = -1 * (mImageView.getHeight() / 2);
        Log.d("xolo", String.valueOf(mImageView.getMeasuredHeight()));

        setMargins(mImageView, 0, 0, 0, imageViewMargin);
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
