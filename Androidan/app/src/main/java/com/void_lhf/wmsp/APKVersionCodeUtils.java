package com.void_lhf.wmsp;

import android.content.*;
import android.content.pm.*;

public class APKVersionCodeUtils
{
	public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            versionCode = mContext.getPackageManager().
				getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
	
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
				getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
