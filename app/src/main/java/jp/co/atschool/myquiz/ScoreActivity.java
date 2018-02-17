package jp.co.atschool.myquiz;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScoreActivity extends AppCompatActivity {

    Realm realm;
    List<ScoreData> scoreset, sortedScoreset;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_score);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "RiiTN_R.otf");
        final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'E'曜日'k'時ごろ'");
        scoreset = new ArrayList<>();
        sortedScoreset = new ArrayList<>();

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
                    //data.setDate(("a").toString());
                    data.setDate(sdf.format(myQuizRealm.date));
                    scoreset.add(data);
                }
                count = scoreset.size();
            }
        });

        sortedScoreset = ScoreSort(scoreset);
//        Log.d("そーと前", sortedScoreset.get(0).getDetail()+", "+sortedScoreset.get(1).getDetail());
//        Log.d("そーと後", sortedScoreset.get(0).getDetail()+", "+sortedScoreset.get(1).getDetail());

        //ScoreRecycleViewAdapter adapter = new ScoreRecycleViewAdapter(this.createDataset());
        ScoreRecycleViewAdapter adapter = new ScoreRecycleViewAdapter(sortedScoreset);

        //TextView rankingTextView = (TextView) findViewById(R.id.rankingTextView);
        //rankingTextView.setTypeface(typeface);

        LinearLayoutManager llm = new LinearLayoutManager(this);

        rv.setHasFixedSize(true);

        rv.setLayoutManager(llm);

        rv.setAdapter(adapter);

        rv.addItemDecoration(new RecyclerViewDecoration(getBaseContext()));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<ScoreData> ScoreSort(List<ScoreData> scoreDataList) {
        //Comparator<ScoreData> scoreDataComparator=Comparator.comparing(ScoreData::getDetail).thenComparing(Comparator.comparing(ScoreData::getDetail));
        Comparator<ScoreData> scoreDataComparator = Comparator.comparing(ScoreData::getDetail).reversed();
        List<ScoreData> sortedScoreDataList = scoreDataList.stream()
                .sorted(scoreDataComparator).collect(Collectors.toList());
//        sortedScoreDataList.stream().sorted((person1, person2) -> person1.getDetail().compareTo(person2.getDetail()))
//                .forEach(ScoreData::getTitle);

        Log.d("そーと前", scoreDataList.get(0).getDetail()+", "+scoreDataList.get(1).getDetail()+", "+scoreDataList.get(2).getDetail()
                +", "+scoreDataList.get(3).getDetail()+", "+scoreDataList.get(4).getDetail());
        Log.d("そーと後", sortedScoreDataList.get(0).getDetail()+", "+sortedScoreDataList.get(1).getDetail()+", "+sortedScoreDataList.get(2).getDetail()
                +", "+sortedScoreDataList.get(3).getDetail()+", "+sortedScoreDataList.get(4).getDetail());
        return sortedScoreDataList;

    }
}