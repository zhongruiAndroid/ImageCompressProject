package com.github.ImageCompress;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/***
 *   created by zhongrui on 2019/2/24
 */
public class CompressManager {
    /*不存在*/
    public final static int error_not_exist = 1;
    /*路径为空*/
    public final static int error_empty = 2;
    /*压缩失败*/
    public final static int error_compress_fail = 3;

    private static final int what_path = 1000;
    private static final int what_pathList = 1001;
    private static final int what_pathPhoto = 1002;

    private static final int compress_success = 2003;
    private static final int compress_error = 2004;


    private final int sourceType_single = 1;
    private final int sourceType_list = 2;
    private final int sourceType_photo = 3;
    private int sourceType = sourceType_single;
    /**
     * 单个原始文件路径
     */
    private String imagePath;
    /**
     * 原始文件路径list
     */
    private List<String> imagePathList;
    /**
     * 原始文件路径list
     */
    private List<ThePhoto> photoList;

    /**
     * 单个压缩之后的文件路径
     */
    private String imageCompressPath;
    /**
     * 压缩之后的文件路径list
     */
    private List<String> imageCompressPathList;
    /**
     * 压缩之后的文件路径list
     */
    private List<ThePhoto> photoCompressList;


    private CompressConfig config;

    private CompressListener compressListener;
    private CompressSingleListener compressSingleListener;
    private CompressObjListener compressObjListener;
    private Thread thread;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
               /* case what_path:

                    break;*/
                case what_pathList:
                    String savePath = (String) msg.obj;
                    imageCompressPathList.add(savePath);
                    if (compressListener != null) {
                        compressListener.onNext(savePath, msg.arg1, imagePathList.size());
                    }
                    break;
                case what_pathPhoto:
                    ThePhoto thePhoto = (ThePhoto) msg.obj;
                    photoCompressList.add(thePhoto);
                    if (compressObjListener != null) {
                        compressObjListener.onNext(thePhoto, msg.arg1, photoList.size());
                    }
                    break;
                case compress_error:
                    onError((String) msg.obj, msg.arg1);
                    setHandlerEmtpy();
                    break;
                case compress_success:
                    String compressPath = "";
                    if (msg.obj != null) {
                        compressPath = (String) msg.obj;
                    }
                    onSuccess(compressPath);
                    setHandlerEmtpy();
                    break;
            }
        }
    };

    private void setHandlerEmtpy() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        handler = null;
    }


    public CompressManager(CompressConfig config) {
        imageCompressPathList = new ArrayList<>();
        photoCompressList = new ArrayList<>();
        this.config = config;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.sourceType = sourceType_single;
        this.imagePath = imagePath;
    }

    public List<String> getImagePathList() {
        return imagePathList;
    }

    public void setImagePathList(List<String> imagePathList) {
        this.sourceType = sourceType_list;
        this.imagePathList = imagePathList;
    }

    public List<ThePhoto> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<ThePhoto> photoList) {
        this.sourceType = sourceType_photo;
        this.photoList = photoList;
    }

    public void setCompressListener(CompressListener compressListener) {
        this.compressListener = compressListener;
    }

    public void setCompressSingleListener(CompressSingleListener compressSingleListener) {
        this.compressSingleListener = compressSingleListener;
    }

    public void setCompressObjListener(CompressObjListener compressObjListener) {
        this.compressObjListener = compressObjListener;
    }

    public void startCompress() {
        if (sourceType == sourceType_single && TextUtils.isEmpty(getImagePath()) == false) {
            compressForSingle(getImagePath());
        } else if (sourceType == sourceType_list && getImagePathList() != null && getImagePathList().size() > 0) {
            compressForList();
        } else if (sourceType == sourceType_photo && getPhotoList() != null && getPhotoList().size() > 0) {
            compressForPhoto();
        } else {
            try {
                throw new NoPathException("Need to compress images cannot be empty,please setPath(path)");
            } catch (NoPathException e) {
                e.printStackTrace();
            }
        }
    }

    private void compressForSingle(final String originalPath) {
        thread = new Thread(new Runnable() {
            public void run() {
                if (pathIsEmpty(originalPath)) {
                    sendErrorMessage(originalPath, error_empty);
                } else if (pathNotExist(originalPath)) {
                    sendErrorMessage(originalPath, error_not_exist);
                }else if (getImageSize(originalPath) <= config.getIgnoreFileSize() * 1024) {
                    //限制大小之内就不需要压缩,直接复制
                    sendSuccessMessage(fileCopy(originalPath));
                }else {
                    String savePath = compress(originalPath);
                    if (TextUtils.isEmpty(savePath)) {
                        sendErrorMessage(originalPath, error_compress_fail);
                    } else {
//                    sendNextMessage(0,what_path, savePath);
                        //压缩完成
                        sendSuccessMessage(savePath);
                    }
                }
            }
        });
        thread.start();
    }

    public void compressForList() {
        thread = new Thread(new Runnable() {
            boolean isSuccess = true;

            public void run() {
                for (int i = 0, size = imagePathList.size(); i < size; i++) {
                    String originalPath = imagePathList.get(i);
                    if (pathIsEmpty(originalPath)) {
                        isSuccess = false;
                        sendErrorMessage(originalPath, error_empty);
                        break;
                    } else if (pathNotExist(originalPath)) {
                        isSuccess = false;
                        sendErrorMessage(originalPath, error_not_exist);
                        break;
                    }else if (getImageSize(originalPath) <= config.getIgnoreFileSize() * 1024) {
                        //限制大小之内就不需要压缩,直接复制
                        sendNextMessage(i, what_pathList, fileCopy(originalPath));
                    } else {
                        String savePath = compress(originalPath);
                        if (TextUtils.isEmpty(savePath)) {
                            isSuccess = false;
                            sendErrorMessage(originalPath, error_compress_fail);
                        } else {
                            sendNextMessage(i, what_pathList, savePath);
                        }
                    }
                }
                if (isSuccess) {
                    //压缩完成
                    sendSuccessMessage("");
                }


            }
        });
        thread.start();
    }

    public void compressForPhoto() {
        thread = new Thread(new Runnable() {
            boolean isSuccess = true;

            public void run() {
                for (int i = 0, size = photoList.size(); i < size; i++) {
                    ThePhoto thePhoto = photoList.get(i);
                    String originalPath = thePhoto.originalPath;
                    if (pathIsEmpty(originalPath)) {
                        isSuccess = false;
                        sendErrorMessage(originalPath, error_empty);
                        break;
                    } else if (pathNotExist(originalPath)) {
                        isSuccess = false;
                        sendErrorMessage(originalPath, error_not_exist);
                        break;
                    } else if (getImageSize(originalPath) <= config.getIgnoreFileSize() * 1024) {
                        //限制大小之内就不需要压缩,直接复制
                        String savePath = fileCopy(originalPath);
                        ThePhoto newPhoto = new ThePhoto(originalPath, savePath);
                        sendNextMessage(i, what_pathPhoto, newPhoto);
                    } else {
                        String savePath = compress(originalPath);
                        ThePhoto newPhoto = new ThePhoto(originalPath, savePath);
                        if (TextUtils.isEmpty(savePath)) {
                            isSuccess = false;
                            sendErrorMessage(originalPath, error_compress_fail);
                        } else {
                            sendNextMessage(i, what_pathPhoto, newPhoto);
                        }
                    }
                }
                if (isSuccess) {
                    //压缩完成
                    sendSuccessMessage("");
                }
            }
        });
        thread.start();
    }


    private String compress(String originalPath) {
        String savePath;
        if (config.getCompressType() == CompressConfig.type_pixel) {
            //像素压缩
            savePath = compressPixel(originalPath);
        } else if (config.getCompressType() == CompressConfig.type_quality) {
            //质量压缩
            savePath = compressQuality(originalPath);
        } else {
            //像素+质量压缩
            //先像素压缩
            Bitmap bitmap = compressPixelToBitmap(originalPath);
            //再质量压缩
            savePath = compressQuality(bitmap, originalPath);
        }
        return savePath;
    }

    private File getSaveFile(String originalPath) {
        File file = new File(config.getCacheDir() + "/compress_" + new File(originalPath).getName());
        if (file.isFile() && file.exists()) {
            file.delete();
        }
        return file;
    }

    private void sendSuccessMessage(String compressPath) {
        Message message = Message.obtain();
        message.what = compress_success;
        message.obj = compressPath;
        handler.sendMessage(message);
    }

    private void sendNextMessage(int position, int what, Object savePath) {
        Message message = Message.obtain();
        message.what = what;
        message.arg1 = position;
        message.obj = savePath;
        handler.sendMessage(message);
    }

    private void sendErrorMessage(String errorPath, int code) {
        Message message = Message.obtain();
        message.what = compress_error;
        message.arg1 = code;
        message.obj = errorPath;
        handler.sendMessage(message);
    }

    private String fileCopy(String oldFilePath) {
        File saveFile = getSaveFile(oldFilePath);
        String savePath = saveFile.getAbsolutePath();
        //复制过去
        boolean copyResult = fileCopy(oldFilePath, savePath);
        if(copyResult==false){
            savePath=oldFilePath;
        }
        return savePath;
    }
    private boolean fileCopy(String oldFilePath, String newFilePath) {
        File file=new File(oldFilePath);
        //如果原文件不存在
        if (file.isFile()&&file.exists()) {
            file.delete();
        }
        //获得原文件流
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);

            byte[] data = new byte[1024];
            //输出流
            FileOutputStream outputStream = new FileOutputStream(new File(newFilePath));
            //开始处理流
            while (inputStream.read(data) != -1) {
                outputStream.write(data);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    private String bitmapToFile(Bitmap compress, String originalPath) {
        File file = new File(config.getCacheDir() + "/compress_" + new File(originalPath).getName());
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            compress.compress(config.getFormat(), 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            compress.recycle();
            compress = null;
        }
        return file.getAbsolutePath();
    }


    private long getImageSize(String path) {
        return new File(path).length();
    }

    private boolean pathIsEmpty(String path) {
        return !pathNotEmpty(path);
    }

    private boolean pathNotEmpty(String path) {
        /*判断是否为空*/
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return true;
    }

    private boolean pathNotExist(String path) {
        return !pathIsExist(path);
    }

    private boolean pathIsExist(String path) {
        /*判断是否存在该文件*/
        File file = new File(path);
        if (file.exists() == false || file.isFile() == false) {
            return false;
        }
        return true;
    }

    private void onSuccess(String compressPath) {
        if (sourceType == sourceType_single) {
            if (compressSingleListener != null) {
                compressSingleListener.onSuccess(compressPath);
            }
        } else if (sourceType == sourceType_list) {
            if (compressListener != null) {
                compressListener.onSuccess(imageCompressPathList);
            }
        } else if (sourceType == sourceType_photo) {
            if (compressObjListener != null) {
                compressObjListener.onSuccess(photoCompressList);
            }
        }
    }

    private void onError(String errorPath, int code) {
        if (compressSingleListener != null && sourceType == sourceType_single) {
            compressSingleListener.onError(imagePath, code);
        } else if (compressListener != null && sourceType == sourceType_list) {
            compressListener.onError(imagePathList, errorPath, code);
        } else if (compressObjListener != null && sourceType == sourceType_photo) {
            compressObjListener.onError(photoList, errorPath, code);
        }
    }

    /**
     * 像素压缩
     *
     * @param originalPath
     */
    private Bitmap compressPixelToBitmap(String originalPath) {
        Bitmap bitmap;
        if (config.getMaxWidthPixel() > 0) {
            bitmap = CompressUtils.compressBitmapForWidth(originalPath, config.getMaxWidthPixel());
        } else {
            bitmap = CompressUtils.compressBitmapForHeight(originalPath, config.getMaxHeightPixel());
        }
        return bitmap;
    }

    private String compressPixel(String originalPath) {
        Bitmap bitmap = compressPixelToBitmap(originalPath);
        File saveFile = getSaveFile(originalPath);
        String savePath = saveFile.getAbsolutePath();
        try {
            bitmap.compress(config.getFormat(), 100, new FileOutputStream(saveFile));
        } catch (FileNotFoundException e) {
            savePath = null;
            e.printStackTrace();
        }
        return savePath;
    }

    /**
     * 质量压缩
     *
     * @param originalPath
     */
    private String compressQuality(String originalPath) {
        Bitmap bitmap = CompressUtils.compressBitmapForScale(originalPath, 1);
        return compressQuality(bitmap, originalPath);
    }

    private String compressQuality(Bitmap bitmap, String originalPath) {
        File saveFile = getSaveFile(originalPath);
        String savePath;
        if (config.getMaxFileSize() > 0) {
            savePath = CompressUtils.compressForQuality(saveFile, bitmap, config.getFormat(), config.getEachCompressQuality(), config.getMaxFileSize());
        } else {
            savePath = CompressUtils.compressForQuality(saveFile, bitmap, config.getFormat(), config.getQuality());
        }
        return savePath;
    }


}
