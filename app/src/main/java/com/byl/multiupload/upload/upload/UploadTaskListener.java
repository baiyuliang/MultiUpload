package com.byl.multiupload.upload.upload;

public interface UploadTaskListener {
    /**
     * queue
     *
     * @param downLoadTask
     */
    void onQueue(UploadTask downLoadTask);


    /**
     * downloading
     *
     * @param downLoadTask
     */
    void onUploading(UploadTask downLoadTask,long currentSize,long totalSize);

    /**
     * cancel
     *
     * @param downLoadTask
     */
    void onCancel(UploadTask downLoadTask);

    /**
     * success
     *
     * @param downLoadTask
     */
    void onFinish(UploadTask downLoadTask);

    /**
     * failure
     *
     * @param downLoadTask
     */
    void onError(int error_code, UploadTask downLoadTask, int code);
}
