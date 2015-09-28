package app.stackexchange.siddharthshah.myapplication.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by siddharthshah on 28/09/15.
 */
public class QuestionAnswerProvider extends ContentProvider {


    private static final String sQuestionId =
            QuestionAnswerContract.QuestionEntry.TABLE_NAME +
                    "." + QuestionAnswerContract.QuestionEntry.COL_QUESTION_ID + " = ? ";
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private QuestionAnswerDbHelper mOpenHelper;

    static final int QUESTION = 100;
    static final int ANSWER = 104;
    static final int QUESTION_WITH_QUESTION_ID = 101;
    static final int ANSWER_WITH_QUESTION_ID = 102;
    static final int QUESTION_WITH_QUESTION_TAG = 103;


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = QuestionAnswerContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, QuestionAnswerContract.PATH_QUESTIONS, QUESTION);
        matcher.addURI(authority, QuestionAnswerContract.PATH_ANSWERS, ANSWER);
        matcher.addURI(authority, QuestionAnswerContract.PATH_QUESTIONS + "/id/#", QUESTION_WITH_QUESTION_ID);
        matcher.addURI(authority, QuestionAnswerContract.PATH_QUESTIONS + "/tag/*", QUESTION_WITH_QUESTION_TAG);
        matcher.addURI(authority, QuestionAnswerContract.PATH_ANSWERS + "/questions/#", ANSWER_WITH_QUESTION_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new QuestionAnswerDbHelper(getContext(), null, null, 1);
        return true;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;


        switch (sUriMatcher.match(uri)) {
            case QUESTION_WITH_QUESTION_TAG:
                String questionTag = QuestionAnswerContract.QuestionEntry.getQuestionTagFromUri(uri);
                selection = QuestionAnswerContract.QuestionEntry.COL_QUESTION_TAG + " = ? ";
                selectionArgs = new String[]{questionTag};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        QuestionAnswerContract.QuestionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;


            case QUESTION_WITH_QUESTION_ID: {
                String questionId = QuestionAnswerContract.QuestionEntry.getQuestionIdFromUri(uri);
                selection = QuestionAnswerContract.QuestionEntry.COL_QUESTION_ID + " = ? ";
                selectionArgs = new String[]{questionId};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        QuestionAnswerContract.QuestionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case ANSWER_WITH_QUESTION_ID: {
                String questionId = QuestionAnswerContract.AnswerEntry.getQuestionIdFromUri(uri);
                selection = QuestionAnswerContract.QuestionEntry.COL_QUESTION_ID + " = ? ";
                selectionArgs = new String[]{questionId};

                retCursor = mOpenHelper.getReadableDatabase().query(
                        QuestionAnswerContract.AnswerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ANSWER:
                return QuestionAnswerContract.QuestionEntry.CONTENT_ITEM_TYPE;
            case QUESTION_WITH_QUESTION_ID:
                return QuestionAnswerContract.QuestionEntry.CONTENT_ITEM_TYPE;
            case ANSWER_WITH_QUESTION_ID:
                return QuestionAnswerContract.QuestionEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case QUESTION: {

                long _id = db.insertWithOnConflict(QuestionAnswerContract.QuestionEntry.TABLE_NAME, null, values
                        , SQLiteDatabase.CONFLICT_REPLACE);
                if (_id > 0)
                    returnUri = QuestionAnswerContract.QuestionEntry.buildQuestionUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ANSWER: {
                long _id = db.insertWithOnConflict(QuestionAnswerContract.AnswerEntry.TABLE_NAME, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                if (_id > 0)
                    returnUri = QuestionAnswerContract.AnswerEntry.buildAnswersUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;


    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


}
