package app.stackexchange.siddharthshah.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siddharthshah on 27/09/15.
 */
public class AnswerList {

    @SerializedName("items")
    public List<Answer> answerList ;
}
