package base.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by huangsheng1 on 2016/5/28.
 */
public class ToastUtil {

    private Context context;
    private int showTime = Toast.LENGTH_SHORT;

    public ToastUtil(Context context){
        this.context = context;
    }


    public void showString(String s){
        Toast.makeText(context, s, showTime).show();
    }
    public void showString(int resId){
        Toast.makeText(context, resId, showTime).show();
    }
}
