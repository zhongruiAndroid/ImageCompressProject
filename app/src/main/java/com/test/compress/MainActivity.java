package com.test.compress;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.ImageCompress.CompressListener;
import com.github.ImageCompress.CompressObjListener;
import com.github.ImageCompress.CompressSingleListener;
import com.github.ImageCompress.MyCompress;
import com.github.ImageCompress.MyMedia;
import com.github.ImageCompress.ThePhoto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btTakePhoto;
    Button btImage;
    Button btCompress;
    ImageView iv;
    String tag = this.getClass().getSimpleName() + "e>>>>:";
    private Uri photoUri;
    private File cameraDefaultFile;

    private String imagePath;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btTakePhoto = findViewById(R.id.btTakePhoto);
        btTakePhoto.setOnClickListener(this);

        btImage = findViewById(R.id.btImage);
        btImage.setOnClickListener(this);

        btCompress = findViewById(R.id.btCompress);
        btCompress.setOnClickListener(this);


        iv = findViewById(R.id.iv);

//        Luban.with(this).launch();
//        TextView tv=null;
//        log(path);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btCompress:
                imageCompress();
                    break;
            case R.id.btImage:
                selectImage();
                    break;
            case R.id.btTakePhoto:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},900);
                }else{
                    takePhoto();
                }
                break;
        }
    }

    private void imageCompress() {
       /* if(TextUtils.isEmpty(imagePath)){
            Toast.makeText(this,"请选择图片",Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (progressBar == null) {
            progressBar = new ProgressDialog(this);
            progressBar.setTitle("压缩中");
        }
        progressBar.show();
        String absoluteFile = Environment.getExternalStorageDirectory().getAbsoluteFile()+"/190302/";

        List<String> list=new ArrayList<>();
        List<ThePhoto> photoList=new ArrayList<>();
        ThePhoto thePhoto=new ThePhoto(absoluteFile+"1.jpg");
        photoList.add(thePhoto);

        thePhoto=new ThePhoto(absoluteFile+"2.jpg");
        photoList.add(thePhoto);

        thePhoto=new ThePhoto(absoluteFile+"5.jpg");
        photoList.add(thePhoto);
        thePhoto=new ThePhoto(absoluteFile+"3.jpg");
        photoList.add(thePhoto);

        thePhoto=new ThePhoto(absoluteFile+"4.jpg");
        photoList.add(thePhoto);


        list.add(absoluteFile+"1.jpg");
        list.add(absoluteFile+"2.jpg");
        list.add(absoluteFile+"3.jpg");
        list.add("");
        list.add(absoluteFile+"4.jpg");
        CompressSingleListener compressSingleListener = new CompressSingleListener() {
            @Override
            public void onSuccess(String compressPath) {
                log("onSuccess:" + compressPath);
                if (progressBar != null) {
                    progressBar.dismiss();
                }
            }

            @Override
            public void onError(String errorPath, int errorCode) {
                log("errorPath:" + errorPath + "==" + errorCode);
                if (progressBar != null) {
                    progressBar.dismiss();
                }
            }
        };
        CompressListener compressListener = new CompressListener() {
            @Override
            public void onNext(String compressPath, int position, int count) {
                super.onNext(compressPath, position, count);
                log(position + "===" + count + "onNext:" + compressPath);
                progressBar.setTitle("压缩中" + position + "===" + count);
            }

            @Override
            public void onSuccess(List<String> compressPathList) {
                log("onSuccess:" + compressPathList.size());
                if (progressBar != null) {
                    progressBar.dismiss();
                }
            }

            @Override
            public void onError(List<String> pathList, String errorPath, int errorCode) {
                log("errorPath:" + errorPath + "==" + errorCode);
                if (progressBar != null) {
                    progressBar.dismiss();
                }
            }
        };
        CompressObjListener compressObjListener = new CompressObjListener() {
            @Override
            public void onNext(ThePhoto photo, int position, int count) {
                super.onNext(photo, position, count);
                log(position + "===" + count + "onNext:" + photo.compressPath);
                progressBar.setTitle("压缩中" + position + "===" + count);
            }

            @Override
            public void onSuccess(List<ThePhoto> photoCompressList) {
                log("onSuccess:" + photoCompressList.size());
                if (progressBar != null) {
                    progressBar.dismiss();
                }
            }

            @Override
            public void onError(List<ThePhoto> photoList, String errorPath, int errorCode) {
                log("errorPath:" + errorPath + "==" + errorCode);
                if (progressBar != null) {
                    progressBar.dismiss();
                }
            }
        };
        //compressSingleListener
        //compressListener
        //compressObjListener
        MyCompress.get(this).setQuality(60).setEachCompressQuality(5).setMaxWidthPixel(1080).setPhotoList(null).setCompressListener(compressObjListener).start();
    }

    private void selectImage() {
        Intent intent = MyMedia.getOpenPhotoAlbumIntent();
        startActivityForResult(intent,200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==900){
            if(grantResults!=null&&grantResults.length>0&&grantResults[0]==0){
                takePhoto();
            }
        }
    }

    private void takePhoto() {
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        filePath = createFilePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
        startActivityForResult(intent, 100);*/


        String name=System.currentTimeMillis()+".jpg";
        String absoluteFile = Environment.getExternalStorageDirectory().getAbsoluteFile()+"/190302/";
        String filePath = getExternalFilesDir("a").getAbsoluteFile()+"/190302/";
        String absolutePath = getFilesDir().getAbsolutePath()+"/190302aa/";
        log("name"+name);
        log("absoluteFile==:"+absoluteFile);
        log("absolutePath==:"+absolutePath);

        photoUri = MyMedia.fileToUriForExternal(this, absoluteFile, name);

        cameraDefaultFile = MyMedia.getCameraDefaultFile();
        photoUri = MyMedia.fileToUri(this, cameraDefaultFile);
        Intent takePhotoIntent = MyMedia.getTakePhotoIntent(photoUri);
        startActivityForResult(takePhotoIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case 200:
                    String string=MyMedia.getPhotoPath(this,data);
                    imagePath=string;
                    log(string);
                    iv.setImageURI(data.getData());
                break;
                case 100:
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(photoUri);
                    sendBroadcast(intent);
                    imagePath=cameraDefaultFile.getAbsolutePath();
                    iv.setImageURI(photoUri);
                    break;
            }
        }

    }

    public void log(String string) {
        Log.e(tag, getPackageName() + "======" + string);

    }


}
