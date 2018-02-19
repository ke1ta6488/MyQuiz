package jp.co.atschool.myquiz;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nttr on 2018/02/07.
 */

public class ScoreViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView detailView;
    public TextView dateView;
    public ImageView rankImageView;

    //recyclerViewに格納するセルの定義
    public ScoreViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.title);
        detailView = (TextView) itemView.findViewById(R.id.detail);
        dateView = (TextView) itemView.findViewById(R.id.date);
        rankImageView = (ImageView) itemView.findViewById(R.id.rankImageView);
    }
}
