package app.stackexchange.siddharthshah.myapplication.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import app.stackexchange.siddharthshah.myapplication.IntentFilterConstants;
import app.stackexchange.siddharthshah.myapplication.R;
import app.stackexchange.siddharthshah.myapplication.fragments.AnswersFragment;
import app.stackexchange.siddharthshah.myapplication.fragments.QuestionListFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private AnswersFragment mAnswersFragment;
    private QuestionListFragment mQuestionListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        Bundle b = null;
        if (uri != null) {
            b = handleUriFromBrowser(uri);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mQuestionListFragment = QuestionListFragment.newInstance();
        if (b != null) {
            mQuestionListFragment.setArguments(b);
        }
        getFragmentManager().beginTransaction().replace(R.id.fragmentlayout,
                mQuestionListFragment, "").commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (mAnswersFragment != null && mAnswersFragment.isVisible()) {
                if (mQuestionListFragment == null) {
                    mQuestionListFragment = new QuestionListFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragmentlayout, mQuestionListFragment).commit();
            } else if (mQuestionListFragment != null && mQuestionListFragment.isVisible()) {
                onBackPressed();
            } else {
                onBackPressed();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void switchFragment(Parcelable questionInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("questionInfo", questionInfo);
        mAnswersFragment = new AnswersFragment();
        mAnswersFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragmentlayout, mAnswersFragment).commit();
    }

    private Bundle handleUriFromBrowser(Uri uri) {
        String searchInput = "";
        switch (uri.getPath()) {
            case IntentFilterConstants.TAGGED_QUESTIONS:
                searchInput = uri.getLastPathSegment();
                break;

            case IntentFilterConstants.SEARCH_API:
                searchInput = uri.getLastPathSegment();
                break;
            case IntentFilterConstants.SEARCH_STACKOVERFLOW:
                searchInput = uri.getQueryParameter("q");
                break;
            default:
                searchInput = "";
        }
        Bundle b = new Bundle();
        b.putString("searchInput", searchInput);
        return b;

    }

}
