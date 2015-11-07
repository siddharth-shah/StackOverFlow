package app.stackexchange.siddharthshah.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import app.stackexchange.siddharthshah.myapplication.R;
import app.stackexchange.siddharthshah.myapplication.fragments.AnswersFragment;
import app.stackexchange.siddharthshah.myapplication.fragments.QuestionListFragment;
import app.stackexchange.siddharthshah.myapplication.model.QuestionInfo;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)Toolbar mToolbar;
    private AnswersFragment mAnswersFragment;
    private QuestionListFragment mQuestionListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mQuestionListFragment = QuestionListFragment.newInstance();
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

    public void switchFragment(QuestionInfo questionInfo) {
        Bundle bundle = new Bundle();
        bundle.putString("questionId", questionInfo.getQuestionId());
        bundle.putString("questionTitle", questionInfo.getQuestionTitle());
        bundle.putString("questionBody", questionInfo.getQuestionBody());
        bundle.putString("questionVotes", questionInfo.getTotalVotes());
        bundle.putString("questionOwnerName", questionInfo.getOwnerName());
        mAnswersFragment = new AnswersFragment();
        mAnswersFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragmentlayout, mAnswersFragment).commit();
    }

}
