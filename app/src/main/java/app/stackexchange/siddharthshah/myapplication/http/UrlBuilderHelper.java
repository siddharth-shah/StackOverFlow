package app.stackexchange.siddharthshah.myapplication.http;

import app.stackexchange.siddharthshah.myapplication.model.ApiExtraParams;

/**
 * Created by siddharthshah on 07/11/15.
 */
public class UrlBuilderHelper {


    public static String buildUrlSearchQuestions(String searchInput, ApiExtraParams apiExtraParams) {


        return Constants.BASE_URI.buildUpon().
                appendPath(Constants.SEARCH_ENDPOINT)
                .appendQueryParameter(Constants.PAGE, apiExtraParams.getPage())
                .appendQueryParameter(Constants.PAGE_SIZE, "" + apiExtraParams.getPageSize())
                .appendQueryParameter(Constants.ORDER, apiExtraParams.getOrder())
                .appendQueryParameter(Constants.SORT_CRITERIA, apiExtraParams.getSortCriteria())
                .appendQueryParameter(Constants.SEARCH_INPUT, searchInput)
                .appendQueryParameter(Constants.SITE_TO_SEARCH, apiExtraParams.getSiteToSearch())
                .appendQueryParameter(Constants.FILTER_CHOSEN, apiExtraParams.getFilter()).build().toString();


    }

    public static String buildUrlAnsToQuestionId(String questionId, ApiExtraParams apiExtraParams) {
        return Constants.BASE_URI.buildUpon()
                .appendPath("questions")
                .appendPath(questionId)
                .appendPath("answers")
                .appendQueryParameter(Constants.PAGE, apiExtraParams.getPage())
                .appendQueryParameter(Constants.PAGE_SIZE, "" + apiExtraParams.getPageSize())
                .appendQueryParameter(Constants.ORDER, apiExtraParams.getOrder())
                .appendQueryParameter(Constants.SORT_CRITERIA, apiExtraParams.getSortCriteria())
                .appendQueryParameter(Constants.SITE_TO_SEARCH, apiExtraParams.getSiteToSearch())
                .appendQueryParameter(Constants.FILTER_CHOSEN, apiExtraParams.getFilter()).build().toString();

    }

}


