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

public class TitleActivity extends AppCompatActivity {

    ImageButton imageButton;
    TextView titleTextView, subTextView;
    RotateAnimation rotate;
    SeekBar seekBar;
    Button startButton, startButton2, startAllButton;
    View view;
    int rotateRate;
    float score=0;
    boolean challenge;
//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        titleTextView = (TextView) findViewById(R.id.textView);
        subTextView = (TextView) findViewById(R.id.textView2);
        startButton = (Button) findViewById(R.id.button);
        startButton2 = (Button) findViewById(R.id.button2);
        startAllButton = (Button) findViewById(R.id.startAllButton);

        Log.d("スコアは", score + "");

        Typeface typeface = Typeface.createFromAsset(getAssets(), "RiiTN_R.otf");
        titleTextView.setTypeface(typeface);
        subTextView.setTypeface(typeface);
        startButton.setTypeface(typeface);
        startButton2.setTypeface(typeface);
        startAllButton.setTypeface(typeface);

//        imageButton.setImageResource(R.mipmap.kagoshima);
//        Matrix matrix = new Matrix();
//        matrix.preScale(-1.0f, 1.0f);
//        BitmapDrawable bd = (BitmapDrawable)imageButton.getDrawable();
//        Bitmap bmp = bd.getBitmap();
//        Bitmap bitmap  = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
//        imageButton.setImageBitmap(bitmap);

        //シークバーの初期値をTextViewに表示
        seekBar = (SeekBar)findViewById(R.id.seekBar);
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

    public void startQuiz(View view){
        // 画面の遷移用のクラスがIntentクラス
        Intent intent = new Intent( this, MainActivity.class );
        // Intent intent2 = new Intent(this, MainActivity.class);
challenge=false;
        rotateRate = seekBar.getProgress();
        // intentへ添え字付で値を保持させる
        intent.putExtra( "rotateRate", rotateRate);
        intent.putExtra("score", score);
        intent.putExtra("challenge", challenge);
        // 指定のActivityを開始する

        startActivityForResult(intent, 0);
        // startActivity(intent2);
    }

    public void startQuiz2(View view) {
        Intent intent = new Intent( this, MainActivity.class );
challenge=true;
        rotateRate = seekBar.getProgress();
        // intentへ添え字付で値を保持させる
        intent.putExtra( "rotateRate", rotateRate);
        intent.putExtra("score", score);
        intent.putExtra("challenge", challenge);
        // 指定のActivityを開始する

        startActivityForResult(intent, 0);
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
        }
        else{
            rotate.setRepeatCount(0);
                Log.d("回転",  "しないはず");
        }
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
}