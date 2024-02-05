package com.example.meccanocar.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppInfo {
    /**
     * Get application version code
     *
     * @param context
     *            Interface to global information about an application
     *            environment
     * @return String of the form "CC", where CC is the version code (e.g., "25")
     */
    public static String getAppVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo info = pm.getPackageInfo(packageName, 0);

            String version = info.versionName;
            return version;
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * Get application version name
     *
     * @param context
     *            Interface to global information about an application
     *            environment
     * @return String of the form "VV", where VV is the version name (e.g., "1.0.3")
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo info = pm.getPackageInfo(packageName, 0);

            String version = String.valueOf(info.versionCode);
            return version;
        }
        catch (Exception ex) {
            return null;
        }
    }
}