package com.acn.behavior.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import com.acn.behavior.db.ACNSp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Utils {


    public static String getMeta(Context context, String key) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName()
                    , PackageManager.GET_META_DATA);

            if (applicationInfo.metaData != null) {
                return applicationInfo.metaData.getString(key, "");
            }
        } catch (PackageManager.NameNotFoundException e) {
            SDKLogger.e(e.getMessage());
        }

        return "";
    }

    public static Drawable getApplicationIcon(Context context) {

        Drawable icon = null;
        try {
            PackageManager pm = context.getPackageManager();
            icon = pm.getApplicationIcon(context.getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }

    public static String getApplicationName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void moveToFront(Context context) {

        if (isRunning(context)) {
            //获取ActivityManager
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            //获得当前运行的task
            List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(100);
            for (ActivityManager.RunningTaskInfo taskInfo : taskList) {
                //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
                if (taskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                    am.moveTaskToFront(taskInfo.id, ActivityManager.MOVE_TASK_WITH_HOME);
                    return;
                }
            }
            startLaunchActivity(context);
        } else {
            startLaunchActivity(context);
        }
    }

    private static void startLaunchActivity(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        context.startActivity(intent);
    }

    public static boolean isRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isJsonValid(String content) {
        if (!TextUtils.isEmpty(content)) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.readTree(content);
                return true;
            } catch (JsonProcessingException e) {
                SDKLogger.e(e.getMessage());
            } catch (IOException e) {
                SDKLogger.e(e.getMessage());
            }
        }
        return false;
    }

    public static String getLocationCode( Context context) {
        Locale local = context.getResources().getConfiguration().locale;
        //local.getISO3Country():USA
        //local.getDisplayCountry():United States
        //local.getCountry():US
        return local.getCountry();
    }

    public static String getClientId(){
        return Build.SERIAL;
    }

    public static String format2ETHAddress(String ttcAddress)throws NullPointerException{
        if (ttcAddress == null || ttcAddress .equals("") ) {
            throw  new NullPointerException();
        }

        if (ttcAddress.startsWith("t") || ttcAddress.startsWith("T")) {
            return "0x" + ttcAddress.substring(2);
        }else {
            return ttcAddress;
        }
    }

    public static String stringToHex(String str) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char ch = str.charAt(i);
            sb.append(Integer.toHexString(ch));
        }
        return sb.toString();
    }

    /**
     *
     * @param hash
     * @return  the sample is : "ufo:1:oplog:md5:hash_value:appid"
     */
    public static String addHash2Ufo1OplogMd5(String hash) {
        if (!TextUtils.isEmpty(hash)) {
            return Constants.DATA_PREFIX + hash + ":" + ACNSp.getDappId();
        } else {
            return Constants.DATA_PREFIX + ACNSp.getDappId();
        }
    }
}
