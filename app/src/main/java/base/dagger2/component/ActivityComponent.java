package base.dagger2.component;

import com.product.demo.activity.LoginActivity;

import base.android.BaseActivity;
import base.dagger2.module.ActivityModule;
import base.dagger2.scope.PerActivity;
import dagger.Subcomponent;

@PerActivity
@Subcomponent  (modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(BaseActivity act);
    void inject(LoginActivity act);
}