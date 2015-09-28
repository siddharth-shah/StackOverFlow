package app.stackexchange.siddharthshah.myapplication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by siddharthshah on 27/09/15.
 */
public class Answer {
    @SerializedName("owner")
    public Owner owner;
    @SerializedName("down_vote_count")
    public Integer downVoteCount;
    @SerializedName("score")
    public Integer score;
    @SerializedName("answer_id")
    public Integer answerId;
    @SerializedName("question_id")
    public Integer questionId;
    @SerializedName("body")
    public String body;
}
