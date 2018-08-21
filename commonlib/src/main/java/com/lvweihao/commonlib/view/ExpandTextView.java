package com.lvweihao.commonlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lvweihao.commonlib.R;

/**
 * 自定义TextView拓展
 */
public class ExpandTextView extends android.support.v7.widget.AppCompatTextView {
    private boolean mExpand = false;
    private String mExpandText;
    private String mExpandColor = "black";
    private String mExpandPosition = "left";
    private String mContent = "";
    private String mText = "";

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView);
        mExpand = array.getBoolean(R.styleable.ExpandTextView_expand, false);
        mExpandText = array.getString(R.styleable.ExpandTextView_expand_text);
        mExpandColor = array.getString(R.styleable.ExpandTextView_expand_color);
        mExpandPosition = array.getString(R.styleable.ExpandTextView_expand_position);
        array.recycle();

        mText = getText().toString();

        if (mExpand && mExpandText != null) {
            if ("left".equals(mExpandPosition)) {
                mContent = "<font color='" + mExpandColor + "'>" + mExpandText +"</font>" + mText;
            } else {
                mContent = mText + "<font color='" + mExpandColor + "'>" + mExpandText +"</font>";
            }

            setText(Html.fromHtml(mContent));
        }
    }

    /**
     * 移除拓展加进的内容
     */
    public void removeExpand() {
        if (mExpand) {
            setText(mText);
        }
    }
}
