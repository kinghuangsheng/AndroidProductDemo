package base.dagger2.component;

import base.android.BaseFragment;
import base.dagger2.scope.PerActivity;
import base.module.FragmentModule;

import dagger.Subcomponent;
@PerActivity
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(BaseFragment fragment);
}