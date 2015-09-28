package app.stackexchange.siddharthshah.myapplication.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.stackexchange.siddharthshah.myapplication.R;
import app.stackexchange.siddharthshah.myapplication.activities.MainActivity;
import app.stackexchange.siddharthshah.myapplication.fragments.QuestionListFragment;
import app.stackexchange.siddharthshah.myapplication.model.QuestionInfo;

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
            ((ItemViewHolder) viewHolder).getName().setText(mCursor.getString(COL_OWNER_NAME));
            ((ItemViewHolder) viewHolder).getQuestionTitle().setText(mCursor.getString(COL_QUESTION_TITLE));
            ((ItemViewHolder) viewHolder).getTotalUpvotes().setText(mCursor.getString(COL_TOTAL_VOTES));
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

        private TextView name;
        private TextView questionTitle;
        private TextView totalUpvotes;
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
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.owner_name);
            questionTitle = (TextView) itemView.findViewById(R.id.question_title);
            totalUpvotes = (TextView) itemView.findViewById(R.id.total_votes);
        }

        public TextView getName() {
            return name;
        }

        public TextView getQuestionTitle() {
            return questionTitle;
        }

        public TextView getTotalUpvotes() {
            return totalUpvotes;
        }

        @Override
        public void onClick(View view) {
            QuestionInfo questionInfoToPass = new QuestionInfo();

            String ownerName = getName().getText().toString();
            String questionTitle = getQuestionTitle().getText().toString();
            String questionBody = getQuestionBody();
            String questionId = getQuestionId();
            String totalVotes = getTotalUpvotes().getText().toString();
            questionInfoToPass.setOwnerName(ownerName);
            questionInfoToPass.setQuestionId(questionId);
            questionInfoToPass.setQuestionTitle(questionTitle);
            questionInfoToPass.setQuestionBody(questionBody);
            questionInfoToPass.setTotalVotes(totalVotes);
            if (questionListFragment != null) {
                ((MainActivity) questionListFragment.
                        getActivity()).switchFragment(questionInfoToPass);
            }

        }
    }

    public void swapCursor(Cursor cursor) {
        this.mCursor = cursor;
        notifyDataSetChanged();

    }
}
