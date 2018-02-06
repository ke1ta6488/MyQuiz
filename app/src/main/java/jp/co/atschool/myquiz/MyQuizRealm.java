package jp.co.atschool.myquiz;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nttr on 2018/01/31.
 */

public class MyQuizRealm extends RealmObject {
    @PrimaryKey
    public  long id;
    public Date date;
    public  String title;
    public  String detail;

}
