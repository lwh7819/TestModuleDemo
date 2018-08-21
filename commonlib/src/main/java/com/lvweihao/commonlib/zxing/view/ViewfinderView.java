/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lvweihao.commonlib.zxing.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.google.zxing.ResultPoint;
import com.lvweihao.commonlib.R;
import com.lvweihao.commonlib.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;


/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;

    private final Paint paint;
    private final int maskColor;
    private final int resultColor;
    private final int frameColor;
    private final int laserColor;
    private final int resultPointColor;
    private Bitmap resultBitmap;
    private int scannerAlpha;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    private float translateY = 5f;
    private int cameraPermission = PackageManager.PERMISSION_DENIED;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        frameColor = resources.getColor(R.color.viewfinder_frame);
        laserColor = resources.getColor(R.color.viewfinder_laser);
        resultPointColor = resources.getColor(R.color.possible_result_points);
        scannerAlpha = 0;
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            cameraPermission = CameraManager.get().checkCamesraPermission();
        }

        Rect frame = CameraManager.get().getFramingRect();;
        if (frame == null) {
            // Android Studio中预览时和未获得相机权限时都为null
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            int width = 675;
            int height = 675;
            int leftOffset = (screenWidth - width) / 2;
            int topOffset = (screenHeight - height) / 2;
            frame = new Rect(leftOffset, topOffset - 120, leftOffset + width, topOffset + height - 120);
//            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        drawText(canvas, frame);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {

            // Draw a two pixel solid black border inside the framing rect
//            paint.setColor(frameColor);
            canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
            canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
            canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
            canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);

            drawAngle(canvas, frame);

//            // Draw a red "laser scanner" line through the middle to show decoding is active
//            paint.setColor(laserColor);
//            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
//            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//            int middle = frame.height() / 2 + frame.top;
//            canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);

            // Draw a red "laser scanner" line through the middle to show decoding is active
            paint.setColor(Color.parseColor("#03A9F4"));
//            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            canvas.translate(0, translateY);
            canvas.drawRect(frame.left + 10, frame.top, frame.right - 10, frame.top + 10, paint);

            translateY += 5f;
            if (translateY >= 670) {
                translateY = 5f;
            }

//            // Draw a yellow "possible points"
//            Collection<ResultPoint> currentPossible = possibleResultPoints;
//            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
//            if (currentPossible.isEmpty()) {
//                lastPossibleResultPoints = null;
//            } else {
//                possibleResultPoints = new HashSet<ResultPoint>(5);
//                lastPossibleResultPoints = currentPossible;
//                paint.setAlpha(OPAQUE);
//                paint.setColor(resultPointColor);
//                for (ResultPoint point : currentPossible) {
//                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
//                }
//            }
//            if (currentLast != null) {
//                paint.setAlpha(OPAQUE / 2);
//                paint.setColor(resultPointColor);
//                for (ResultPoint point : currentLast) {
//                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
//                }
//            }

            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

    private void drawAngle(Canvas canvas, Rect frame) {
        int angleLength = 50;
        int angleWidth = 10;
        int top = frame.top;
        int bottom = frame.bottom;
        int left = frame.left;
        int right = frame.right;

        paint.setColor(Color.WHITE);
        // 左上
        canvas.drawRect(left - angleWidth, top - angleWidth, left + angleLength, top, paint);
        canvas.drawRect(left - angleWidth, top - angleWidth, left, top + angleLength, paint);
        // 左下
        canvas.drawRect(left - angleWidth, bottom, left + angleLength, bottom + angleWidth, paint);
        canvas.drawRect(left - angleWidth, bottom - angleLength, left, bottom + angleWidth, paint);
        // 右上
        canvas.drawRect(right - angleLength, top - angleWidth, right + angleWidth, top, paint);
        canvas.drawRect(right, top - angleWidth, right + angleWidth, top + angleLength, paint);
        // 右下
        canvas.drawRect(right - angleLength, bottom, right, bottom + angleWidth, paint);
        canvas.drawRect(right, bottom - angleLength, right + angleWidth, bottom + angleWidth, paint);
    }

    private void drawText(Canvas canvas, Rect frame) {
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            paint.setColor(Color.GRAY);
            paint.setTextSize(36);
            String text = "将二维码/条形码置于框内即自动扫描";
            canvas.drawText(text, frame.centerX() - text.length() * 36 / 2, frame.bottom + 35 + 20, paint);
        } else {
            paint.setColor(Color.WHITE);
            paint.setTextSize(36);
            String text = "请允许访问摄像头后重试";
            canvas.drawText(text, frame.centerX() - text.length() * 36 / 2, frame.bottom + 35 + 20, paint);
        }
    }
}
