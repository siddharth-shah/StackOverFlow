package app.stackexchange.siddharthshah.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by siddharthshah on 28/09/15.
 */
public class QuestionAnswerDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "questionanswer.db";
    private static final int DATABASE_VERSION = 1;

    public QuestionAnswerDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_QUESTION_TABLE = "CREATE TABLE " + QuestionAnswerContract.QuestionEntry.TABLE_NAME + " (" +
                QuestionAnswerContract.QuestionEntry.COL_QUESTION_ID + " INTEGER PRIMARY KEY," +
                QuestionAnswerContract.QuestionEntry.COL_QUESTION_TITLE + " TEXT, " +
                QuestionAnswerContract.QuestionEntry.COL_OWNER_NAME + " TEXT, " +
                QuestionAnswerContract.QuestionEntry.COL_QUESTION_BODY + " TEXT, " +
                QuestionAnswerContract.QuestionEntry.COL_TOTAL_VOTES + " INTEGER, " +
                QuestionAnswerContract.QuestionEntry.COL_QUESTION_TAG + " TEXT " +
                "  );";

        final String SQL_CREATE_ANSWER_TABLE = "CREATE TABLE " + QuestionAnswerContract.AnswerEntry.TABLE_NAME + " (" +
                QuestionAnswerContract.AnswerEntry.COL_ANSWER_ID + " INTEGER PRIMARY KEY," +
                QuestionAnswerContract.AnswerEntry.COL_ANSWER_BODY + " TEXT, " +
                QuestionAnswerContract.AnswerEntry.COL_ANSWER_OWNER + " TEXT, " +
                QuestionAnswerContract.AnswerEntry.COL_QUESTION_ID + " INTEGER, " +
                QuestionAnswerContract.AnswerEntry.COL_TOTAL_VOTES + " INTEGER " + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_QUESTION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ANSWER_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
