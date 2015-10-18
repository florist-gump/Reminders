package Model;

/**
 * Created by Flo on 17/10/15.
 */
public class SurveyQuestion {
    private String question;
    private int ranking;

    public SurveyQuestion(String question, int ranking) {
        this.question = question;
        this.ranking = ranking;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}
