package app.stackexchange.siddharthshah.myapplication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by siddharthshah on 27/09/15.
 */
public class Owner {
    @SerializedName("user_id")
    public Integer userId;
    @SerializedName("profile_image")
    public String profileImage;
    @SerializedName("display_name")
    public String displayName;
}
