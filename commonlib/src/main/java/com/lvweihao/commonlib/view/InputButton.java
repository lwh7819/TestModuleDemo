package com.lvweihao.commonlib.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Button;
import com.alibaba.fastjson.JSON;

public class InputButton extends android.support.v7.widget.AppCompatButton {

   private DialogSelectionListener mListener = new DialogSelectionListener() {

      @Override
      public void OnSelectItem(String name, String code) {
         setText(name);
         setInputValue(code);;
      }

   };

   public DialogSelectionListener getListener() {
      return mListener;
   }

   protected Object mInputValue = null;

   public InputButton(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public Object getInputValue() {
      return mInputValue;
   }

   public void setInputValue(Object value) {
      mInputValue = value;
   }

   public static class SavedState extends BaseSavedState {
      CharSequence text;
      Object inputValue;

      public SavedState(Parcelable superState) {
         super(superState);
      }

      @Override
      public void writeToParcel(Parcel out, int flags) {
         super.writeToParcel(out, flags);

         out.writeString(text.toString());
         out.writeString(JSON.toJSONString(inputValue));
      }

      public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
         public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
         }

         public SavedState[] newArray(int size) {
            return new SavedState[size];
         }
      };

      private SavedState(Parcel source) {
         super(source);

         text = source.readString();
         inputValue = JSON.parseObject(source.readString(), SavedState.class);
      }
   }

   @Override
   public Parcelable onSaveInstanceState() {
      SavedState ss = new SavedState(super.onSaveInstanceState());

      ss.text = getText();
      ss.inputValue = getInputValue();

      return ss;
   }

   @Override
   public void onRestoreInstanceState(Parcelable state) {
      if (!(state instanceof SavedState)) {
         super.onRestoreInstanceState(state);
         return;
      }

      SavedState ss = (SavedState) state;
      super.onRestoreInstanceState(ss.getSuperState());

      setText(ss.text);
      setInputValue(ss.inputValue);
   }
}
