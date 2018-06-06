package kr.ac.kaist.orz;

import android.app.Application;

import kr.ac.kaist.orz.models.User;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController extends Application {
    private static ApplicationController instance;
    private OrzApi orzApi;
    private User user;


    public static ApplicationController getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationController.instance = this;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ssal.sparcs.org:14545")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        orzApi = retrofit.create(OrzApi.class);
    }

    public OrzApi getApi() {
        return orzApi;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
