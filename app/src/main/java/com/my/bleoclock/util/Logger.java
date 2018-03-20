package com.my.bleoclock.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qkz on 2018/1/27.
 */

public class Logger {
    public static int LOG_LEVEL = 6;
    public static int ERROR = 1;
    public static int WARN = 2;
    public static int INFO = 3;
    public static int DEBUG = 4;
    public static int VERBOS = 5;

    private static String TAG = "Logger";
    static boolean logControl = true;

    public static String apiLogFileDirectory = CommonUtil.getSDCardPath() + "/qkbluetooth/log/";
    public static String apiLogFileName = "bluetoothtest.txt";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static String Tag = null;

    public static void setTag(String mtag) {
        Tag = mtag;
    }

    public static void e(String msg) {
        if (LOG_LEVEL > ERROR) Log.e(Tag, msg);
    }

    public static void w(String msg) {
        if (LOG_LEVEL > WARN) Log.w(Tag, msg);
    }

    public static void i(String msg) {
        if (LOG_LEVEL > INFO) Log.i(Tag, msg);
    }

    public static void d(String msg) {
        if (LOG_LEVEL > DEBUG) Log.d(Tag, msg);
    }

    public static void v(String msg) {
        if (LOG_LEVEL > VERBOS) Log.v(Tag, msg);
    }

    public static void e(String tag, String msg) {
        if (LOG_LEVEL > ERROR) Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL > WARN) Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (LOG_LEVEL > INFO) Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL > DEBUG) Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (LOG_LEVEL > VERBOS) Log.v(tag, msg);
    }

    /**
     * 过滤敏感关键字 不要将敏感关键字数据写到日志文件中
     */
    public static Map<String,String> filterSensitiveKeywordMap;

    static {
        filterSensitiveKeywordMap = new HashMap<String,String>();
    }

    /**
     * 添加过滤敏感关键字
     * @param filterKeys 添加要过滤敏感关键字数组
     */
    public static void addFilterSensitiveKeywords(String[] filterKeys) {
        for(String key:filterKeys) {
            filterSensitiveKeywordMap.put(key,key);
        }
    }

    /**
     * 要删除过滤敏感关键字
     * @param filterKeys 删除要过滤敏感关键字数组
     */
    public static void removeSensitiveKeywords(String[] filterKeys) {
        if(filterSensitiveKeywordMap.size()>0) {
            for(String key:filterKeys) {
                filterSensitiveKeywordMap.remove(key);
            }
        }
    }

    /**
     * 保存日志
     * @param savePathStr 目录路径
     * @param saveFileNameS 文件名
     * @param saveDataStr 日期时间
     * @param saveTypeStr
     */
    public static void saveLog(String savePathStr, String saveFileNameS, String saveDataStr, boolean saveTypeStr) {
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            File folder = new File(savePathStr);
            if(folder.exists() == false || folder.isDirectory() == false){
                folder.mkdirs();
            }
            File f = new File(folder, saveFileNameS);
            fw = new FileWriter(f, saveTypeStr);

            pw = new PrintWriter(fw);
            pw.print(saveDataStr+"\r\n");
            pw.flush();
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {}
            }
            if(pw != null){
                pw.close();
            }
        }
    }

    /**
     *
     * @param log 日志信息
     * @param directoryPath 目录路径
     * @param fileName 文件名
     */
    public static void log(String log,String directoryPath,String fileName) {
        if (logControl) {
            Log.d(TAG, log);
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File path = new File(directoryPath);
                saveLog(path.getPath(), fileName,log, true);
            }
        }
    }

}
