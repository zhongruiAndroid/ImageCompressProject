package com.github.ImageCompress;

import java.util.List;

/***
 *   created by zhongrui on 2019/2/24
 */
public abstract class CompressObjListener {
    public void onNext(ThePhoto photo,int position,int count){};
    public abstract void onSuccess(List<ThePhoto> photoCompressList);
    public abstract void onError(List<ThePhoto> photoList,String errorPath,int errorCode);
}
