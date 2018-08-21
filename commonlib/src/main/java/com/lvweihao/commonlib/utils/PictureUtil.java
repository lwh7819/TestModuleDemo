package com.lvweihao.commonlib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import com.facebook.stetho.common.LogUtil;
import com.lvweihao.commonlib.BuildConfig;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.functions.Consumer;

/**
 * 拍照图库
 * Created by tang.wangqiang on 2018/5/29.
 */

public class PictureUtil {

    public static final String APP_NAME = "Hz_Yf";
    public static final String CAMERA_PATH = "/" + APP_NAME + "/CameraImage/";
    public static final String POSTFIX = ".JPEG";
    private Activity activity;
    private File cameraFile;
    private File imageFile;
    Uri imageUri, cameraUri;
    private File folderDir;

    public final static int REQUEST_IMAGE_PHOTO = 0x01;
    public final static int REQUEST_IMAGE_CAMERA = 0x02;
    public final static int REQUEST_IMAGE_CUT_CAMERA = 0x03;

    public PictureUtil(Activity activity) {
        this.activity = activity;
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : activity.getCacheDir();
        folderDir = new File(rootDir.getAbsolutePath() + CAMERA_PATH);
        try {
            if (!folderDir.exists()) {
                folderDir.mkdirs();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = APP_NAME + "_" + timeStamp + "";
            cameraFile = new File(folderDir, fileName + "camera" + POSTFIX);
            imageFile = new File(folderDir, fileName + "image" + POSTFIX);
            if (cameraFile.exists()) {
                cameraFile.delete();
            }
            if (imageFile.exists()) {
                imageFile.delete();
            }
            cameraFile.createNewFile();
            imageFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void delitePicture() {
        delAllFile(folderDir.getAbsolutePath());
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除文件夹下所有内容
     *
     * @param folderPath（绝对路径）
     */
    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 选择相机
     */
    public void ChooseCamera() {
        if (hasCamera()) {
            if (hasPermission(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                takeCamera();
            } else {
                RxPermissions rxPermissions = new RxPermissions(activity);
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    takeCamera();
                                } else {
                                    PopUtil.showToast(activity, "请在系统设置中为App开启权限后重试");
                                }
                            }
                        });
            }
        } else {
            PopUtil.showToast(activity, "没有可用相机");
        }

    }

    public void ChooseImage() {
        if (hasSdcard()) {
            if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                choosePicture();
            } else {
                RxPermissions rxPermissions = new RxPermissions(activity);
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    choosePicture();
                                } else {
                                    PopUtil.showToast(activity, "请在系统设置中为App开启权限后重试");
                                }
                            }
                        });
            }
        } else {
            PopUtil.showToast(activity, "没有SD卡");
        }
    }

    /**
     * 打开图库
     */
    private void choosePicture() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(pickIntent, REQUEST_IMAGE_PHOTO);
    }

    /**
     * 相机
     */
    private void takeCamera() {
        //拍照
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            String authority = BuildConfig.APPLICATION_ID + ".fileprovider";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                cameraUri = FileProvider.getUriForFile(activity, authority, cameraFile);//通过FileProvider创建一个content类型的Uri
            } else {
                cameraUri = Uri.fromFile(cameraFile);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            activity.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, ChooseListener chooseListener) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAMERA:
                    imageUri = Uri.fromFile(imageFile);
                    ActionUtils.startActivityForImageCut(activity, REQUEST_IMAGE_CUT_CAMERA, cameraUri, imageUri, 480, 480);
                    break;
                case REQUEST_IMAGE_PHOTO:
                    if (data != null) {
                        imageUri = Uri.fromFile(imageFile);
                        Uri briefUri = Uri.parse(ActionUtils.getPath(activity, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            String authority = BuildConfig.APPLICATION_ID + ".fileprovider";
                            briefUri = FileProvider.getUriForFile(activity, authority, new File(briefUri.getPath()));
                        }
                        ActionUtils.startActivityForImageCut(activity, REQUEST_IMAGE_CUT_CAMERA, briefUri, imageUri, 480, 480);
                    }
                    break;
                case REQUEST_IMAGE_CUT_CAMERA:
                    if (chooseListener != null && imageUri != null) {
                        chooseListener.choose(imageUri, imageFile);
                        Log.e("TAG", imageFile.getAbsolutePath());
                    }
                    break;
            }
        }
    }

    /**
     * 质量压缩
     *
     * @param
     * @return
     */
    public Bitmap compressImage(Bitmap bitmap, String filePath) {//只会保存到文件大小变化，内存中大小不变
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            LogUtil.e("=========>" + options + "========" + baos.toByteArray().length / 1024);
        }
        try {
            baos.writeTo(new FileOutputStream(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 在这里已经压缩到100以下了，但是只要调用了decodeStream就又会涨到200K,所以在操作之前把图片先保存到本地
        // 如果需要返回一个bitmap对象，则调用如下方法
        // ByteArrayInputStream isBm = new
        // ByteArrayInputStream(baos.toByteArray());
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        // Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        // 把ByteArrayInputStream数据生成图片
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmapnew = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
//        image.recycle();
        return bitmapnew;
    }

    /**
     * 判断是否有权限
     *
     * @return
     */
    public boolean hasPermission(String... permission) {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                for (String permissions : permission) {
                    result = ContextCompat.checkSelfPermission(activity, permissions) == PackageManager.PERMISSION_GRANTED;
                    if (!result) {
                        break;
                    }
                }
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                for (String permissions : permission) {
                    result = PermissionChecker.checkSelfPermission(activity, permissions) == PackageManager.PERMISSION_GRANTED;
                    if (!result) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 判断是否有可用相机
     *
     * @return
     */
    private boolean hasCamera() {
        PackageManager pm = activity.getPackageManager();
        // FEATURE_CAMERA - 后置相机
        // FEATURE_CAMERA_FRONT - 前置相机
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                && !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Log.i("camera", "non-support");
            return false;
        } else {
            Log.i("camera", "support");
            return true;
        }
    }

    public interface ChooseListener {
        void choose(Uri uri, File path);
    }
}
