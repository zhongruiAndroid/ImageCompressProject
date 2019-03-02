package com.github.ImageCompress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/***
 *   created by zhongrui on 2019/2/24
 */
public class CompressUtils {
    //region 根据高度压缩
    /*******************************************************************************************************/
    public static Bitmap compressBitmapForHeight(String pathName,int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

          int height = options.outHeight;
//          int width = options.outWidth;
        int inSampleSize = 1;

        int degree = readPictureDegree(pathName);
        //如果图片旋转了90度或者270度，则获取到的宽为实际图片的高
        if(is90And270(degree)){
            height=options.outWidth;
        }
        if (height > reqHeight ) {
            final int heightRatio = (int)Math.floor((float) height / (float) reqHeight);
            inSampleSize = heightRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        if(inSampleSize==1){
            Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
            return bitmapRotate(bitmap,degree);
        }
        return scaleBitmapForHeight(BitmapFactory.decodeFile(pathName, options),reqHeight,degree);
    }
    /*******************************************************************************************************/
    //endregion

    //region 根据宽度压缩
    /*******************************************************************************************************/
    public static Bitmap compressBitmapForWidth(String pathName, int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        int width = options.outWidth;
//      int height = options.outHeight;

        int inSampleSize = 1;
        int degree = readPictureDegree(pathName);
        //如果图片旋转了90度或者270度，则获取到的宽为实际图片的高
        if(is90And270(degree)){
            width=options.outHeight;
        }
        if ( width > reqWidth) {
            int widthRatio = (int)Math.floor((float) width / (float) reqWidth);
            inSampleSize =  widthRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;

        if(inSampleSize==1){
            Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
            return bitmapRotate(bitmap,degree);
        }
        return scaleBitmapForWidth(BitmapFactory.decodeFile(pathName, options),reqWidth,degree);
    }
    /*******************************************************************************************************/
    //endregion


    //region 根据倍数压缩
    /*******************************************************************************************************/
    public static Bitmap compressBitmapForScale(String pathName,int scaleSize) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        if (scaleSize>1) {
            options.inSampleSize = scaleSize;
        }else{
            options.inSampleSize = 1;
        }
        int degree = readPictureDegree(pathName);
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
        if(degree>0){
            Matrix matrix=new Matrix();
            matrix.postRotate(degree);
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            bitmap=null;
            return newBitmap;
        }
        return bitmap;
    }
    /*******************************************************************************************************/
    //endregion

    /*******************************************************************************************************/
    private static Bitmap scaleBitmapForWidth(Bitmap bitmap,int newWidth,int degree){
        int bWidth=bitmap.getWidth();
        int bHeight=bitmap.getHeight();
        if(is90And270(degree)){
            bWidth=bitmap.getHeight();
            bHeight=bitmap.getWidth();
        }
        if(bWidth<=newWidth){
            //如果过度缩小就不放大了
            if(degree>0){
                Matrix matrix=new Matrix();
                matrix.postRotate(degree);
                Bitmap newBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(), bitmap.getHeight(),matrix, true);
                bitmap.recycle();
                bitmap=null;
                return newBitmap;
            }else {
                return bitmap;
            }
        }
        float initScale=newWidth*1.0f/bWidth;
        Matrix matrix=new Matrix();
        matrix.postRotate(degree);
        matrix.postScale(initScale,initScale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(), bitmap.getHeight(),matrix, true);
        bitmap.recycle();
        bitmap=null;
        return newBitmap;
    }
    private static Bitmap scaleBitmapForWidth(Bitmap bitmap,int newWidth){
        return scaleBitmapForWidth(bitmap,newWidth,0);
    }
    private static Bitmap scaleBitmapForHeight(Bitmap bitmap,int newHeight,int degree){
        int bWidth=bitmap.getWidth();
        int bHeight=bitmap.getHeight();
        if(is90And270(degree)){
            bWidth=bitmap.getHeight();
            bHeight=bitmap.getWidth();
        }
        if(bHeight<=newHeight){
            if(degree>0){
                Matrix matrix=new Matrix();
                matrix.postRotate(degree);
                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                bitmap=null;
                return newBitmap;
            }else{
                return bitmap;
            }
        }
        float initScale=newHeight*1.0f/bHeight;
        Matrix matrix=new Matrix();
        matrix.postRotate(degree);
        matrix.postScale(initScale,initScale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        bitmap=null;
        return newBitmap;
    }
    private static Bitmap scaleBitmapForHeight(Bitmap bitmap,int newHeight){
        return scaleBitmapForHeight(bitmap,newHeight,0);
    }
    /*******************************************************************************************************/
    //region  图片旋转
    public static Bitmap bitmapRotate(Bitmap bitmap,int degree){
        if(degree>0){
            Matrix matrix=new Matrix();
            matrix.postRotate(degree);
            Bitmap newBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(), bitmap.getHeight(),matrix, true);
            bitmap.recycle();
            bitmap=null;
            return newBitmap;
        }else {
            return bitmap;
        }
    }

    //endregion
    //region  获取本地图片旋转角度

    /**
     * 读取图片旋转的角度
     *
     * @param filePath
     * @return
     */
    public static int readPictureDegree(String filePath) {
        int rotate = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int result = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (result) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotate;
    }

    //endregion

    //region质量压缩
    public static String compressForQuality(File saveFile,Bitmap bitmap, Bitmap.CompressFormat format, int eachCompressQuality, float maxFileSize){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format,100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length>maxFileSize*1024) { //循环判断如果压缩后图片是否大于maxFileSize kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= eachCompressQuality;//每次都减少10
            if(options<=5){
                options=5;
                //如果options小于0,此时图片压缩之后的大小还没满足设置大小就结束压缩防止异常
                bitmap.compress(format, options, baos);
                break;
            }
            bitmap.compress(format, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(saveFile);
            fileOutputStream.write(baos.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();

            baos.flush();
            baos.close();

            bitmap.recycle();
            bitmap=null;

            return saveFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String compressForQuality(File saveFile,Bitmap bitmap,Bitmap.CompressFormat format, int quality){
        try {
            bitmap.compress(format,quality, new FileOutputStream(saveFile));
            return saveFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static boolean is90And270(int degree){
        if(degree>0&&(degree==90||degree%270==0)){
            return true;
        }
        return false;
    }
    //endregion
}
