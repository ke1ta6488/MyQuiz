package jp.co.atschool.myquiz;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ScoreActivity extends AppCompatActivity {

    TextView rankingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "RiiTN_R.otf");

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        ScoreRecycleViewAdapter adapter = new ScoreRecycleViewAdapter(this.createDataset());

        TextView rankingTextView= (TextView) findViewById(R.id.rankingTextView);
        rankingTextView.setTypeface(typeface);

        LinearLayoutManager llm = new LinearLayoutManager(this);

        rv.setHasFixedSize(true);

        rv.setLayoutManager(llm);

        rv.setAdapter(adapter);
    }

    private List<ScoreData> createDataset() {

        List<ScoreData> dataset = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            ScoreData data = new ScoreData();
            data.setTitle("カサレアル　太郎" + i + "号");
            data.setScore("カサレアル　太郎は" + i + "個の唐揚げが好き");

            dataset.add(data);
        }
        return dataset;
    }

}
