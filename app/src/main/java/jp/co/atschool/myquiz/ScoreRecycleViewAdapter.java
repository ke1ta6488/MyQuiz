package jp.co.atschool.myquiz;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by nttr on 2018/02/07.
 */

public class ScoreRecycleViewAdapter extends RecyclerView.Adapter<ScoreViewHolder> {
    private List<ScoreData> list;


    public ScoreRecycleViewAdapter(List<ScoreData> list) {
        this.list = list;
    }

    //Viewに渡すviewHolderを定義
    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.score, parent, false);
        ScoreViewHolder vh = new ScoreViewHolder(inflate);

        Typeface typeface = Typeface.createFromAsset(vh.itemView.getContext().getAssets(), "RiiTN_R.otf");
        vh.titleView.setTypeface(typeface);
        vh.dateView.setTypeface(typeface);
        vh.detailView.setTypeface(typeface);

        return vh;
    }

    //ScoreViewHolderで定義した項目に対して値をセットする
    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        holder.titleView.setText(list.get(position).getTitle());
        holder.detailView.setText(list.get(position).getDetail());


        holder.dateView.setText(list.get(position).getDate());
        if (position == 0) {
            holder.rankImageView.setImageResource(R.drawable.rank_1);
        } else if (position == 1) {
            holder.rankImageView.setImageResource(R.drawable.rank_2);
        } else if (position == 2) {
            holder.rankImageView.setImageResource(R.drawable.rank_3);
        } else {
            holder.rankImageView.setImageResource(R.drawable.mousukosi);
        }
    }

    //格納しているセル数を返す
    @Override
    public int getItemCount() {
        return list.size();
    }
}