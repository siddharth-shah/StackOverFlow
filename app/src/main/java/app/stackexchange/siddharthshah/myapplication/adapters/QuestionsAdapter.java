package app.stackexchange.siddharthshah.myapplication.adapters;

import android.database.Cursor;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import app.stackexchange.siddharthshah.myapplication.R;
import app.stackexchange.siddharthshah.myapplication.activities.MainActivity;
import app.stackexchange.siddharthshah.myapplication.fragments.QuestionListFragment;
import app.stackexchange.siddharthshah.myapplication.model.QuestionInfo;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by siddharthshah on 27/09/15.
 */
public class QuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor mCursor;
    public static final int COL_QUESTION_ID = 0;
    public static final int COL_QUESTION_TITLE = 1;
    public static final int COL_OWNER_NAME = 2;
    public static final int COL_TOTAL_VOTES = 3;
    public static final int COL_QUESTION_BODY = 4;
    public static final int COL_QUESTION_TAG = 5;
    private QuestionListFragment questionListFragment;

    public QuestionsAdapter(QuestionListFragment questionListFragment) {
        this.questionListFragment = questionListFragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.
                from(viewGroup.getContext()).inflate(R.layout.question_item, viewGroup, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        if (mCursor != null) {
            mCursor.moveToPosition(i);
            ((ItemViewHolder) viewHolder).name.setText(mCursor.getString(COL_OWNER_NAME));
            ((ItemViewHolder) viewHolder).questionTitle.setText(mCursor.getString(COL_QUESTION_TITLE));
            ((ItemViewHolder) viewHolder).totalUpvotes.setText(mCursor.getString(COL_TOTAL_VOTES));
            ((ItemViewHolder) viewHolder).setQuestionBody(mCursor.getString(COL_QUESTION_BODY));
            ((ItemViewHolder) viewHolder).setQuestionId(mCursor.getString(COL_QUESTION_ID));
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.owner_name)
        TextView name;
        @Bind(R.id.question_title)
        TextView questionTitle;
        @Bind(R.id.total_votes)
        TextView totalUpvotes;
        String questionBody;
        String questionId;

        public String getQuestionBody() {
            return questionBody;
        }

        public void setQuestionBody(String questionBody) {
            this.questionBody = questionBody;
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String ownerName = name.getText().toString();
            String questionTitle = this.questionTitle.getText().toString();
            String questionBody = getQuestionBody();
            String questionId = getQuestionId();
            String totalVotes = totalUpvotes.getText().toString();

            QuestionInfo questionInfo =
                    new QuestionInfo(ownerName,questionId,questionTitle,questionBody,totalVotes);
            Parcelable questionParcelable = Parcels.wrap(questionInfo);
            if (questionListFragment != null) {
                ((MainActivity) questionListFragment.
                        getActivity()).switchFragment(questionParcelable);
            }
        }
    }

    public void swapCursor(Cursor cursor) {
        this.mCursor = cursor;
        notifyDataSetChanged();

    }
}
