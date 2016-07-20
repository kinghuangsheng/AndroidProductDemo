package base.android;

import android.content.BroadcastReceiver;

import base.dagger2.component.BroadcastReceiverComponent;
import base.module.BroadcastReceiverModule;

/**
 * Created by huangsheng1 on 2016/7/15.
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {

    public BaseBroadcastReceiver(){
        dagger2Inject(App.getInstance().getAppComponent().createBroadcastReceiverComponent(new BroadcastReceiverModule()));
    }

    public abstract void dagger2Inject(BroadcastReceiverComponent component);
}
