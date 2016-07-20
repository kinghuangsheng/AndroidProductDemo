package base.android;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import base.dagger2.component.DialogComponent;
import base.module.DialogModule;

import base.util.UIUtil;
import butterknife.ButterKnife;

/**
 * Created by huangsheng1 on 2016/6/5.
 */
public abstract class BaseDialog extends Dialog {

    private boolean hasStyle;

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int style) {
        super(context, style);
        hasStyle = true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!hasStyle){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setCanceledOnTouchOutside(false);
        setContentView(getLayoutViewID());
        UIUtil.dialogScreenAdapter(this);
        ButterKnife.bind(this);
        dagger2Inject(App.getInstance().getAppComponent().createDialogComponent(new DialogModule()));
        onCreate();
    }

    public abstract void onCreate();

    public abstract int getLayoutViewID();

    public abstract void dagger2Inject(DialogComponent component);
}
