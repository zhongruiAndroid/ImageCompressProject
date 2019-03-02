package com.github.ImageCompress;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 *   created by zhongrui on 2019/2/24
 */
public class MyMedia {
    //region   file转Uri(适用外置储存卡位置)

    /***
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri fileToUriForExternal(Context context,File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            //小于7.0
            uri = Uri.fromFile(file);
        } else {
            //大于等于7.0
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());

            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        return uri;
    }
    public static Uri fileToUriForExternal(Context context, String folderPath, String fileName) {
        File file = new File(folderPath);
        if (file.exists() == false) {
            file.mkdirs();
        }
        String savePath = folderPath + "/" + fileName;
        return fileToUriForExternal(context,new File(savePath));
    }
    //endregion

    //region 获取拍照intent
    public static Intent getTakePhotoIntentForExternal(Context context, String folderPath, String fileName) {
        return getTakePhotoIntent(fileToUriForExternal(context,folderPath,fileName));
    }

    public static Intent getTakePhotoIntent(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    public static Uri fileToUri(Context context,String authority, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            //小于7.0
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, authority, file);
        }
        return uri;
    }
    public static Uri fileToUri(Context context, File file) {
        return fileToUri(context, context.getPackageName(), file);
    }
    public static Intent getTakePhotoIntent(Context context,String authority, String folderPath, String fileName) {
        File file = new File(folderPath);
        if (file.exists() == false) {
            file.mkdirs();
        }
        String savePath = folderPath + "/" + fileName;
        File photoFile = new File(savePath);
        return getTakePhotoIntent(fileToUri(context,authority,photoFile));
    }
    public static Intent getTakePhotoIntent(Context context, String folderPath, String fileName) {
        return getTakePhotoIntent(context,context.getPackageName(),folderPath,fileName);
    }
    //endregion

    //region   获取手机拍照的默认路径
    public static File getCameraDefaultFile() {
        String DCIM = Environment.getExternalStorageDirectory() +
                File.separator + Environment.DIRECTORY_DCIM + File.separator;
        File file=new File(DCIM+"/"+"Camera");
        if(file.exists()==false){
            file.mkdirs();
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName="IMG_" + dateFormat.format(date)+".jpg";
        return new File(file,fileName);
    }
    //endregion

    //region 获取选择相册intent
    public static Intent getOpenPhotoAlbumIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }
    //endregion

    //region 根据相册返回的data获取图片路径
    public static String getPhotoPath(Context context, Uri uri){
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if(cursor!=null&&cursor.moveToFirst()){
            String photoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            return photoPath;
        }
        return null;
    }
    public static String getPhotoPath(Context context, Intent data){
        if (data == null) {
            return null;
        }
        Uri uri = data.getData();
        if(uri==null){
            return null;
        }
        return getPhotoPath(context,uri);
    }
    //endregion
}
