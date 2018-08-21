package com.lvweihao.commonlib.view;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

/**
 *继承自BaseAdapter的适配器，适用于有多个内容块以及每个内容块都有一个title的组件。
 */
public class BaseSectionAdapter extends BaseAdapter {

   public final static int TYPE_LIST_ITEM = 0;
   public final static int TYPE_SECTION_HEADER = 1;
   public final static int TYPE_SECTION_SEPARATOR = 2;

   public class IndexPath {
      public int section;
      public int row;

      public IndexPath(int section, int row) {
         this.section = section;
         this.row = row;
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;

         IndexPath indexPath = (IndexPath) o;
         return section == indexPath.section && row == indexPath.row;
      }

      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;

         result = (prime * result) + section;
         result = (prime * result) + row;
         return result;
      }
   }

   private Drawable mSectionSeparator = null;
   private int mSeparatorHeight = 0;

   public Drawable getSectionSeparator() {
      return mSectionSeparator;
   }

   public void setSectionSeparator(Drawable separator) {
      if (separator != null) {
         mSeparatorHeight = separator.getIntrinsicHeight();
      } else {
         mSeparatorHeight = 0;
      }

      mSectionSeparator = separator;
      notifyDataSetChanged();
   }

   public int getSeparatorHeight() {
      return mSeparatorHeight;
   }

   public void setSeparatorHeight(int height) {
      mSeparatorHeight = height;
      notifyDataSetChanged();
   }

   @Override
   public int getCount() {
      int total = 0;
      for (int i = 0; i < getNumberOfSections(); i++) {
         total += getNumberOfRowsInSection(i) + 1;
         if (mSectionSeparator != null) {
            total += 1;
         }
      }
      return total;
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public Object getItem(int position) {
      return getItemAtIndexPath(getIndexPath(position));
   }

   @Override
   public int getViewTypeCount() {
      return mSectionSeparator != null ? 3 : 2;
   }

   @Override
   public int getItemViewType(int position) {
      IndexPath indexPath = getIndexPath(position);

      if (indexPath.row == -1) {
         return TYPE_SECTION_HEADER;
      }

      if (indexPath.row == -2) {
         return TYPE_SECTION_SEPARATOR;
      }

      return TYPE_LIST_ITEM;
   }

   @Override
   public boolean areAllItemsEnabled() {
      return false;
   }

   @Override
   public boolean isEnabled(int position) {
      IndexPath indexPath = getIndexPath(position);

      if (indexPath.row == -1) {
         return isSectionEnabled(indexPath.section);
      } else if (indexPath.row == -2) {
         return false;
      }

      return isEnabled(indexPath);
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      IndexPath indexPath = getIndexPath(position);

      if (indexPath.row == -1) {
         return getViewForHeaderInSection(indexPath.section, convertView, parent);
      }

      if (indexPath.row == -2) {
         if (convertView == null) {
            convertView = new View(parent.getContext());
            convertView.setBackgroundDrawable(mSectionSeparator);
            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mSeparatorHeight));
         }

         return convertView;
      }

      return getViewForRowAtIndexPath(indexPath, convertView, parent);
   }

   public IndexPath getIndexPath(int position) {
      int section = 0;
      while (section < getNumberOfSections()) {
         int size = getNumberOfRowsInSection(section) + 1;
         if (mSectionSeparator != null) {
            size += 1;
         }

         if (mSectionSeparator != null && position == size - 1) {
            position = -1;
            break;
         } else if (position >= size) {
            position -= size;
         } else {
            break;
         }

         section++;
      }
      return new IndexPath(section, position - 1);
   }

   // {{{{ customize

   public int getNumberOfSections() {
      return 0;
   }

   public int getNumberOfRowsInSection(int section) {
      return 0;
   }

   public View getViewForHeaderInSection(int section, View convertView, ViewGroup parent) {
      return null;
   }

   public View getViewForRowAtIndexPath(IndexPath indexPath, View convertView, ViewGroup parent) {
      return null;
   }

   public Object getItemAtIndexPath(IndexPath indexPath) {
      return null;
   }

   public boolean isSectionEnabled(int section) {
      return false;
   }

   public boolean isEnabled(IndexPath indexPath) {
      return true;
   }

   // }}}}
}
