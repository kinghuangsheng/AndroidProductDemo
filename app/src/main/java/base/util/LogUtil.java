package base.util;

import android.util.Log;

public final class LogUtil
{
    
    private LogUtil()
    {
        
    }
    

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
    
}
