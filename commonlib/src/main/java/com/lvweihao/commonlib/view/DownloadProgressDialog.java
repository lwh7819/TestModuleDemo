package com.lvweihao.commonlib.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lvweihao.commonlib.R;

import java.text.NumberFormat;

/**
 * 带有progress的下载进度条dialog
 * Created by lv.weihao on 2016/9/24.
 */
public class DownloadProgressDialog {

    /**
     * Creates a ProgressDialog with a circular, spinning progress
     * bar. This is the default.
     */
    public static final int STYLE_SPINNER = 0;

    /**
     * Creates a ProgressDialog with a horizontal progress bar.
     */
    public static final int STYLE_HORIZONTAL = 1;

    private Dialog mDialog;
    private Context mContext;
    private ProgressBar mProgress;
    private TextView mProgressNumber;
    private TextView mProgressPercent;
    private TextView mProgressMessage;

    private Handler mViewUpdateHandler;
    private int mMax;
    private CharSequence mMessage;
    private boolean mHasStarted;
    private int mProgressVal;

    private String TAG = "DownloadProgressDialog";
    private String mProgressNumberFormat;
    private NumberFormat mProgressPercentFormat;

    public DownloadProgressDialog(Context context) {
        this(context, -1);
        initFormats();
    }

    public DownloadProgressDialog(Context context, int theme) {
//        super(context, theme);
        initFormats();
        mDialog = new Dialog(context, R.style.MaterialDialog);
        mDialog.setContentView(R.layout.common_progress_dialog);
        mDialog.setTitle(null);
        mDialog.setCancelable(false);
        mDialog.setOnCancelListener(null);

        mContext = context;

        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0.2f;

        dialogWindow.getAttributes().gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);

        mProgress = (ProgressBar) dialogWindow.findViewById(R.id.progress);
        mProgressNumber = (TextView) dialogWindow.findViewById(R.id.progress_number);
        mProgressPercent = (TextView) dialogWindow.findViewById(R.id.progress_percent);
        mProgressMessage = (TextView) dialogWindow.findViewById(R.id.progress_message);

        mViewUpdateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = mProgress.getProgress();
            int max = mProgress.getMax();
            double dProgress = (double) progress / (double) (1024 * 1024);
            double dMax = (double) max / (double) (1024 * 1024);
            if (mProgressNumberFormat != null) {
                String format = mProgressNumberFormat;
                mProgressNumber.setText(String.format(format, dProgress, dMax));
            } else {
                mProgressNumber.setText("");
            }
            if (mProgressPercentFormat != null) {
                double percent = (double) progress / (double) max;
                SpannableString tmp = new SpannableString(mProgressPercentFormat.format(percent));
                tmp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                        0, tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mProgressPercent.setText(tmp);
            } else {
                mProgressPercent.setText("");
            }
            }

        };
            onProgressChanged();
            if (mMessage != null) {
                setMessage(mMessage);
            }
            if (mMax > 0) {
                setMax(mMax);
            }
            if (mProgressVal > 0) {
                setProgress(mProgressVal);
            }
    }

    /**
     * 设置下载内容总大小字的显示格式
     */
    private void initFormats() {
        mProgressNumberFormat = "%1.2fM/%2.2fM";
        mProgressPercentFormat = NumberFormat.getPercentInstance();
        mProgressPercentFormat.setMaximumFractionDigits(0);
    }

    private void onProgressChanged() {
        mViewUpdateHandler.sendEmptyMessage(0);

    }

    public void setProgressStyle(int style) {
        //mProgressStyle = style;
    }

    public int getMax() {
        if (mProgress != null) {
            return mProgress.getMax();
        }
        return mMax;
    }

    /**
     * 设置进度条最大值
     *
     * @param max
     */
    public void setMax(int max) {
        if (mProgress != null) {
            mProgress.setMax(max);
            onProgressChanged();
        } else {
            mMax = max;
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        if (mProgress != null) {
            mProgress.setIndeterminate(indeterminate);
        }
//    	else {
//            mIndeterminate = indeterminate;
//        }
    }

    /**
     * 设置进度条进度
     *
     * @param value
     */
    public void setProgress(int value) {
        if (true) {
            mProgress.setProgress(value);
            onProgressChanged();
        } else {
            mProgressVal = value;
        }
    }

    /**
     * 设置dialog标题
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        //super.setMessage(message);
        if (mProgressMessage != null) {
            mProgressMessage.setText(message);
        } else {
            mMessage = message;
        }
    }

    /**
     * 显示dialog
     */
    public void show() {
        mDialog.show();
    }

    /**
     * 隐藏dialog
     */
    public void dismiss() {
        mDialog.dismiss();
    }
}
