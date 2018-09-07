package base.dagger2.component;

import com.product.demo.activity.AssetInventoryActivity;
import com.product.demo.activity.LoginActivity;
import com.product.demo.activity.MainActivity;
import com.product.demo.activity.TestActivity;
import com.product.demo.activity.WriteTagActivity;

import base.android.BaseActivity;
import base.dagger2.module.ActivityModule;
import base.dagger2.scope.PerActivity;
import dagger.Subcomponent;

@PerActivity
@Subcomponent  (modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(BaseActivity act);
    void inject(LoginActivity act);
    void inject(MainActivity act);
    void inject(WriteTagActivity act);
    void inject(AssetInventoryActivity act);
    void inject(TestActivity act);
}