package base.dagger2.component;

import base.android.BasePopupWindow;
import base.dagger2.scope.PerActivity;
import base.module.PopupWindowModule;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = PopupWindowModule.class)
public interface PopupWindowComponent {
    void inject(BasePopupWindow window);
}