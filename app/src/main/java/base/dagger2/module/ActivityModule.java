package base.dagger2.module;


import base.android.App;
import dagger.Module;
import dagger.Provides;

/**
 * Created by huangsheng1 on 2016/5/25.
 */
@Module
public class ActivityModule {

    @Provides
    public App provideApp(){
        return App.getInstance();
    }
}