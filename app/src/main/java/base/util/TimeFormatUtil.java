package base.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huangsheng1 on 2016/5/28.
 */
public class TimeFormatUtil {

    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String DD_HH_MM = "MM-dd HH:mm";
    public final static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String millisTimeFormat(String format, long timeMillis){
        Date date = new Date(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
