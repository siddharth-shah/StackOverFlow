package app.stackexchange.siddharthshah.myapplication.http;

import android.net.Uri;

/**
 * Created by siddharthshah on 28/09/15.
 */
public class Constants {

    public static final String HOST = "api.stackexchange.com";
    public static final String API_VERSION = "2.2";

    public static final String SEARCH_ENDPOINT = "search";

    public static final String QUESTIONS_ENDPOINT = "/questions";
    public static final String ANSWERS_ENDPOINT = "/answers?";

    //Query Parameters
    public static final String PAGE = "page";
    public static final String PAGE_SIZE = "pagesize";
    public static final String ORDER = "order";
    public static final String SORT_CRITERIA = "sort";
    public static final String SEARCH_INPUT = "intitle";
    public static final String SITE_TO_SEARCH = "site";
    public static final String FILTER_CHOSEN = "filter";
    private static Uri.Builder builder = new Uri.Builder();
    public static final Uri BASE_URI = builder.scheme("https")
            .authority(Constants.HOST)
            .appendPath(Constants.API_VERSION).build();


    ;


}
