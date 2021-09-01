package com.example.javaapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.attribute.FileTime;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "samples.flutter.dev/data";
    int imagecode = 1;
    File imageFile;
    MethodChannel.Result mresult;


    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler((call, result) -> {
            if (call.method.equals("updateProfile")){
                getimage();
                mresult = result;


            }
        });


    }

    private void getimage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, imagecode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageURI = data.getData();
//        realPath = ImageFilePath.getPath(SignupActivity.this, data.getData());
        imageFile = new File(getRealPathFromURI(selectedImageURI));
        mresult.success(imageFile.toString());
        System.out.println(imageFile.toString());

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            cursor.moveToFirst();
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
