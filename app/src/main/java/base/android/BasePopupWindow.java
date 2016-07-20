package base.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.PopupWindow;

import base.dagger2.component.PopupWindowComponent;
import base.module.PopupWindowModule;

import base.util.UIUtil;
import butterknife.ButterKnife;

/**
 * Created by huangsheng1 on 2016/6/1.
 */
public abstract class BasePopupWindow extends PopupWindow {

    public BasePopupWindow(Context context, int layoutViewId, int width, int height){
        super(UIUtil.viewScreenAdapter(LayoutInflater.from(context).inflate(layoutViewId, null)),
                UIUtil.widthAdapter(width), UIUtil.heightAdapter(height));
        ButterKnife.bind(this, getContentView());
        dagger2Inject(App.getInstance().getAppComponent().createPopupWindowComponent(new PopupWindowModule()));
    }

    protected abstract void dagger2Inject(PopupWindowComponent component);

}
