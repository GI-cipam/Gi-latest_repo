package gov.cipam.gi.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import gov.cipam.gi.R;

/**
 * Created by karan on 1/2/2018.
 */

public class PaletteGenerate implements Palette.PaletteAsyncListener{

    public Palette createPaletteAsync(Bitmap bitmap) {
        return Palette.from(bitmap).generate();
    }

    private Palette createPaletteSync(Bitmap bitmap) {
        return Palette.from(bitmap).generate();
    }

    private Palette.Swatch checkVibrantSwatch(Palette p) {
        Palette.Swatch vibrant = p.getVibrantSwatch();
        if (vibrant != null) {
            return vibrant;
        }
        return p.getDarkVibrantSwatch();
    }

    public void setViewColor(Bitmap bitmap, TextView textView) {

        if(bitmap!=null) {
            Palette p = createPaletteAsync(bitmap);
            Palette.Swatch vibrantSwatch = checkVibrantSwatch(p);

            if(vibrantSwatch!=null){
                textView.setBackgroundColor(vibrantSwatch.getRgb());
            }
            else {
                textView.setBackgroundColor(Color.parseColor("#88000000"));
            }
        }
    }

    @Override
    public void onGenerated(Palette palette) {

    }
}
