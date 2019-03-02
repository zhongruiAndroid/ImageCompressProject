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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.ImageCompress.CompressSingleListener;
import com.github.ImageCompress.MyCompress;
import com.github.ImageCompress.MyMedia;

import java.io.File;

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
        if(TextUtils.isEmpty(imagePath)){
            Toast.makeText(this,"请选择图片",Toast.LENGTH_SHORT).show();
            return;
        }
        if (progressBar == null) {
            progressBar = new ProgressDialog(this);
            progressBar.setTitle("压缩中");
        }
        progressBar.show();
        MyCompress.get(this).setQuality(60).setMaxHeightPixel(1080).setPath(imagePath).setCompressListener(new CompressSingleListener() {
            @Override
            public void onSuccess(String compressPath) {
                log("onSuccess:"+compressPath);
                if (progressBar != null) {
                    progressBar.dismiss();
                }
            }
            @Override
            public void onError(String errorPath, int errorCode) {
                log("errorPath:"+errorPath+"=="+errorCode);
                if (progressBar != null) {
                    progressBar.dismiss();
                }
            }
        }).start();
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
