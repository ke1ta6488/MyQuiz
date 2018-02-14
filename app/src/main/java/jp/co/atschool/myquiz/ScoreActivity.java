package jp.co.atschool.myquiz;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScoreActivity extends AppCompatActivity {

    Realm realm;
    List<ScoreData> scoreset;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_score);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "RiiTN_R.otf");
        final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        scoreset = new ArrayList<>();

        realm = Realm.getDefaultInstance();
        //ランキングに投入する値を取ってくる
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<MyQuizRealm> myQuizRealms = realm.where(MyQuizRealm.class).findAll();
                for (MyQuizRealm myQuizRealm : myQuizRealms) {
                    ScoreData data = new ScoreData();
                    data.setDetail(myQuizRealm.detail);
                    data.setTitle(myQuizRealm.title);
                    scoreset.add(data);
                }
                count = scoreset.size();
            }
        });


        //ScoreRecycleViewAdapter adapter = new ScoreRecycleViewAdapter(this.createDataset());
        ScoreRecycleViewAdapter adapter = new ScoreRecycleViewAdapter(scoreset);

        TextView rankingTextView = (TextView) findViewById(R.id.rankingTextView);
        rankingTextView.setTypeface(typeface);

        LinearLayoutManager llm = new LinearLayoutManager(this);

        rv.setHasFixedSize(true);

        rv.setLayoutManager(llm);

        rv.setAdapter(adapter);

    }
}