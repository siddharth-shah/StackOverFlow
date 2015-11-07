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

import app.stackexchange.siddharthshah.myapplication.R;
import app.stackexchange.siddharthshah.myapplication.StackexchangeApplication;
import app.stackexchange.siddharthshah.myapplication.adapters.QuestionsAdapter;
import app.stackexchange.siddharthshah.myapplication.database.QuestionAnswerContract;
import app.stackexchange.siddharthshah.myapplication.http.GsonRequest;
import app.stackexchange.siddharthshah.myapplication.http.UrlBuilderHelper;
import app.stackexchange.siddharthshah.myapplication.model.ApiExtraParams;
import app.stackexchange.siddharthshah.myapplication.model.Question;
import app.stackexchange.siddharthshah.myapplication.model.QuestionList;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

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
    private QuestionsAdapter mQuestionsAdapter;
    private CursorLoader mCursorLoader;
    private Integer questionId;
    private String page = "1";
    private String pageSize = "20";

    @Bind(R.id.search_input)
    EditText mSearchInput;
    @Bind(R.id.search_button)
    Button mSearchButton;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private Cursor mCursor;
    private int LOAD_QUESTIONS = 1;

    public static QuestionListFragment newInstance() {
        QuestionListFragment fragment = new QuestionListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if(b!=null){

            String searchInput  = b.getString("searchInput");
            if(searchInput!=null && !searchInput.isEmpty()){
                this.searchInput = searchInput;
            }

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_list, container, false);
        ButterKnife.bind(this, v);
        mQuestionsAdapter = new QuestionsAdapter(QuestionListFragment.this);
        if(searchInput!=null && !searchInput.isEmpty()){
            mSearchInput.setText(searchInput);
            search();
        } else {
            getLoaderManager().initLoader(LOAD_QUESTIONS, null, this);
        }
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


    private void searchDataAndUpdateDB(final String searchInput) {
        this.searchInput = searchInput;
        String myUrl = UrlBuilderHelper.buildUrlSearchQuestions(searchInput, new
                ApiExtraParams(page, pageSize, sortCriteria, filter, order, siteToSearch));
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

    @OnClick(R.id.search_button)
    public void search() {
        searchInput = mSearchInput.getText().toString();
        if (searchInput.isEmpty()) {
            Toast.makeText(getActivity(), "Enter some text", Toast.LENGTH_SHORT).show();
        } else {
            searchDataAndUpdateDB(searchInput);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}