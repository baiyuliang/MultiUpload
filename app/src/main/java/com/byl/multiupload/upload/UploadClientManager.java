package com.byl.multiupload.upload;


import com.byl.multiupload.upload.upload.UploadManager;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Title 上传管理类
 * @Description
 * @Author baiyuliang
 */
public class UploadClientManager {
    private static UploadClientManager mInstance = null;
    private boolean mDebug;

    private static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * constructor.
     */
    private UploadClientManager() {
        mDebug = false;
    }

    /**
     * DownLoadHttpClientManager getInstance.
     *
     * @return
     */
    public static UploadClientManager getInstance() {
        try {
            LOCK.lock();
            if (null == mInstance) {
                mInstance = new UploadClientManager();
            }
        } finally {
            LOCK.unlock();
        }
        return mInstance;
    }

    public void init() {
        UploadManager.getInstance().init();
    }

    public void init(int threadCount) {
        UploadManager.getInstance().init(threadCount);
    }

    /**
     * 设定debug类型.
     *
     * @param debug
     */
    public void setDebug(boolean debug) {
        mDebug = debug;
    }

    /**
     * 返回是否debug.
     *
     * @return
     */
    public boolean isDebug() {
        return mDebug;
    }

}
