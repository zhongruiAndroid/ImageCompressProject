package com.github.ImageCompress;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/***
 *   created by zhongrui on 2019/2/24
 */
public class CompressUtils {
    //region 根据高度压缩
    /*******************************************************************************************************/
    public static Bitmap compressBitmapForHeight(Context context, int resId,  int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);

        final int height = options.outHeight;
//        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight ) {
            final int heightRatio = (int)Math.floor((float) height / (float) reqHeight);
            inSampleSize = heightRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return scaleBitmapForHeight(BitmapFactory.decodeResource(context.getResources(), resId, options),reqHeight);
    }
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
        if(degree==90||degree%270==0){
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
    public static Bitmap compressBitmapForHeight(byte[] data, int offset, int length,int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data,offset,length, options);

        final int height = options.outHeight;
//        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight ) {
            final int heightRatio = (int)Math.floor((float) height / (float) reqHeight);
            inSampleSize = heightRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return scaleBitmapForHeight(BitmapFactory.decodeByteArray(data,offset,length, options),reqHeight);
    }
    public static Bitmap compressBitmapForHeight(FileDescriptor fd, Rect outPadding,int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,outPadding, options);

        final int height = options.outHeight;
//        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight ) {
            final int heightRatio = (int)Math.floor((float) height / (float) reqHeight);
            inSampleSize = heightRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return scaleBitmapForHeight(BitmapFactory.decodeFileDescriptor(fd,outPadding, options),reqHeight);
    }
    public static Bitmap compressBitmapForHeight(Resources res, TypedValue value,InputStream is, Rect pad,int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResourceStream(res,value,is,pad, options);

        final int height = options.outHeight;
//        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight ) {
            final int heightRatio = (int)Math.floor((float) height / (float) reqHeight);
            inSampleSize = heightRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return scaleBitmapForHeight(BitmapFactory.decodeResourceStream(res,value,is,pad, options),reqHeight);
    }
    public static Bitmap compressBitmapForHeight(InputStream is, Rect outPadding,int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,outPadding, options);

        final int height = options.outHeight;
//        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight ) {
            final int heightRatio = (int)Math.floor((float) height / (float) reqHeight);
            inSampleSize = heightRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return scaleBitmapForHeight(BitmapFactory.decodeStream(is,outPadding,  options),reqHeight);
    }
    /*******************************************************************************************************/
    //endregion

    //region 根据宽度压缩
    /*******************************************************************************************************/
    public static Bitmap compressBitmapForWidth(Context context, int resId, int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);

//        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if ( width > reqWidth) {
            final int widthRatio = (int)Math.floor((float) width / (float) reqWidth);
            inSampleSize =  widthRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return scaleBitmapForWidth(BitmapFactory.decodeResource(context.getResources(), resId, options),reqWidth);
    }
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
        if(degree==90||degree%270==0){
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
    public static Bitmap compressBitmapForWidth(byte[] data, int offset, int length, int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data,offset,length, options);

//        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if ( width > reqWidth) {
            final int widthRatio = (int)Math.floor((float) width / (float) reqWidth);
            inSampleSize =  widthRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return scaleBitmapForWidth(BitmapFactory.decodeByteArray(data,offset,length, options),reqWidth);
    }
    public static Bitmap compressBitmapForWidth(FileDescriptor fd, Rect outPadding, int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,outPadding, options);

//        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if ( width > reqWidth) {
            final int widthRatio = (int)Math.floor((float) width / (float) reqWidth);
            inSampleSize =  widthRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return scaleBitmapForWidth(BitmapFactory.decodeFileDescriptor(fd,outPadding, options),reqWidth);
    }
    public static Bitmap compressBitmapForWidth(Resources res, TypedValue value,InputStream is, Rect pad, int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResourceStream(res,value,is,pad, options);

//        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if ( width > reqWidth) {
            final int widthRatio = (int)Math.floor((float) width / (float) reqWidth);
            inSampleSize =  widthRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return scaleBitmapForWidth(BitmapFactory.decodeResourceStream(res,value,is,pad, options),reqWidth);
    }
    public static Bitmap compressBitmapForWidth(InputStream is, Rect outPadding, int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        /*
         * 第一次解析时，inJustDecodeBounds设置为true，
         * 禁止为bitmap分配内存，虽然bitmap返回值为空，但可以获取图片大小
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,outPadding, options);

//        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if ( width > reqWidth) {
            final int widthRatio = (int)Math.floor((float) width / (float) reqWidth);
            inSampleSize =  widthRatio;
        }
        options.inSampleSize = inSampleSize;
        // 使用计算得到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return scaleBitmapForWidth(BitmapFactory.decodeStream(is,outPadding, options),reqWidth);
    }
    /*******************************************************************************************************/
    //endregion


    //region 根据倍数压缩
    /*******************************************************************************************************/
    public static Bitmap compressBitmapForScale(Context context, int resId,int scaleSize) {
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
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }
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
    public static Bitmap compressBitmapForScale(byte[] data, int offset, int length,int scaleSize) {
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
        return BitmapFactory.decodeByteArray(data,offset,length, options);
    }
    public static Bitmap compressBitmapForScale(FileDescriptor fd, Rect outPadding,int scaleSize) {
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
        return BitmapFactory.decodeFileDescriptor(fd,outPadding, options);
    }
    public static Bitmap compressBitmapForScale(Resources res, TypedValue value,InputStream is, Rect pad,int scaleSize) {
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
        return BitmapFactory.decodeResourceStream(res,value,is,pad, options);
    }
    public static Bitmap compressBitmapForScale(InputStream is, Rect outPadding,int scaleSize) {
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
        return BitmapFactory.decodeStream(is,outPadding, options);
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
//        if(bitmap.getWidth()<=newWidth){
//            return bitmap;
//        }
//        float initScale=newWidth*1.0f/bitmap.getWidth();
//        Matrix matrix=new Matrix();
//        matrix.postScale(initScale,initScale);
//
//        int newHeight=(int)(bitmap.getHeight()*initScale);
//        Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
//        Canvas canvas=new Canvas(newBitmap);
//        canvas.drawBitmap(bitmap,matrix,null);
        return scaleBitmapForWidth(bitmap,newWidth,0);
    }
    private static Bitmap scaleBitmapForHeight(Bitmap bitmap,int newHeight,int degree){
        if(bitmap.getHeight()<=newHeight){
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
        float initScale=newHeight*1.0f/bitmap.getHeight();
        Matrix matrix=new Matrix();
        matrix.postRotate(degree);
        matrix.postScale(initScale,initScale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        bitmap=null;
        return newBitmap;
    }
    private static Bitmap scaleBitmapForHeight(Bitmap bitmap,int newHeight){
     /*   if(bitmap.getHeight()<=newHeight){
            return bitmap;
        }
        float initScale=newHeight*1.0f/bitmap.getHeight();
        Matrix matrix=new Matrix();
        matrix.postScale(initScale,initScale);

        int newWidth=(int)(bitmap.getWidth()*initScale);
        Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(newBitmap);
        canvas.drawBitmap(bitmap,matrix,null);*/
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
        if(degree==90||degree%270==0){
            return true;
        }
        return false;
    }
    //endregion
}
