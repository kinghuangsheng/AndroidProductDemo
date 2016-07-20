package base.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.TextView;

public final class UIUtil {
    private UIUtil() {

    }

    private static float screenRate = 1;

    private static float heightRate = 1;

    private static float widthRate = 1;

    private static int screenHeight = 480;

    private static int screenWidth = 800;

    private static Activity activity;

    private static final int DEFAULT_WIDTH = 480;

    private static final int DEFAULT_HEIGHT = 800;

    public static void initScreenRate(Activity act) {
        WindowManager windowManager = act.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        setScreenHeight(display.getHeight());
        setScreenWidth(display.getWidth());
        Configuration mConfiguration = act.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
            LogUtil.info("mConfiguration", "mConfiguration 横屏");
            setHeightRate((float) getScreenHeight() / DEFAULT_WIDTH);
            setWidthRate((float) getScreenWidth() / DEFAULT_HEIGHT);
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //竖屏
            LogUtil.info("mConfiguration", "mConfiguration 竖屏");
            setHeightRate((float) getScreenHeight() / DEFAULT_HEIGHT);
            setWidthRate((float) getScreenWidth() / DEFAULT_WIDTH);
        }
        setScreenRate(UIUtil.heightRate < UIUtil.widthRate ? UIUtil.heightRate
                : UIUtil.widthRate);
    }

    /**
     * Activity适配
     *
     * @param act
     */
    public static void activityScreenAdapter(Activity act) {
        viewScreenAdapter(act.findViewById(android.R.id.content));
    }

    /**
     * Dialog适配
     */
    public static void dialogScreenAdapter(Dialog dialog) {
        viewScreenAdapter(dialog.findViewById(android.R.id.content));
    }

    /**
     * Dialog风格的activity适配
     */
    public static void activityInDialogThemeScreenAdapter(Activity act) {
        viewScreenAdapter(act.findViewById(android.R.id.content));
    }

    /**
     * Layout View适配
     */
    public static View viewScreenAdapter(final View view) {
        if (view instanceof ViewGroup) {
            int count = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < count; i++) {
                final View childView = ((ViewGroup) view).getChildAt(i);
                if (childView instanceof ViewGroup) {
                    remeasure(childView);
                    viewScreenAdapter((ViewGroup) childView);
                } else {
                    remeasure(childView);
                }
            }

        } else {
            remeasure(view);
        }
        return view;
    }

    private static void remeasure(View view) {
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        if (lp.height == LayoutParams.WRAP_CONTENT) {
            if (view.getBackground() != null) {
                lp.height = heightAdapter(view.getBackground().getIntrinsicHeight());
            }
        } else if (lp.height == LayoutParams.MATCH_PARENT) {

        } else {
            lp.height = heightAdapter(lp.height);
        }
        if (lp.width == LayoutParams.WRAP_CONTENT) {
            if (view.getBackground() != null) {
                lp.width = widthAdapter(view.getBackground().getIntrinsicWidth());
            }
        } else if (lp.width == LayoutParams.MATCH_PARENT) {

        } else {
            lp.width = widthAdapter(lp.width);
        }
        lp.bottomMargin = heightAdapter(lp.bottomMargin);
        lp.topMargin =  heightAdapter(lp.topMargin);
        lp.leftMargin = widthAdapter(lp.leftMargin);
        lp.rightMargin = widthAdapter(lp.rightMargin);
        int leftPadding = widthAdapter(view.getPaddingLeft());
        int topPadding = heightAdapter(view.getPaddingTop());
        int rightPadding = widthAdapter(view.getPaddingRight());
        int bottomPadding = heightAdapter(view.getPaddingBottom());
        view.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    tv.getTextSize() * getScreenRate());
            if ("wrap_content".equals(tv.getTag())) {
                lp.height = LayoutParams.WRAP_CONTENT;
                lp.width = LayoutParams.WRAP_CONTENT;
            } else if ("width_adapter".equals(tv.getTag())) {
                lp.width = (int) (lp.width * screenRate);
            } else if ("height_adapter".equals(tv.getTag())) {
                lp.height = LayoutParams.WRAP_CONTENT;
            }
        }

    }

    public static float getScreenRate() {
        return screenRate;
    }

    public static void setScreenRate(float screenRate) {
        UIUtil.screenRate = (screenRate > 1 ? screenRate : 1);
    }

    public static int widthAdapter(int width) {
        return (int) (width * widthRate);
    }

    public static int heightAdapter(int height) {
        return (int) (height * heightRate);
    }

    public static void setHeightRate(float heightRate) {
        UIUtil.heightRate = (heightRate > 1 ? heightRate : 1);
    }

    public static void setWidthRate(float widthRate) {
        UIUtil.widthRate = (widthRate > 1 ? widthRate : 1);
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        UIUtil.screenHeight = screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        UIUtil.screenWidth = screenWidth;
    }

}
