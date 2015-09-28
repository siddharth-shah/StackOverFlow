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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import app.stackexchange.siddharthshah.myapplication.http.GsonRequest;
import app.stackexchange.siddharthshah.myapplication.R;
import app.stackexchange.siddharthshah.myapplication.StackexchangeApplication;
import app.stackexchange.siddharthshah.myapplication.adapters.QuestionsAdapter;
import app.stackexchange.siddharthshah.myapplication.database.QuestionAnswerContract;
import app.stackexchange.siddharthshah.myapplication.http.Constants;
import app.stackexchange.siddharthshah.myapplication.model.Question;
import app.stackexchange.siddharthshah.myapplication.model.QuestionList;

public class QuestionListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static final String[] QUESTION_COLUMNS = {
            QuestionAnswerContract.QuestionEntry.COL_QUESTION_ID,
            QuestionAnswerContract.QuestionEntry.COL_QUESTION_TITLE,
            QuestionAnswerContract.QuestionEntry.COL_OWNER_NAME,
            QuestionAnswerContract.QuestionEntry.COL_TOTAL_VOTES,
            QuestionAnswerContract.QuestionEntry.COL_QUESTION_BODY,
            QuestionAnswerContract.QuestionEntry.COL_QUESTION_TAG
    };
    String order = "desc";
    String sortCriteria = "votes";
    String searchInput = "android";
    String siteToSearch = "stackoverflow";
    String filter = "!ORaDYJ3okQ(x.OUNeuBwjRqmvMFB4CCvAx8TwqtWoBW";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private QuestionsAdapter mQuestionsAdapter;
    private CursorLoader mCursorLoader;
    private Integer questionId;
    private int page = 1;
    private int pageSize = 20;
    private EditText mSearchInput;
    private Button mSearchButton;
    private Cursor mCursor;
    private int LOAD_QUESTIONS = 1;

    public static QuestionListFragment newInstance() {
        QuestionListFragment fragment = new QuestionListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_list, container, false);
        initSearchView(v);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mQuestionsAdapter = new QuestionsAdapter(QuestionListFragment.this);
        getLoaderManager().initLoader(LOAD_QUESTIONS, null, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mQuestionsAdapter);
        return v;

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        mCursorLoader = new android.content.CursorLoader(getActivity(), QuestionAnswerContract.QuestionEntry.buildQuestionOnTag(searchInput),
                QUESTION_COLUMNS, null, null, null);
        return mCursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.mCursor = cursor;
        mQuestionsAdapter.swapCursor(mCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public String buildUrl() {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.stackexchange.com")
                .appendPath("2.2")
                .appendPath("search")
                .appendQueryParameter(Constants.PAGE, "" + page)
                .appendQueryParameter(Constants.PAGE_SIZE, "" + pageSize)
                .appendQueryParameter(Constants.ORDER, order)
                .appendQueryParameter(Constants.SORT_CRITERIA, sortCriteria)
                .appendQueryParameter(Constants.SEARCH_INPUT, searchInput)
                .appendQueryParameter(Constants.SITE_TO_SEARCH, siteToSearch)
                .appendQueryParameter(Constants.FILTER_CHOSEN, filter);

        return builder.build().toString();

    }

    private void initSearchView(View v) {
        mSearchInput = (EditText) v.findViewById(R.id.search_input);
        mSearchButton = (Button) v.findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(this);
    }


    private void searchDataAndUpdateDB(final String searchInput) {
        this.searchInput = searchInput;
        String myUrl = buildUrl();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Fetching Questions...");
        progressDialog.show();
        GsonRequest<QuestionList> g = new GsonRequest<>(myUrl, QuestionList.class, null, new Response.Listener<QuestionList>() {
            @Override
            public void onResponse(QuestionList response) {
                ArrayList<Question> questions = (ArrayList) response.questionList;
                int size = questions.size();
                for (int i = 0; i < size; i++) {
                    ContentValues contentValues = new ContentValues();
                    Question question = questions.get(i);
                    contentValues.put(QuestionAnswerContract.QuestionEntry.COL_QUESTION_ID,
                            question.questionId);
                    contentValues.put(QuestionAnswerContract.QuestionEntry.COL_QUESTION_TITLE,
                            question.title);
                    contentValues.put(QuestionAnswerContract.QuestionEntry.COL_OWNER_NAME,
                            question.owner.displayName);
                    contentValues.put(QuestionAnswerContract.QuestionEntry.COL_QUESTION_BODY,
                            question.body);
                    int totalVotes = question.upVoteCount + question.downVoteCount;
                    contentValues.put(QuestionAnswerContract.QuestionEntry.COL_TOTAL_VOTES,
                            totalVotes);
                    contentValues.put(QuestionAnswerContract.QuestionEntry.COL_QUESTION_TAG, searchInput);
                    Uri uri = Uri.withAppendedPath(QuestionAnswerContract.BASE_CONTENT_URI,
                            QuestionAnswerContract.PATH_QUESTIONS);
                    getActivity().getContentResolver().insert(uri, contentValues);
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();

                }
                getLoaderManager().restartLoader(LOAD_QUESTIONS, null, QuestionListFragment.this);
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
        });

        RequestQueue requestQueue = StackexchangeApplication.getInstance().getRequestQueue();
        requestQueue.add(g);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.search_button:
                searchInput = mSearchInput.getText().toString();
                if (searchInput.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter some text", Toast.LENGTH_SHORT).show();
                } else {
                    searchDataAndUpdateDB(searchInput);
                }
                break;
        }
    }
}