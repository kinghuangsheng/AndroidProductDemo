package base.dagger2.component;

import javax.inject.Singleton;

import base.android.App;
import base.dagger2.module.ActivityModule;
import base.dagger2.module.AppModule;
import base.module.BroadcastReceiverModule;
import base.module.DialogModule;
import base.module.FragmentModule;
import base.module.PopupWindowModule;
import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(App app);
    ActivityComponent createActivityComponent(ActivityModule module);
    FragmentComponent createFragmentComponent(FragmentModule module);
    PopupWindowComponent createPopupWindowComponent(PopupWindowModule module);
    DialogComponent createDialogComponent(DialogModule module);
    BroadcastReceiverComponent createBroadcastReceiverComponent(BroadcastReceiverModule module);
}