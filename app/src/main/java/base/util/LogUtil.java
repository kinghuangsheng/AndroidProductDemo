package base.util;

import android.util.Log;

public final class LogUtil
{
    
    private LogUtil()
    {
        
    }
    
    private static final String TAG = "MyLog";
    
    private static final boolean LOGGER = true;
    
    public static void error(Object obj, String str)
    {
        if (LOGGER)
        {
            Log.e(obj.getClass().getName(), str);
        }
    }
    
    public static void info(Object obj, String str)
    {
        if (LOGGER)
        {
            Log.i(obj.getClass().getName(), str);
        }
    }
    
    public static void info(String str)
    {
        if (LOGGER)
        {
            info("", str);
        }
    }
    
    public static void info(String tag, String str)
    {
        if (LOGGER)
        {
            Log.i(tag, str);
        }
    }
    
    public static void error(String tag, String str)
    {
        if (LOGGER)
        {
            Log.e(tag, str);
        }
    }
    
    public static void dialog(String str)
    {
        // UiHandler.dialog(str);
    }
    
    // 添加新的方法
    public static void i(Object obj, String msg)
    {
        if (LOGGER)
        {
            Log.i(TAG, obj.getClass().getName() + "-->" + msg);
        }
    }
    
    public static void i(String tag, String msg)
    {
        if (LOGGER)
        {
            Log.i(TAG, tag + "-->" + msg);
        }
    }
}
