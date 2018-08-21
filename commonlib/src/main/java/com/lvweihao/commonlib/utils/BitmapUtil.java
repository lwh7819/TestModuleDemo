package com.lvweihao.commonlib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.*;

/**
 * 位图处理Util
 */
public class BitmapUtil {

    /**
     * 位图转字节
     *
     * @param bm Bitmap对象
     * @return 字节数组
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap drawableToBitmap(Drawable drawable){
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    /**
     * 获取位图并转字节
     *
     * @param ctx 上下文
     * @param id  位图ID
     * @return 字节数组
     */
    public static byte[] loadResBitmap2Bytes(Context ctx, int id) {
        Resources res = ctx.getResources();
        Bitmap bm = BitmapFactory.decodeResource(res, id);
        return bitmap2Bytes(bm);
    }

    /**
     * 截取位图
     *
     * @param bitmap Bitmap对象
     * @param scaleX X坐标
     * @param scaleY Y坐标
     * @return 新Bitmap对象
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 将文件转化为字节数组字符串，并对其进行Base64编码处理
     *
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public static String getFileStr(String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();

        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

//    /**
//     * 将BASE64的图片字节流转换成文件
//     *
//     * @param src      字节流
//     * @param fileName 文件名
//     * @throws IOException 异常
//     */
//    public static boolean saveImageFromStr(String src, String fileName) {
//        return saveFileFromStr(src, FileUtil.photoDir().getAbsolutePath() + "/" + fileName);
//    }

    /**
     * 将BASE64的字节流转换成文件
     *
     * @param src      字节流
     * @param fileName 文件名
     * @throws IOException 异常
     */
    public static boolean saveFileFromStr(String src, String fileName) {
        File file;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            file = new File(fileName);
            fos = new FileOutputStream(file);
            byte[] buffer = Base64.decode(src.getBytes(), Base64.DEFAULT);
            bos = new BufferedOutputStream(fos);
            bos.write(buffer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
