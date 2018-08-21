package com.lvweihao.commonlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.lvweihao.commonlib.R;
import com.lvweihao.commonlib.utils.BitmapUtil;
import com.lvweihao.commonlib.utils.QrUtils;

/**
 * 二维码图片
 */
public class QRCodeView extends AppCompatImageView {

    private Bitmap bitmap;

    public QRCodeView(Context context) {
        super(context);
    }

    public QRCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getQRCodeBitmap(context, attrs);
    }

    public void getQRCodeBitmap(Context context, AttributeSet attrs) {
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.QRCodeView);

        String content = t.getString(R.styleable.QRCodeView_content);
        float width = t.getDimension(R.styleable.QRCodeView_widthDP, 320);
        float height = t.getDimension(R.styleable.QRCodeView_heightDP, 320);
        Bitmap logo = BitmapUtil.drawableToBitmap(t.getDrawable(R.styleable.QRCodeView_logo));

        bitmap =  QrUtils.createQRBitmap(context, content, width, height, logo);

        setImageBitmap(bitmap);
    }

    public void recyleBitmap() {
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

}
