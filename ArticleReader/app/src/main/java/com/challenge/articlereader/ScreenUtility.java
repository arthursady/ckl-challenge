package com.challenge.articlereader;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by arthur on 07/12/16.
 */

public class ScreenUtility {

    private double screenSize;

    public ScreenUtility(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        screenSize=screenInches;
    }

    public double getScreenSize(){return screenSize;}
}