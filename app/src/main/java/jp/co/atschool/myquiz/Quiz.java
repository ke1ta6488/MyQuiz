package jp.co.atschool.myquiz;

/**
 * Created by nttr on 2017/12/07.
 */

public class Quiz {

    String option1;
    String option2;
    String option3;
    String option4;
    int answerNum;

    public Quiz(String option1, String option2, String option3, String option4, int answerNum) {
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answerNum = answerNum;
    }
}
