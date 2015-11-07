package app.stackexchange.siddharthshah.myapplication.model;

import org.parceler.Parcel;

/**
 * Created by siddharthshah on 28/09/15.
 */
@Parcel
public class QuestionInfo {

    String ownerName;
    String questionId;
    String questionTitle;
    String questionBody;
    String totalVotes;

    public QuestionInfo() {
    }

    public QuestionInfo(String ownerName, String questionId, String questionTitle, String questionBody, String totalVotes) {
        this.ownerName = ownerName;
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        this.totalVotes = totalVotes;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    public String getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(String totalVotes) {
        this.totalVotes = totalVotes;
    }
}
