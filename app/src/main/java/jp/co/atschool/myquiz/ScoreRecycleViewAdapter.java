package jp.co.atschool.myquiz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by nttr on 2018/02/07.
 */

public class ScoreRecycleViewAdapter extends RecyclerView.Adapter<ScoreViewHolder>{
    private List<ScoreData> list;

    public ScoreRecycleViewAdapter(List<ScoreData> list) {
        this.list = list;
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.score, parent,false);
        ScoreViewHolder vh = new ScoreViewHolder(inflate);
        return vh;
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        holder.titleView.setText(list.get(position).getTitle());
        holder.detailView.setText(list.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
