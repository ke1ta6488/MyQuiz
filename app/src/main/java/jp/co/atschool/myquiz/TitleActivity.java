package jp.co.atschool.myquiz;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class TitleActivity extends AppCompatActivity {

    ImageButton imageButton;
    TextView titleTextView, subTextView, mTextView;
    RotateAnimation rotate;
    SeekBar seekBar;
    Button startButton, startButton2, startAllButton;
    Button create, read, update, delete;
    View view;
    int rotateRate;
    float score = 0;
    boolean challenge, all;
    //    SharedPreferences preferences;
//    SharedPreferences.Editor editor;
    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);


        imageButton = (ImageButton) findViewById(R.id.imageButton);
        titleTextView = (TextView) findViewById(R.id.textView);
        subTextView = (TextView) findViewById(R.id.textView2);
        startButton = (Button) findViewById(R.id.startButton);
        startButton2 = (Button) findViewById(R.id.button2);
        startAllButton = (Button) findViewById(R.id.startAllButton);

        Log.d("スコアは", score + "");

        Typeface typeface = Typeface.createFromAsset(getAssets(), "RiiTN_R.otf");
        titleTextView.setTypeface(typeface);
        subTextView.setTypeface(typeface);
        startButton.setTypeface(typeface);
        startButton2.setTypeface(typeface);
        startAllButton.setTypeface(typeface);

        //シークバーの初期値をTextViewに表示
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // ツマミをドラッグしたときに呼ばれる
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rotateRate = seekBar.getProgress();
                startRotation(view);
            }
        });

        //DBのオープン処理
        mRealm = Realm.getDefaultInstance();

//        //stethoの初期化処理
//        Stetho.initialize(
//                Stetho.newInitializerBuilder(this)
//                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
//                        .build());

        //realmテスト用
        mTextView = findViewById(R.id.mTextView);
        create = findViewById(R.id.create);
        read = findViewById(R.id.read);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        //登録ボタンを押した時の処理
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //例外に入らなかったら
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number max = realm.where(MyQuizRealm.class).max("id");
                        long newId = 0;
                        if (max != null) {
                            newId = max.longValue() + 1;
                        }
                        MyQuizRealm myQuizRealm = realm.createObject(MyQuizRealm.class, newId);
                        myQuizRealm.date = new Date();
                        myQuizRealm.detail = "test";
                        myQuizRealm.title = "わはは";
                        mTextView.setText("登録完了¥n" + myQuizRealm.toString());
                    }
                });
            }
        });
        //readボタンを押した時の処理
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<MyQuizRealm> myQuizRealms = realm.where(MyQuizRealm.class).findAll();
                        mTextView.setText("取得");
                        for (MyQuizRealm myQuizRealm : myQuizRealms) {
                            String text = mTextView.getText() + "¥n" + myQuizRealm.toString();
                            mTextView.setText(text);
                        }
                    }
                });
            }
        });
        //updateボタンを押した時の処理
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number max = realm.where(MyQuizRealm.class).max("id");
                        MyQuizRealm myQuizRealm = realm.where(MyQuizRealm.class).equalTo("id", max.longValue()).findFirst();
                        myQuizRealm.title += "<更新>";
                        myQuizRealm.detail += "<更新>";
                        mTextView.setText("更新しました¥n" + myQuizRealm.toString());
                    }
                });
            }
        });
        //deleteボタンを押した時の処理
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number min = realm.where(MyQuizRealm.class).min("id");
                        if (min != null) {
                            MyQuizRealm myQuizRealm = realm.where(MyQuizRealm.class).equalTo("id", min.longValue()).findFirst();
                            myQuizRealm.deleteFromRealm();
                            mTextView.setText("削除しました¥n" + myQuizRealm.toString());
                        }
                    }
                });
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        preferences = getSharedPreferences("score", Context.MODE_PRIVATE);
//        editor = preferences.edit();
//        editor.putInt("HEIGHT", 100);
//        editor.putBoolean("MARRIED", true);
//        editor.apply();
//
//        Log.d("スコアは", score + "");
////            score = prefer.getFloat("score", 0);
////            if (score >= 1.0) startButton2.setVisibility(View.VISIBLE);
//    }


    public void startQuiz(View view) {
        // 画面の遷移用のクラスがIntentクラス
        Intent intent = new Intent(this, MainActivity.class);
        // Intent intent2 = new Intent(this, MainActivity.class);
        challenge = false;
        all = false;
        rotateRate = seekBar.getProgress();
        // intentへ添え字付で値を保持させる
        intent.putExtra("rotateRate", rotateRate);
        intent.putExtra("score", score);
        intent.putExtra("challenge", challenge);
        intent.putExtra("all", all);
        // 指定のActivityを開始する

        startActivityForResult(intent, 0);
        // startActivity(intent2);
    }

    public void startQuiz2(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        challenge = true;
        all = false;
        rotateRate = seekBar.getProgress();
        // intentへ添え字付で値を保持させる
        intent.putExtra("rotateRate", rotateRate);
        intent.putExtra("score", score);
        intent.putExtra("challenge", challenge);
        intent.putExtra("all", all);
        // 指定のActivityを開始する

        startActivityForResult(intent, 0);
    }

    public void startAllQuiz(View view) {
        // 画面の遷移用のクラスがIntentクラス
        Intent intent = new Intent(this, MainActivity.class);
        // Intent intent2 = new Intent(this, MainActivity.class);
        challenge = false;
        all = true;
        rotateRate = seekBar.getProgress();
        // intentへ添え字付で値を保持させる
        intent.putExtra("rotateRate", rotateRate);
        intent.putExtra("score", score);
        intent.putExtra("challenge", challenge);
        intent.putExtra("all", all);
        // 指定のActivityを開始する

        startActivityForResult(intent, 0);
        // startActivity(intent2);
    }

    //クリック用
    public void startRotation(View view) {
        // RotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType,float pivotYValue)
        if (seekBar.getProgress() != 0) {
            rotateRate = seekBar.getProgress();

            rotate = new RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setInterpolator(new LinearInterpolator());
            // animation時間 msec
            rotate.setDuration(2020 + rotateRate * (-20));
            // 繰り返し回数
            rotate.setRepeatCount(-1);
            // animationが終わったそのまま表示にする
            rotate.setFillAfter(true);
            //アニメーションの開始
            imageButton.startAnimation(rotate);
        } else {
            rotate.setRepeatCount(0);
        }
    }

    public void showRanking(View view){
        // 画面の遷移用のクラスがIntentクラス
        Intent intent = new Intent(this, ScoreActivity.class);

        // 指定のActivityを開始する
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            score = data.getFloatExtra("score", score);
            Log.d("スコアは", score + "");
            if (score >= 150.0) startButton2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}