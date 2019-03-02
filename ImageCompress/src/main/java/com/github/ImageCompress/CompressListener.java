package com.github.ImageCompress;

import java.util.List;

/***
 *   created by zhongrui on 2019/2/24
 */
public abstract class CompressListener {
    public void onNext(String compressPath,int position,int count){};
    public abstract void onSuccess(List<String> compressPathList);
    public abstract void onError(List<String> pathList,String errorPath,int errorCode);

}
