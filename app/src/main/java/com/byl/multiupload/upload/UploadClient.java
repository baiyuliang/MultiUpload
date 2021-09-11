package com.byl.multiupload.upload;

import com.byl.multiupload.utils.LogUtils;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class UploadClient {
    Timer timer = new Timer();
    int progress;

    OnUploadProgress onUploadProgress;

    /**
     * 模拟上传
     */
    public void upload(String path, OnUploadListener onUploadListener) {
        int duration = new Random().nextInt(200);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                progress++;
                onUploadProgress.onProgress(progress, 100);
                if (progress >= 100) {
                    timer.cancel();
                    onUploadListener.onSuccess();
                }
            }
        }, 0, duration);
    }

    public void cancel() {
        LogUtils.e("cancel");
        timer.cancel();
    }

    public void setOnUploadProgress(OnUploadProgress onUploadProgress) {
        this.onUploadProgress = onUploadProgress;
    }

    public interface OnUploadProgress {
        void onProgress(long currentSize, long totalSize);
    }

    public interface OnUploadListener {
        void onSuccess();

        void onError();
    }

}
