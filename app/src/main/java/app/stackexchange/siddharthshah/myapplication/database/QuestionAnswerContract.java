package app.stackexchange.siddharthshah.myapplication.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by siddharthshah on 28/09/15.
 */
public class QuestionAnswerContract {

    public static String CONTENT_AUTHORITY = "app.stackexchange.siddharthshah.myapplication.database";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_QUESTIONS = "questions";
    public static final String PATH_ANSWERS = "answers";

    public static final class QuestionEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY;

        public static final String TABLE_NAME = "questions_table";
        public static final String COL_QUESTION_ID = "question_id";
        public static final String COL_QUESTION_TITLE = "question_title";
        public static final String COL_OWNER_NAME = "question_owner_name";
        public static final String COL_QUESTION_BODY = "body";
        public static final String COL_TOTAL_VOTES = "total_votes";
        public static final String COL_QUESTION_TAG = "tag";

        public static Uri buildQuestionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildQuestionOnQuestionId(Integer questionId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_QUESTIONS).appendPath("id").
                    appendPath(String.valueOf(questionId)).build();
        }

        public static Uri buildQuestionOnTag(String tag) {
            return CONTENT_URI.buildUpon().appendPath(PATH_QUESTIONS).appendPath("tag").
                    appendPath(tag).build();
        }

        public static String getQuestionIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getQuestionTagFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }


    public static final class AnswerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ANSWERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ANSWERS;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ANSWERS;


        public static final String TABLE_NAME = "answers";
        public static final String COL_ANSWER_ID = "answer_id";
        public static final String COL_QUESTION_ID = "question_id";
        public static final String COL_ANSWER_BODY = "answer_body";
        public static final String COL_ANSWER_OWNER = "answer_owner";
        public static final String COL_TOTAL_VOTES = "total_votes";


        public static Uri buildAnswersUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildAnswersOnQuestionId(
                String questionId) {
            return CONTENT_URI.buildUpon()
                    .appendPath("questions").appendPath(questionId).build();
        }

        public static String getQuestionIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }


    }
}
