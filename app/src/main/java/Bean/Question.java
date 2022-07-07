package Bean;

import java.io.Serializable;

public class Question implements Serializable {
    private String questionId;
    private String questionInfor;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int rightAnswer;
    private int chosenAnswer;

    public Question() {
    }

    public Question(String questionId, String questionInfor, String answer1, String answer2, String answer3, String answer4, int rightAnswer, int chosenAnswer) {
        this.questionId = questionId;
        this.questionInfor = questionInfor;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.rightAnswer = rightAnswer;
        this.chosenAnswer = chosenAnswer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionInfor() {
        return questionInfor;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setQuestionInfor(String questionInfor) {
        this.questionInfor = questionInfor;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public int getChosenAnswer() {
        return chosenAnswer;
    }

    public void setChosenAnswer(int chosenAnswer) {
        this.chosenAnswer = chosenAnswer;
    }
}
