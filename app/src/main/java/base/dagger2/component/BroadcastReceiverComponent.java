package base.dagger2.component;

import base.android.BaseBroadcastReceiver;
import base.dagger2.scope.PerActivity;
import base.module.BroadcastReceiverModule;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = BroadcastReceiverModule.class)
public interface BroadcastReceiverComponent {
    void inject(BaseBroadcastReceiver receiver);
}
