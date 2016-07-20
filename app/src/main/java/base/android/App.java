package base.android;

import android.app.Application;

import base.dagger2.component.DaggerAppComponent;
import base.dagger2.module.AppModule;
import base.dagger2.component.AppComponent;


public class App extends Application
{
    
    private static App instance;
    
    public static App getInstance()
    {
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private AppComponent appComponent;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        this.appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        this.appComponent.inject(this);
    }
    
}
