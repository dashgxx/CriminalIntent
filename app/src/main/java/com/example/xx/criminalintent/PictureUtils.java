package com.example.xx.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;

/**
 * Created by XX on 2017/3/20.
 */

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destW,int destH)
    {
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);

        float srcW=options.outWidth,
                srcH=options.outHeight;

        int inSampleSize=1;
        if(srcH>destH || srcW> destW){
            if(srcH>srcW)
                inSampleSize=Math.round(srcH/destH);
            else
                inSampleSize= Math.round(srcW/destW);
        }

        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;

        return BitmapFactory.decodeFile(path,options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity)
    {
        Point size=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path,size.x,size.y);
    }
}
