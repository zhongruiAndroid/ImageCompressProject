# ImageCompressProject

## [Demo.apk下载](https://raw.githubusercontent.com/zhongruiAndroid/ImageCompressProject/master/file/demo.apk "apk文件")

## [jar下载](https://raw.githubusercontent.com/zhongruiAndroid/ImageCompressProject/master/file/imagecompress.jar "jar文件")


```java
/*像素+质量压缩*/
MyCompress.getBuilder(this)

/*像素压缩*/
MyCompress.getBuilderPixel(this)

/*质量压缩*/
MyCompress.getBuilderQuality(this);

MyCompress.getBuilder(this)
                .setPath(imagePath)//设置需要压缩的图片路径
                //.setPathList(List<String>)   多张图片压缩
                //.setPhotoList(List<ThePhoto>)多张图片压缩
                //设置图片压缩之后的目录,默认值getExternalCacheDir().getAbsolutePath()+"/compress";
                .setCacheDir(compressDir)
                .setMaxWidthPixel(720)//像素压缩之后的图片宽度不超过720,默认值当前屏幕宽度,单位px
                .setMaxHeightPixel(1280)//像素压缩之后的图片高度不超过1280,默认值1920,单位px
                //压缩过程只会满足其中一个条件,maxWidthPixel或者maxHeightpixel
                .setFormat(Bitmap.CompressFormat.JPEG)//设置压缩之后的图片格式，默认JPEG
                .setQuality(55)//质量压缩图片至原图质量的55%，默认值60
                .setEachCompressQuality(10)//每次减少10%的质量,默认值5
                .setMaxFileSize(300)//质量压缩后的文件大小不超过300，默认值0，单位KB
                //优先级：setMaxFileSize不等于0时 > setQuality
                .setIgnoreFileSize(100)//忽略100KB以内的图片，默认值200，单位KB，
                //单张图片压缩(String)       : CompressSingleListener
                //多张图片压缩(List<String>) : CompressListener
                //多个图片对象List<ThePhoto> : CompressObjListener
                .setCompressListener(new CompressSingleListener() {
                    @Override
                    public void onSuccess(String compressPath) {
                         //压缩成功返回的图片路径
                    }
                    @Override
                    public void onError(String errorPath, int errorCode) {
                         //errorPath：压缩失败返回的图片原路径
                         //errorCode：失败之后的状态码
                         //errorCode==1 :错误的图片路径
                         //errorCode==2 :图片路径为空(针对多张图片压缩)
                         //errorCode==3 :图片压缩失败
                         //errorCode==4 :图片路径为空(单张图片压缩)或者图片集合的大小为0
                    }
                })
                .start();
```
## 压缩需要调用start()千万不要漏掉
```java
/*多张图片压缩示例*/
MyCompress.getBuilder(this)
		.setPathList(pathList)
		.setCompressListener(new CompressListener() {
		    //多张图片压缩时，需要手动重写onNext方法
		    public void onNext(String compressPath, int position, int count) {
		        super.onNext(compressPath, position, count);
		    }
		    @Override
		    public void onSuccess(List<String> compressPathList) {
		
		    }
		    @Override
		    public void onError(List<String> pathList, String errorPath, int errorCode) {
		
		    }
		}).start();
```

```java
/*单张图片压缩回调*/
public abstract class CompressSingleListener {
    /*压缩成功返回压缩之后的路径*/
    public abstract void onSuccess(String compressPath);
    public abstract void onError(String errorPath,int errorCode);
}

/*多张图片List<String>压缩回调*/
public abstract class CompressListener {
    /*每次压缩完一张图片后返回保存路径以及当前图片下标，和需要压缩的图片总数*/
    public void onNext(String compressPath,int position,int count){};
    public abstract void onSuccess(List<String> compressPathList);
    public abstract void onError(List<String> pathList,String errorPath,int errorCode);
}

/*多张图片List<ThePhoto>压缩回调*/
public abstract class CompressObjListener {
    public void onNext(ThePhoto photo,int position,int count){};
    public abstract void onSuccess(List<ThePhoto> photoCompressList);
    public abstract void onError(List<ThePhoto> photoList,String errorPath,int errorCode);
}

/* ThePhoto 对象属性*/
public class ThePhoto implements Serializable {
    public String originalPath;//原始图片路径
    public String compressPath;//压缩之后路径
}
```


### 需要自己在子线程压缩方法如下
```java
 CompressConfig compressConfig=new CompressConfig(this);//该对象可以设置压缩的参数
 CompressManager manager=new CompressManager(compressConfig);
  
//压缩失败或者路径无效则返回null

 /*像素+质量压缩*/
 String path= manager.compressPXAndQ("");
 
 /*像素压缩*/
 String path= manager.compressPX("");
 
 /*质量压缩*/
 String path= manager.compressQ("");

```
