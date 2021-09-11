package com.byl.multiupload.utils;

import android.app.Activity;

public class FileUtils {

    /**
     * 这里可以做一些压缩操作
     *
     * @param activity
     * @param path
     * @param onCompressListener
     */
    public static void compress(Activity activity, String path, OnCompressListener onCompressListener) {
        String realPath = path;
        //压缩算法...
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //压缩后的路径
        String compressPath = path;
        onCompressListener.onCompressResult(compressPath, realPath);
    }

    public interface OnCompressListener {
        void onCompressResult(String path, String realPath);
    }
}
