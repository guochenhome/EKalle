package com.e_young.plugin.httplibr.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
     * 这里主要是给极光推送提供一个唯一的签名
     * 这里是自定义的生成的一串唯一值
     * <p>
     * 在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来。不需要权限，平板设备通用。获取成功率也较高，缺点是设备恢复出厂设置会重置。另外就是某些厂商的低版本系统会有bug，返回的都是相同的AndroidId。获取方式如下：
     * <p>
     * Android系统2.3版本以上可以通过下面的方法得到Serial Number，且非手机设备也可以通过该接口获取。不需要权限，通用性也较高，但我测试发现红米手机返回的是 0123456789ABCDEF 明显是一个顺序的非随机字符串。也不一定靠谱。
     * <p>
     * 综上述，AndroidId 和 Serial Number 的通用性都较好，并且不受权限限制，如果刷机和恢复出厂设置会导致设备标识符重置这一点可以接受的话，那么将他们组合使用时，唯一性就可以应付绝大多数设备了。
     */
    public static String getDeviceId(Context context) {

        try {
            String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            String id = androidID + Build.SERIAL;
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (Exception error) {
            return "";
        }
    }

    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
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
