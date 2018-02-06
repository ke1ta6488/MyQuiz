package jp.co.atschool.myquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    List<Quiz> quizzes;
    int point, quizNum;
    float score;
    int[] tdhkNum;
    int rotateRate;
    Button[] buttons;
    ImageButton speechImageButton;
    TextView contentTextView, countTextView, speechTextView;
    ImageButton imageButton;
    String comment;
    String[] tdhk = {"北海道", "青森県", "岩手県", "秋田県", "宮城県", "山形県", "福島県", "新潟県", "石川県", "富山県", "福井県", "茨城県", "栃木県", "群馬県", "千葉県", "埼玉県", "東京都",
            "神奈川県", "静岡県", "愛知県", "山梨県", "長野県", "岐阜県", "三重県", "滋賀県", "和歌山県", "奈良県", "大阪府", "京都府", "兵庫県", "岡山県", "鳥取県", "島根県", "山口県", "広島県",
            "香川県", "徳島県", "愛媛県", "高知県", "福岡県", "佐賀県", "長崎県", "大分県", "熊本県", "宮崎県", "鹿児島県", "沖縄県"};
    RotateAnimation rotate;
    boolean challenge, all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 現在のintentを取得する
        Intent intent = getIntent();
        // intentから指定キーの文字列を取得する
        rotateRate = intent.getIntExtra("rotateRate", 50);
        score = intent.getIntExtra("score", 0);
        challenge = intent.getBooleanExtra("challenge", false);
        all = intent.getBooleanExtra("all", false);

        Log.d("２人モードか", "" + all);
        // 関連付け
        countTextView = (TextView) findViewById(R.id.countTextView);
        contentTextView = (TextView) findViewById(R.id.contentTextView);
        imageButton = (ImageButton) findViewById(R.id.image);
        speechTextView = (TextView) findViewById(R.id.speechTextView);

        //ボタンの関連付け
        buttons = new Button[4];
        buttons[0] = findViewById(R.id.button1);
        buttons[1] = findViewById(R.id.button2);
        buttons[2] = findViewById(R.id.button3);
        buttons[3] = findViewById(R.id.button4);
        speechImageButton = findViewById(R.id.speechImageButton);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "RiiTN_R.otf");
        contentTextView.setTypeface(typeface);
        countTextView.setTypeface(typeface);
        for (Button button : buttons) {
            button.setTypeface(typeface);
            button.setOnClickListener(this);
        }

        //モードによって見た目を変える
        if (all) {
            buttons[0].setVisibility(View.GONE);
            buttons[1].setVisibility(View.GONE);
            buttons[2].setVisibility(View.GONE);
            buttons[3].setVisibility(View.GONE);
            speechImageButton.setVisibility(View.VISIBLE);
        } else {
            buttons[0].setVisibility(View.VISIBLE);
            buttons[1].setVisibility(View.VISIBLE);
            buttons[2].setVisibility(View.VISIBLE);
            buttons[3].setVisibility(View.VISIBLE);
            speechImageButton.setVisibility(View.GONE);
        }

        //問題をセット
        reset();
    }

    public void speechToText(View view) {
        if (getPackageManager().queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0).size()==0){
            Log.d("音声入力がサポートされているか","サポートされていません");
            return;
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // 指定のActivityを開始する
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data.hasExtra(RecognizerIntent.EXTRA_RESULTS)) {
                List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (results.size() > 0) {
                    String str = "";
                    for (String s : results) {
                        str += s + "¥n";
                    }
                    speechTextView.setText(str);
                }
            }
            Quiz quiz = quizzes.get(quizNum);
            // ボタンの文字と、答えが同じかチェックします。
            if (speechTextView.getText()==tdhk[quiz.answerNum]) {
                point++;
                score += 10.0 + (float) rotateRate / 10.0;
                Toast.makeText(this, "正解", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "はずれ", Toast.LENGTH_SHORT).show();
            }
            next();
        }
    }

    public int[] selectTdhk() {
        int[] num = new int[4];
        List<Integer> numAry = new ArrayList<>();
        for (int i = 0; i < 47; i++) {
            numAry.add(i);
        }
        Collections.shuffle(numAry);
        num[0] = numAry.get(0);
        num[1] = numAry.get(1);
        num[2] = numAry.get(2);
        num[3] = numAry.get(3);
        Log.d("4つの数字", num[0] + "," + num[1] + "," + num[2] + "," + num[3]);
        return num;
    }

    public void init() {//選択肢4つと答えを決める
        quizzes = new ArrayList<>();
        point = 0;
        score = 0;
        quizNum = 0;
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            tdhkNum = selectTdhk();
            int answerNum = rnd.nextInt(4);
            Quiz quiz1 = new Quiz(tdhk[tdhkNum[0]], tdhk[tdhkNum[1]], tdhk[tdhkNum[2]], tdhk[tdhkNum[3]], tdhkNum[answerNum]);
            quizzes.add(quiz1);
        }
    }

    public void reset() {
        init();
        show();
    }

    public void next() {
        quizNum++;
        Intent intent;
        if (quizNum < quizzes.size()) {
            show();
        } else {
            //結果の表示方法
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("診断結果");

            builder.setCancelable(false);
            if (challenge) score = score * 2;
            builder.setMessage(quizzes.size() + "問中" + point + "問正解" + "\n" + "動体視力スコア：" + (int) score);
            builder.setPositiveButton("リトライ", new DialogInterface.OnClickListener() {
                @Override
                //もう一度やらせる
                public void onClick(DialogInterface dialog, int which) {
                    reset();
                }
            });
            builder.setNeutralButton("タイトルへ戻る", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 画面の遷移用のクラスがIntentクラス
//                    Intent intent = new Intent(MainActivity.this, TitleActivity.class);
//                    startActivity(intent);
                    Intent data = new Intent();
                    data.putExtra("score", score);
                    setResult(RESULT_OK, data);
                    finish();
                }
            });
            builder.show();
        }
    }

    public void startRotation() {
        if (rotateRate != 0) {
            rotate = new RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            rotate.setInterpolator(new LinearInterpolator());
            // animation時間 msec
            rotate.setDuration(1000 + rotateRate * (-9));
            // 繰り返し回数
            rotate.setRepeatCount(-1);
            // animationが終わったそのまま表示にする
            rotate.setFillAfter(true);
            //アニメーションの開始
            imageButton.startAnimation(rotate);
        }
    }

    public void show() {
        countTextView.setText((quizNum + 1) + "問目");
        Quiz quiz = quizzes.get(quizNum);
        buttons[0].setText(quiz.option1);
        buttons[1].setText(quiz.option2);
        buttons[2].setText(quiz.option3);
        buttons[3].setText(quiz.option4);
        //imageNum = rnd.nextInt(4);
        if (challenge) {
            Matrix matrix = new Matrix();
            matrix.preScale(-1.0f, 1.0f);
            BitmapDrawable bd;
            Bitmap bmp, bitmap;

            switch (quiz.answerNum) {
                case 0:
                    imageButton.setImageResource(R.mipmap.hokkaidou);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 1:
                    imageButton.setImageResource(R.mipmap.aomori);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 2:
                    imageButton.setImageResource(R.mipmap.iwate);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 3:
                    imageButton.setImageResource(R.mipmap.akita);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 4:
                    imageButton.setImageResource(R.mipmap.miyagi);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 5:
                    imageButton.setImageResource(R.mipmap.yamagata);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 6:
                    imageButton.setImageResource(R.mipmap.fukushima);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 7:
                    imageButton.setImageResource(R.mipmap.niigata);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 8:
                    imageButton.setImageResource(R.mipmap.ishikawa);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 9:
                    imageButton.setImageResource(R.mipmap.toyama);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 10:
                    imageButton.setImageResource(R.mipmap.fukui);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 11:
                    imageButton.setImageResource(R.mipmap.ibaraki);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 12:
                    imageButton.setImageResource(R.mipmap.tochigi);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 13:
                    imageButton.setImageResource(R.mipmap.gunma);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 14:
                    imageButton.setImageResource(R.mipmap.chiba);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 15:
                    imageButton.setImageResource(R.mipmap.saitama);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 16:
                    imageButton.setImageResource(R.mipmap.tokyo);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 17:
                    imageButton.setImageResource(R.mipmap.kanagawa);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 18:
                    imageButton.setImageResource(R.mipmap.shizuoka);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 19:
                    imageButton.setImageResource(R.mipmap.aichi);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 20:
                    imageButton.setImageResource(R.mipmap.yamanashi);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 21:
                    imageButton.setImageResource(R.mipmap.nagano);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 22:
                    imageButton.setImageResource(R.mipmap.gifu);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 23:
                    imageButton.setImageResource(R.mipmap.mie);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 24:
                    imageButton.setImageResource(R.mipmap.shiga);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 25:
                    imageButton.setImageResource(R.mipmap.wakayama);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 26:
                    imageButton.setImageResource(R.mipmap.nara);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 27:
                    imageButton.setImageResource(R.mipmap.osaka);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 28:
                    imageButton.setImageResource(R.mipmap.kyouto);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 29:
                    imageButton.setImageResource(R.mipmap.hyougo);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 30:
                    imageButton.setImageResource(R.mipmap.okayama);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 31:
                    imageButton.setImageResource(R.mipmap.tottori);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 32:
                    imageButton.setImageResource(R.mipmap.shimane);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 33:
                    imageButton.setImageResource(R.mipmap.yamaguchi);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 34:
                    imageButton.setImageResource(R.mipmap.hiroshima);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 35:
                    imageButton.setImageResource(R.mipmap.kagawa);
                    startRotation();
                    break;
                case 36:
                    imageButton.setImageResource(R.mipmap.tokushima);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 37:
                    imageButton.setImageResource(R.mipmap.ehime);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 38:
                    imageButton.setImageResource(R.mipmap.kouchi);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 39:
                    imageButton.setImageResource(R.mipmap.fukuoka);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 40:
                    imageButton.setImageResource(R.mipmap.saga);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 41:
                    imageButton.setImageResource(R.mipmap.nagasaki);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 42:
                    imageButton.setImageResource(R.mipmap.ooita);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 43:
                    imageButton.setImageResource(R.mipmap.kumamoto);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 44:
                    imageButton.setImageResource(R.mipmap.miyazaki);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 45:
                    imageButton.setImageResource(R.mipmap.kagoshima);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
                case 46:
                    imageButton.setImageResource(R.mipmap.okinawa);
                    bd = (BitmapDrawable) imageButton.getDrawable();
                    bmp = bd.getBitmap();
                    bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    imageButton.setImageBitmap(bitmap);
                    startRotation();
                    break;
            }
        } else {
            switch (quiz.answerNum) {
                case 0:
                    imageButton.setImageResource(R.mipmap.hokkaidou);
                    startRotation();
                    break;
                case 1:
                    imageButton.setImageResource(R.mipmap.aomori);
                    startRotation();
                    break;
                case 2:
                    imageButton.setImageResource(R.mipmap.iwate);
                    startRotation();
                    break;
                case 3:
                    imageButton.setImageResource(R.mipmap.akita);
                    startRotation();
                    break;
                case 4:
                    imageButton.setImageResource(R.mipmap.miyagi);
                    startRotation();
                    break;
                case 5:
                    imageButton.setImageResource(R.mipmap.yamagata);
                    startRotation();
                    break;
                case 6:
                    imageButton.setImageResource(R.mipmap.fukushima);
                    startRotation();
                    break;
                case 7:
                    imageButton.setImageResource(R.mipmap.niigata);
                    startRotation();
                    break;
                case 8:
                    imageButton.setImageResource(R.mipmap.ishikawa);
                    startRotation();
                    break;
                case 9:
                    imageButton.setImageResource(R.mipmap.toyama);
                    startRotation();
                    break;
                case 10:
                    imageButton.setImageResource(R.mipmap.fukui);
                    startRotation();
                    break;
                case 11:
                    imageButton.setImageResource(R.mipmap.ibaraki);
                    startRotation();
                    break;
                case 12:
                    imageButton.setImageResource(R.mipmap.tochigi);
                    startRotation();
                    break;
                case 13:
                    imageButton.setImageResource(R.mipmap.gunma);
                    startRotation();
                    break;
                case 14:
                    imageButton.setImageResource(R.mipmap.chiba);
                    startRotation();
                    break;
                case 15:
                    imageButton.setImageResource(R.mipmap.saitama);
                    startRotation();
                    break;
                case 16:
                    imageButton.setImageResource(R.mipmap.tokyo);
                    startRotation();
                    break;
                case 17:
                    imageButton.setImageResource(R.mipmap.kanagawa);
                    startRotation();
                    break;
                case 18:
                    imageButton.setImageResource(R.mipmap.shizuoka);
                    startRotation();
                    break;
                case 19:
                    imageButton.setImageResource(R.mipmap.aichi);
                    startRotation();
                    break;
                case 20:
                    imageButton.setImageResource(R.mipmap.yamanashi);
                    startRotation();
                    break;
                case 21:
                    imageButton.setImageResource(R.mipmap.nagano);
                    startRotation();
                    break;
                case 22:
                    imageButton.setImageResource(R.mipmap.gifu);
                    startRotation();
                    break;
                case 23:
                    imageButton.setImageResource(R.mipmap.mie);
                    startRotation();
                    break;
                case 24:
                    imageButton.setImageResource(R.mipmap.shiga);
                    startRotation();
                    break;
                case 25:
                    imageButton.setImageResource(R.mipmap.wakayama);
                    startRotation();
                    break;
                case 26:
                    imageButton.setImageResource(R.mipmap.nara);
                    startRotation();
                    break;
                case 27:
                    imageButton.setImageResource(R.mipmap.osaka);
                    startRotation();
                    break;
                case 28:
                    imageButton.setImageResource(R.mipmap.kyouto);
                    startRotation();
                    break;
                case 29:
                    imageButton.setImageResource(R.mipmap.hyougo);
                    startRotation();
                    break;
                case 30:
                    imageButton.setImageResource(R.mipmap.okayama);
                    startRotation();
                    break;
                case 31:
                    imageButton.setImageResource(R.mipmap.tottori);
                    startRotation();
                    break;
                case 32:
                    imageButton.setImageResource(R.mipmap.shimane);
                    startRotation();
                    break;
                case 33:
                    imageButton.setImageResource(R.mipmap.yamaguchi);
                    startRotation();
                    break;
                case 34:
                    imageButton.setImageResource(R.mipmap.hiroshima);
                    startRotation();
                    break;
                case 35:
                    imageButton.setImageResource(R.mipmap.kagawa);
                    startRotation();
                    break;
                case 36:
                    imageButton.setImageResource(R.mipmap.tokushima);
                    startRotation();
                    break;
                case 37:
                    imageButton.setImageResource(R.mipmap.ehime);
                    startRotation();
                    break;
                case 38:
                    imageButton.setImageResource(R.mipmap.kouchi);
                    startRotation();
                    break;
                case 39:
                    imageButton.setImageResource(R.mipmap.fukuoka);
                    startRotation();
                    break;
                case 40:
                    imageButton.setImageResource(R.mipmap.saga);
                    startRotation();
                    break;
                case 41:
                    imageButton.setImageResource(R.mipmap.nagasaki);
                    startRotation();
                    break;
                case 42:
                    imageButton.setImageResource(R.mipmap.ooita);
                    startRotation();
                    break;
                case 43:
                    imageButton.setImageResource(R.mipmap.kumamoto);
                    startRotation();
                    break;
                case 44:
                    imageButton.setImageResource(R.mipmap.miyazaki);
                    startRotation();
                    break;
                case 45:
                    imageButton.setImageResource(R.mipmap.kagoshima);
                    startRotation();
                    break;
                case 46:
                    imageButton.setImageResource(R.mipmap.okinawa);
                    startRotation();
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {

        Button clickedButton = (Button) view;
        Quiz quiz = quizzes.get(quizNum);
        // ボタンの文字と、答えが同じかチェックします。
        if (TextUtils.equals(clickedButton.getText(), tdhk[quiz.answerNum])) {
            point++;
            score += 10.0 + (float) rotateRate / 10.0;
            Toast.makeText(this, "正解", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "はずれ", Toast.LENGTH_SHORT).show();
        }

        // 次の問題にアップデートします。
        next();
    }
}
