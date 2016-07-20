package base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateFormatUtil
{
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String dateFormat(String dateStr, String sourceDateFormatStr, String destDateFormatStr){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sourceDateFormatStr);
            Date date = null;
            date = simpleDateFormat.parse(dateStr);
            simpleDateFormat = new SimpleDateFormat(destDateFormatStr);
            simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @param time 时间
     * @param format 转换格式
     * @return
     */
    public static String longTimeFormat(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date(time);
        return formatter.format(date);
    }
    /**
     * 判断日期是否为该用最后一天
     * 
     * @param dateStr
     *            日期
     * @param dateFormatStr
     *            日期格式
     * @return
     */
    public static boolean isLastDay(String dateStr, String dateFormatStr)
    {
        try
        {
            // 把该日期往后推一天，比较月份，如果月份相同，则不是月份的最后一
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            return month != cal.get(Calendar.MONTH);
            
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
}
