package com.github.ImageCompress;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/***
 *   created by zhongrui on 2019/2/24
 */
public class CompressConfig {
    public Bitmap.CompressFormat format=Bitmap.CompressFormat.JPEG;
    //0：像素+质量压缩  1：像素压缩  2：质量压缩

    public final static int type_pixel_quality=0;
    public final static int type_pixel=1;
    public final static int type_quality=2;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({type_pixel_quality,type_pixel,type_quality})
    public @interface CompressType{};
    public int compressType=type_pixel_quality;
    /**图片质量默认压缩至原图片的60%*/
    public int quality=60;
    /** 图片压缩之后的文件大小*/
    public float maxFileSize=0;
    /** 每次压缩原图的百分比*/
    public int eachCompressQuality=5;
    /** 压缩之后的宽度默认不超过1080像素*/
    public int maxWidthPixel=1080;
    /** 压缩之后的高度默认不超过1920像素*/
    public int maxHeightPixel=1920;
    /**质量压缩：默认低于200kb的图片不压缩*/
    public float ignoreFileSize=200;

    /**像素压缩：宽和高不超过281和500像素的图片不压缩*/
//    public int ignoreWidthPixel=281;
//    public int ignoreHeightPixel=500;

    /**缓存目录,默认路径/sd卡/Android/data/包名/cache/compress/*/
    public String cacheDir;
//    /**是否保留原始图片*/
//    public boolean reserveRaw=true;

    public CompressConfig(Fragment fragment) {
        this(fragment.getActivity());
    }
    public CompressConfig(Context context) {
        cacheDir=context.getExternalCacheDir().getAbsolutePath()+"/compress";
        makeDir(cacheDir);
        maxWidthPixel=getScreenWidth(context);
    }
    private int getScreenWidth(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        return width;
    }
    public void makeDir(String filePath){
        File file=new File(filePath);
        if(file.exists()==false){
            file.mkdirs();
        }
    }

    public Bitmap.CompressFormat getFormat() {
        return format;
    }

    public void setFormat(Bitmap.CompressFormat format) {
        this.format = format;
    }

    public int getCompressType() {
        return compressType;
    }

    public void setCompressType(@CompressType int compressType) {
        this.compressType = compressType;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        if(quality<=0){
            quality=1;
        }else if(quality>100){
            quality=100;
        }
        this.quality = quality;
    }

    public int getEachCompressQuality() {
        return eachCompressQuality;
    }

    public void setEachCompressQuality(int eachCompressQuality) {
        if(eachCompressQuality<=0){
            eachCompressQuality=1;
        }else if(eachCompressQuality>=100){
            eachCompressQuality=99;
        }
        this.eachCompressQuality = eachCompressQuality;
    }

    /**
     * 单位：kb
     * @return
     */
    public float getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * 单位：kb
     * @param maxFileSize
     */
    public void setMaxFileSize(float maxFileSize) {
        this.maxFileSize = maxFileSize;
    }



    public float getIgnoreFileSize() {
        return ignoreFileSize;
    }

    public void setIgnoreFileSize(float ignoreFileSize) {
        this.ignoreFileSize = ignoreFileSize;
    }

    public int getMaxWidthPixel() {
        return maxWidthPixel;
    }

    public void setMaxWidthPixel(int maxWidthPixel) {
        if(maxWidthPixel<=0){
            maxWidthPixel=720;
        }
        this.maxWidthPixel = maxWidthPixel;
        this.maxHeightPixel=0;
    }

    public int getMaxHeightPixel() {
        return maxHeightPixel;
    }

    public void setMaxHeightPixel(int maxHeightPixel) {
        if(maxHeightPixel<=0){
            maxHeightPixel=1280;
        }
        this.maxHeightPixel = maxHeightPixel;
        this.maxWidthPixel = 0;
    }

    /*public int getIgnoreWidthPixel() {
        return ignoreWidthPixel;
    }

    public void setIgnoreWidthPixel(int ignoreWidthPixel) {
        this.ignoreWidthPixel = ignoreWidthPixel;
    }

    public int getIgnoreHeightPixel() {
        return ignoreHeightPixel;
    }

    public void setIgnoreHeightPixel(int ignoreHeightPixel) {
        this.ignoreHeightPixel = ignoreHeightPixel;
    }*/

    public String getCacheDir() {
        return cacheDir;
    }

    public void setCacheDir(String cacheDir) {
        makeDir(cacheDir);
        this.cacheDir = cacheDir;
    }

   /* public boolean isReserveRaw() {
        return reserveRaw;
    }

    public void setReserveRaw(boolean reserveRaw) {
        this.reserveRaw = reserveRaw;
    }*/
}
