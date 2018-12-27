package com.ttc.behavior.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class AndroidUtil {

    public static String getAppID(Context context) {
        return context.getPackageName();
    }

    public static String getMeta(Context context, String key) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName()
                    , PackageManager.GET_META_DATA);

            if (applicationInfo.metaData != null) {
                return applicationInfo.metaData.getString(key, "");
            }
        } catch (PackageManager.NameNotFoundException e) {
            TTCLogger.e(e.getMessage());
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
                TTCLogger.e(e.getMessage());
            } catch (IOException e) {
                TTCLogger.e(e.getMessage());
            }
        }
        return false;
    }
}
