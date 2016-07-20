package base.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import base.dagger2.component.ActivityComponent;
import base.dagger2.module.ActivityModule;
import base.util.UIUtil;
import butterknife.ButterKnife;

public abstract class BaseActivity extends FragmentActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutViewID());
        UIUtil.initScreenRate(this);
        UIUtil.activityScreenAdapter(this);
        ButterKnife.bind(this);
        dagger2Inject(App.getInstance().getAppComponent().createActivityComponent(new ActivityModule()));
    }
    public abstract int getLayoutViewID();

    public abstract void dagger2Inject(ActivityComponent component);

    public void onActivityFinishOrDestroy(){
        isOnActivityFinishOrDestroyCalled = true;
    }

    private boolean isOnActivityFinishOrDestroyCalled;
    @Override
    public void finish() {
        onActivityFinishOrDestroy();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if(!isOnActivityFinishOrDestroyCalled){
            onActivityFinishOrDestroy();
        }
        super.onDestroy();
    }


}
