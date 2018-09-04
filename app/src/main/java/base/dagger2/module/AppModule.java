package base.dagger2.module;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.product.demo.greendao.DaoMaster;
import com.product.demo.greendao.DaoSession;
import com.product.demo.retrofit.service.RequestService;

import javax.inject.Singleton;

import base.android.App;
import base.util.ToastUtil;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by huangsheng1 on 2016/5/25.
 */
@Module
public class AppModule {

    public final static String BASE_URL = "http://www.baidu.com";
    public final static String DB_NAME = "demo.db";

    private App app;

    public AppModule(App app) {
        this.app = app;
    }
    @Singleton
    @Provides
    public SharedPreferences provideSharedPreferences(App app) {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }
    @Singleton
    @Provides
    public App provideApp(){
        return app;
    }

    @Singleton
    @Provides
    public ToastUtil provideToastUtil(){
        return new ToastUtil(app);
    }

    @Singleton
    @Provides
    public DaoSession provideDaoSession(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(app,
                DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging); // <-- this is the important line!
        //1.创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().client(httpClient.build())
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
    @Provides
    public RequestService provideRequestService(Retrofit retrofit){
        RequestService service = retrofit.create(RequestService.class);
        return service;
    }


}