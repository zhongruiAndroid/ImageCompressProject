package com.github.ImageCompress;

/***
 *   created by zhongrui on 2019/2/24
 */
public abstract class CompressSingleListener {
    public abstract void onSuccess(String compressPath);
    public abstract void onError(String errorPath,int errorCode);
}
