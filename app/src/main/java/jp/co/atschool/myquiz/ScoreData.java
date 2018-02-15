package jp.co.atschool.myquiz;

/**
 * Created by nttr on 2018/02/07.
 */

public class ScoreData {
    private String title;
    private String detail;
    private String date;

    public void setDetail(String score) {
        this.detail = score;
    }
    public String getDetail() {
        return detail;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setDate(String date) {this.date = date;}
    public String getDate() {return date;}
}

