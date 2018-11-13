package com.e_young.plugin.httplibr.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取App及系统数据
 */

public class OSUtil {

    /**
     * 获取app版本名
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    /**
     * 获取app版本Code
     *
     * @param context
     * @return
     */
    public static Integer getAppVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 收集设备参数信息
     */
    private static Map<String, String> deviceInfos = new HashMap<>();

    /**
     * 返回ostype 信息
     */

    public static String getOsType(Context context) {
        Map<String, String> deviceInfo = getDeviceInfo(context);
        String brand = deviceInfo.get("BRAND");
        String model = deviceInfo.get("MODEL");
        return model.toLowerCase().contains("mi".toLowerCase()) ? model : (isNotBlank(brand) ? brand : "android");
    }

    /**
     * 返回deviceId
     */
    public static String getDeviceId(Context context) {
        return SystemUtil.getIMEI(context);
    }

    /**
     * 判断字符串
     *
     * @param str
     * @return
     */
    private static boolean isNotBlank(String str) {
        return !(str == null || str.trim().length() == 0);
    }

    /**
     * 收集信息
     *
     * @param application
     * @return
     */

    public static Map<String, String> getDeviceInfo(Context application) {
        if (!deviceInfos.isEmpty()) {
            return deviceInfos;
        }
        try {
            PackageManager pm = application.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(application.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                deviceInfos.put("versionName", versionName);
                deviceInfos.put("versionCode", versionCode);
                deviceInfos.put("AndroidOSVersionCode", Build.VERSION.SDK_INT + "");

            }

        } catch (PackageManager.NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                deviceInfos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
        return deviceInfos;
    }
}
