package com.lvweihao.commonlib.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lvweihao.commonlib.R;

/**
 * MaterialDesign风格的Dialog
 */
public class MaterialDialog {
   private Dialog mDialog;
   private Context mContext;

   private TextView mTitleView;
   private TextView mMessageView;
   private FrameLayout mContentLayout;
   private Button mPositiveButton;
   private Button mNegativeButton;
   private View mView;

   public MaterialDialog(Context context) {
      mDialog = new Dialog(context, R.style.MaterialDialog);
      mDialog.setContentView(R.layout.material_dialog);
      mDialog.setTitle(null);
      mDialog.setCancelable(false);
      mDialog.setOnCancelListener(null);

      mContext = context;

      Window dialogWindow = mDialog.getWindow();
      WindowManager.LayoutParams lp = dialogWindow.getAttributes();
      lp.dimAmount = 0.2f;

      dialogWindow.getAttributes().gravity = Gravity.CENTER;
      dialogWindow.setAttributes(lp);

      mTitleView = (TextView) dialogWindow.findViewById(R.id.title);
      mMessageView = (TextView) dialogWindow.findViewById(R.id.message);
      mContentLayout = (FrameLayout) dialogWindow.findViewById(R.id.content_layout);
      mPositiveButton = (Button) dialogWindow.findViewById(R.id.positive_btn);
      mNegativeButton = (Button) dialogWindow.findViewById(R.id.negative_btn);
      mView = dialogWindow.findViewById(R.id.view);
   }

   public MaterialDialog setTitle(int resId) {
      mTitleView.setText(resId);
      return this;
   }

   public MaterialDialog setTitle(CharSequence title) {
      mTitleView.setText(title);
      return this;
   }

   public MaterialDialog setMessage(int resId) {
      mMessageView.setText(resId);
      if (mMessageView.getText().length() == 0) {
         mMessageView.setVisibility(View.GONE);
      } else {
         mMessageView.setVisibility(View.VISIBLE);
      }
      return this;
   }

   public MaterialDialog setMessage(CharSequence message) {
      mMessageView.setText(message);
      if (mMessageView.getText().length() == 0) {
         mMessageView.setVisibility(View.GONE);
      } else {
         mMessageView.setVisibility(View.VISIBLE);
      }
      return this;
   }

   public MaterialDialog setPositiveButton(int resId, final View.OnClickListener listener) {
      mPositiveButton.setText(resId);
      mPositiveButton.setOnClickListener(listener);

      if (listener != null) {
         mPositiveButton.setVisibility(View.VISIBLE);
      } else {
         mPositiveButton.setVisibility(View.GONE);
      }

      return this;
   }

   public MaterialDialog setPositiveButton(String text, final View.OnClickListener listener) {
      mPositiveButton.setText(text);
      mPositiveButton.setOnClickListener(listener);

      if (listener != null) {
         mPositiveButton.setVisibility(View.VISIBLE);
      } else {
         mPositiveButton.setVisibility(View.GONE);
      }

      return this;
   }

   public MaterialDialog setNegativeButton(int resId, final View.OnClickListener listener) {
      mNegativeButton.setText(resId);
      mNegativeButton.setOnClickListener(listener);

      if (listener != null) {
         mNegativeButton.setVisibility(View.VISIBLE);
         mView.setVisibility(View.VISIBLE);
      } else {
         mNegativeButton.setVisibility(View.GONE);
         mView.setVisibility(View.GONE);
      }

      return this;
   }

   public MaterialDialog setNegativeButton(String text, final View.OnClickListener listener) {
      mNegativeButton.setText(text);
      mNegativeButton.setOnClickListener(listener);

      if (listener != null) {
         mNegativeButton.setVisibility(View.VISIBLE);
         mView.setVisibility(View.VISIBLE);
      } else {
         mNegativeButton.setVisibility(View.GONE);
         mView.setVisibility(View.GONE);
      }

      return this;
   }

   public MaterialDialog setContentView(View view) {
      mContentLayout.removeAllViews();

      if (view == null) {
         mContentLayout.setVisibility(View.GONE);
      } else {
         mContentLayout.setVisibility(View.VISIBLE);
         FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
         layoutParams.leftMargin =  mContext.getResources().getDimensionPixelOffset(R.dimen.server_edit_vertical_margin);
         layoutParams.rightMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.server_edit_vertical_margin);
         view.setLayoutParams(layoutParams);
         mContentLayout.addView(view);
      }

      return this;
   }

   public void show() {
      mDialog.show();
   }

   public void dismiss() {
      mDialog.dismiss();
   }
}