package app.stackexchange.siddharthshah.myapplication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by siddharthshah on 27/09/15.
 */
public class Question {
    @SerializedName("owner")
    public Owner owner;
    @SerializedName("delete_vote_count")
    public Integer deleteVoteCount;
    @SerializedName("close_vote_count")
    public Integer closeVoteCount;
    @SerializedName("down_vote_count")
    public Integer downVoteCount;
    @SerializedName("up_vote_count")
    public Integer upVoteCount;
    @SerializedName("question_id")
    public Integer questionId;
    @SerializedName("title")
    public String title;
    @SerializedName("body")
    public String body;


}
