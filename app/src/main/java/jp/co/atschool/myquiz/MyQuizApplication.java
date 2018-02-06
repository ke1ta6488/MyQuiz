package jp.co.atschool.myquiz;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by nttr on 2018/01/31.
 */

public class MyQuizApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
