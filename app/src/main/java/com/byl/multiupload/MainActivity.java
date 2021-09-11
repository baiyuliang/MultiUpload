package com.byl.multiupload;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.byl.multiupload.upload.UploadUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btn_upload);
        button.setOnClickListener(v -> {
            button.setClickable(false);
            new Handler().postDelayed(() -> {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("请稍后...");
                progressDialog.show();
                new Thread(() -> {
                    List<String> pathList = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        pathList.add("file:///" + i);//模拟本地文件地址
                    }
                    UploadUtil.addUploadTask(111, 1, pathList);
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, UploadingActivity.class);
                        startActivity(intent);
                    });
                }).start();
            }, 100);
        });
        findViewById(R.id.btn_uploading).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UploadingActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}