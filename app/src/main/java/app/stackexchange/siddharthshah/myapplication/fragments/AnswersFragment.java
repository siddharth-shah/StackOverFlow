package app.stackexchange.siddharthshah.myapplication.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.parceler.Parcels;
import java.util.ArrayList;

import app.stackexchange.siddharthshah.myapplication.CodeTagHandler;
import app.stackexchange.siddharthshah.myapplication.http.GsonRequest;
import app.stackexchange.siddharthshah.myapplication.R;
import app.stackexchange.siddharthshah.myapplication.StackexchangeApplication;
import app.stackexchange.siddharthshah.myapplication.adapters.AnswersAdapter;
import app.stackexchange.siddharthshah.myapplication.database.QuestionAnswerContract;
import app.stackexchange.siddharthshah.myapplication.http.Constants;
import app.stackexchange.siddharthshah.myapplication.model.Answer;
import app.stackexchange.siddharthshah.myapplication.model.AnswerList;
import app.stackexchange.siddharthshah.myapplication.model.QuestionInfo;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by siddharthshah on 27/09/15.
 */
public class AnswersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String[] ANSWERS_COLUMNS = {
            QuestionAnswerContract.AnswerEntry.COL_QUESTION_ID,
            QuestionAnswerContract.AnswerEntry.COL_ANSWER_ID,
            QuestionAnswerContract.AnswerEntry.COL_ANSWER_BODY,
            QuestionAnswerContract.AnswerEntry.COL_TOTAL_VOTES,
            QuestionAnswerContract.AnswerEntry.COL_ANSWER_OWNER
    };
    String order = "desc";
    String sortCriteria = "votes";
    String siteToSearch = "stackoverflow";
    String filter = "!SV_QVU8i95kH7E1kXu";
    String questionBodyText;
    String questionTitleText;
    String questionOwner;
    String questionVotes;

    @Bind(R.id.recyclerView)RecyclerView mRecyclerView;
    @Bind(R.id.owner_name)TextView ownerName;
    @Bind(R.id.question_title) TextView questionTitle;
    @Bind(R.id.question_body) TextView questionBody;
    private AnswersAdapter mAnswersAdapter;
    private CursorLoader mCursorLoader;
    private int LOAD_ANSWERS = 1;
    private int page = 1;
    private int pageSize = 10;
    private String questionId;
    private Cursor mCursor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Parcelable questionInfoParcelable = (Parcelable)bundle.getParcelable("questionInfo");
            QuestionInfo questionInfo = (QuestionInfo) Parcels.unwrap(questionInfoParcelable);

            questionTitleText = questionInfo.getQuestionTitle();
            questionBodyText = questionInfo.getQuestionBody();
            questionVotes = questionInfo.getTotalVotes();
            questionOwner = questionInfo.getOwnerName();
            questionId = questionInfo.getQuestionId();
            fetchAnswersForQuestionId(questionId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_answer_list, container, false);
        ButterKnife.bind(this,v);
        initQuestionInfo();
        mAnswersAdapter = new AnswersAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAnswersAdapter);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        mCursorLoader = new android.content.CursorLoader(getActivity(), QuestionAnswerContract.
                AnswerEntry.buildAnswersOnQuestionId(questionId),
                ANSWERS_COLUMNS, null, null, null);
        return mCursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.mCursor = cursor;
        mAnswersAdapter.swapCursor(mCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void initQuestionInfo() {
        ownerName.setText(questionOwner);
        questionBody.setText(Html.fromHtml(questionBodyText, null, new CodeTagHandler()));
        questionTitle.setText(questionTitleText);
    }

    private String buildUrl(String questionId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.stackexchange.com")
                .appendPath("2.2")
                .appendPath("questions")
                .appendPath(questionId)
                .appendPath("answers")
                .appendQueryParameter(Constants.PAGE, "" + page)
                .appendQueryParameter(Constants.PAGE_SIZE, "" + pageSize)
                .appendQueryParameter(Constants.ORDER, order)
                .appendQueryParameter(Constants.SORT_CRITERIA, sortCriteria)
                .appendQueryParameter(Constants.SITE_TO_SEARCH, siteToSearch)
                .appendQueryParameter(Constants.FILTER_CHOSEN, filter);

        return builder.build().toString();

    }

    private void fetchAnswersForQuestionId(String questionId) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Fetching Answers...");
        progressDialog.show();

        GsonRequest<AnswerList> g = new GsonRequest<AnswerList>(buildUrl(questionId),
                AnswerList.class, null, new Response.Listener<AnswerList>() {

            @Override
            public void onResponse(AnswerList response) {
                ArrayList<Answer> answerList = (ArrayList) response.answerList;
                int size = answerList.size();
                for (int i = 0; i < size; i++) {
                    if (answerList.get(i).answerId != null) {
                        ContentValues contentValues = new ContentValues();
                        Answer answer = answerList.get(i);
                        contentValues.put(QuestionAnswerContract.AnswerEntry.COL_QUESTION_ID,
                                answer.questionId);
                        contentValues.put(QuestionAnswerContract.AnswerEntry.COL_ANSWER_ID,
                                answer.answerId);
                        contentValues.put(QuestionAnswerContract.AnswerEntry.COL_ANSWER_OWNER,
                                answer.owner.displayName);

                        contentValues.put(QuestionAnswerContract.AnswerEntry.COL_ANSWER_BODY,
                                answer.body);
                        contentValues.put(QuestionAnswerContract.AnswerEntry.COL_TOTAL_VOTES,
                                0);
                        Uri uri = Uri.withAppendedPath(QuestionAnswerContract.BASE_CONTENT_URI,
                                QuestionAnswerContract.PATH_ANSWERS);
                        getActivity().getContentResolver().insert(uri, contentValues);
                    }
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                getLoaderManager().initLoader(LOAD_ANSWERS, null, AnswersFragment.this);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (error instanceof TimeoutError || error instanceof NoConnectionError)
                    Toast.makeText(getActivity(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

            }
        }
        );

        RequestQueue requestQueue = StackexchangeApplication.getInstance().getRequestQueue();
        requestQueue.add(g);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
