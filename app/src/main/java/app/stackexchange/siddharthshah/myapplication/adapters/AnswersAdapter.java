package app.stackexchange.siddharthshah.myapplication.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.stackexchange.siddharthshah.myapplication.CodeTagHandler;
import app.stackexchange.siddharthshah.myapplication.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by siddharthshah on 27/09/15.
 */
public class AnswersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Cursor mCursor;

    public static final int COL_QUESTION_ID = 0;
    public static final int COL_ANSWER_ID = 1;
    public static final int COL_ANSWER_BODY = 2;
    public static final int COL_TOTAL_VOTES = 3;
    public static final int COL_ANSWER_OWNER = 4;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.
                from(viewGroup.getContext()).inflate(R.layout.answers_item, viewGroup, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        if (mCursor != null) {
            mCursor.moveToPosition(i);
            ((ItemViewHolder) viewHolder).ownerName.setText(mCursor.getString(COL_ANSWER_OWNER));
            ((ItemViewHolder) viewHolder).answerBody.setText(Html.fromHtml(mCursor.getString(COL_ANSWER_BODY),null,new CodeTagHandler()));
        }

    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;

        return mCursor.getCount();

    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.owner_name) TextView ownerName;
        @Bind(R.id.answer_body) TextView answerBody;

        //        private TextView answerVotes;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }


    public void swapCursor(Cursor cursor) {
        this.mCursor = cursor;
        notifyDataSetChanged();

    }
}
