package app.stackexchange.siddharthshah.myapplication;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import org.xml.sax.XMLReader;

/**
 * Created by siddharthshah on 28/09/15.
 */
public class CodeTagHandler implements Html.TagHandler {
    Context context = StackexchangeApplication.getInstance().getApplicationContext();

    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("code")) {
            processCode(opening, output);
        }
    }

    private void processCode(boolean opening, Editable output) {
        int len = output.length();
        if (opening) {
            output.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.blue)), len, len, Spannable.SPAN_MARK_MARK);
        } else {
            Object obj = getLast(output, ForegroundColorSpan.class);
            int where = output.getSpanStart(obj);

            output.removeSpan(obj);

            if (where != len) {
                output.setSpan(new ForegroundColorSpan(context.
                        getResources().getColor(R.color.blue)), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            for (int i = objs.length; i > 0; i--) {
                if (text.getSpanFlags(objs[i - 1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i - 1];
                }
            }
            return null;
        }
    }
}
